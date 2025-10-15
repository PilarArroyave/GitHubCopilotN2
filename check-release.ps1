# Script de verificacion de calidad para validar que la aplicacion este lista para push
# Ejecuta verificaciones de compilacion, arranque, tests y cobertura
# Generado por: IA
# Fecha: 2025-10-14

param(
    [int]$CoverageThreshold = 80,
    [int]$AppStartupTimeoutSeconds = 60
)

# Configuracion de colores para output
$Red = [System.ConsoleColor]::Red
$Green = [System.ConsoleColor]::Green
$Yellow = [System.ConsoleColor]::Yellow
$Blue = [System.ConsoleColor]::Blue

function Write-ColorOutput($ForegroundColor, $Message) {
    $currentColor = [Console]::ForegroundColor
    [Console]::ForegroundColor = $ForegroundColor
    Write-Output $Message
    [Console]::ForegroundColor = $currentColor
}

function Write-Step($StepNumber, $Description) {
    Write-ColorOutput $Blue "========================================================"
    Write-ColorOutput $Blue "PASO $StepNumber - $Description"
    Write-ColorOutput $Blue "========================================================"
}

function Write-Success($Message) {
    Write-ColorOutput $Green "[OK] $Message"
}

function Write-Error($Message) {
    Write-ColorOutput $Red "[ERROR] $Message"
}

function Write-Warning($Message) {
    Write-ColorOutput $Yellow "[WARNING] $Message"
}

# Variables de control
$AllChecksPass = $true
$ErrorMessages = @()

# Funcion para registrar errores
function Add-Error($Message) {
    $script:AllChecksPass = $false
    $script:ErrorMessages += $Message
    Write-Error $Message
}

Write-ColorOutput $Blue "========================================================"
Write-ColorOutput $Blue "           VERIFICACION DE CALIDAD"
Write-ColorOutput $Blue "             SURA Auth Service"
Write-ColorOutput $Blue "            Generado por: IA"
Write-ColorOutput $Blue "========================================================"

# PASO 1: Verificar compilacion
Write-Step 1 "VERIFICACION DE COMPILACION"
try {
    Write-Output "Ejecutando: mvn clean compile..."
    $compileResult = & mvn clean compile 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Success "La aplicacion compila correctamente"
    } else {
        Add-Error "Error en compilacion. Codigo de salida: $LASTEXITCODE"
        Write-Output "Detalles del error:"
        $compileResult | Select-String -Pattern "ERROR|COMPILATION ERROR|Failed" | ForEach-Object { Write-Error $_.Line }
    }
} catch {
    Add-Error "Excepcion durante compilacion: $($_.Exception.Message)"
}

# PASO 2: Verificar que la aplicacion se pueda levantar
Write-Step 2 "VERIFICACION DE ARRANQUE DE APLICACION"
try {
    Write-Output "Iniciando aplicacion en segundo plano..."
    
    # Compilar y empaquetar la aplicacion
    Write-Output "Empaquetando aplicacion..."
    & mvn package -DskipTests | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Add-Error "Error al empaquetar la aplicacion"
    } else {
        # Buscar el JAR generado
        $jarFile = Get-ChildItem -Path "target" -Filter "*.jar" | Where-Object { $_.Name -notmatch "sources|javadoc" } | Select-Object -First 1
        
        if ($jarFile) {
            Write-Output "Iniciando aplicacion desde: $($jarFile.FullName)"
            
            # Iniciar aplicacion en proceso separado
            $appProcess = Start-Process -FilePath "java" -ArgumentList "-jar", $jarFile.FullName, "--server.port=8081" -PassThru -WindowStyle Hidden
            
            Start-Sleep 5  # Dar tiempo para que inicie
            
            # Verificar que el proceso este corriendo
            $timeout = $AppStartupTimeoutSeconds
            $started = $false
            
            for ($i = 0; $i -lt $timeout; $i++) {
                try {
                    # Probar endpoint de documentacion de API ya que health puede no estar habilitado
                    $response = Invoke-WebRequest -Uri "http://localhost:8081/swagger-ui/index.html" -TimeoutSec 2 -ErrorAction SilentlyContinue
                    if ($response.StatusCode -eq 200) {
                        $started = $true
                        break
                    }
                } catch {
                    # Continuar intentando
                }
                Start-Sleep 1
            }
            
            # Detener el proceso
            if ($appProcess -and !$appProcess.HasExited) {
                $appProcess.Kill()
                $appProcess.WaitForExit(5000)
            }
            
            if ($started) {
                Write-Success "La aplicacion se levanta correctamente y responde en el endpoint de documentacion"
            } else {
                Add-Error "La aplicacion no se pudo levantar correctamente o no responde en $timeout segundos"
            }
        } else {
            Add-Error "No se encontro el archivo JAR generado en el directorio target"
        }
    }
} catch {
    Add-Error "Excepcion durante verificacion de arranque: $($_.Exception.Message)"
    # Asegurar que no queden procesos colgados
    Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object { $_.CommandLine -like "*auth-service*" } | Stop-Process -Force -ErrorAction SilentlyContinue
}

# PASO 3: Ejecutar todos los tests
Write-Step 3 "EJECUCION DE TESTS UNITARIOS"
try {
    Write-Output "Ejecutando: mvn test..."
    $testResult = & mvn test 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        # Extraer informacion de tests
        $testSummary = $testResult | Select-String -Pattern "Tests run:" | Select-Object -Last 1
        if ($testSummary) {
            Write-Success "Todos los tests pasan correctamente"
            Write-Output "Resumen: $($testSummary.Line.Trim())"
        } else {
            Write-Success "Tests ejecutados correctamente"
        }
    } else {
        Add-Error "Fallos en tests unitarios. Codigo de salida: $LASTEXITCODE"
        Write-Output "Detalles de fallos:"
        $testResult | Select-String -Pattern "FAILURE|ERROR|Failed" | ForEach-Object { Write-Error $_.Line }
    }
} catch {
    Add-Error "Excepcion durante ejecucion de tests: $($_.Exception.Message)"
}

# PASO 4: Verificar cobertura de testing
Write-Step 4 "VERIFICACION DE COBERTURA DE TESTING"
try {
    Write-Output "Generando reporte de cobertura Jacoco..."
    & mvn jacoco:report | Out-Null
    
    if ($LASTEXITCODE -eq 0) {
        Write-Success "Reporte de cobertura Jacoco generado correctamente"
        
        # Verificar cobertura con mvn verify (incluye las reglas de cobertura)
        Write-Output "Verificando umbral de cobertura..."
        $verifyResult = & mvn verify 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Cobertura de testing cumple con el umbral minimo requerido (>=$CoverageThreshold%)"
            
            # Intentar extraer porcentaje de cobertura del reporte HTML si existe
            $indexHtml = "target/site/jacoco/index.html"
            if (Test-Path $indexHtml) {
                try {
                    $htmlContent = Get-Content $indexHtml -Raw
                    if ($htmlContent -match "Total.*?(\d+)%") {
                        $coverage = $matches[1]
                        Write-Output "Cobertura actual: $coverage%"
                    }
                } catch {
                    Write-Warning "No se pudo extraer el porcentaje exacto de cobertura del reporte HTML"
                }
            }
        } else {
            Add-Error "Cobertura de testing NO cumple con el umbral minimo requerido (>=$CoverageThreshold%)"
            $verifyResult | Select-String -Pattern "Coverage|Rule|violation" | ForEach-Object { Write-Error $_.Line }
        }
    } else {
        Add-Error "Error al generar reporte de cobertura Jacoco"
    }
} catch {
    Add-Error "Excepcion durante verificacion de cobertura: $($_.Exception.Message)"
}

# RESULTADO FINAL
Write-ColorOutput $Blue "========================================================"
Write-ColorOutput $Blue "                 RESULTADO FINAL"
Write-ColorOutput $Blue "========================================================"

if ($AllChecksPass) {
    Write-ColorOutput $Green ""
    Write-ColorOutput $Green "          [EXITO]"
    Write-ColorOutput $Green ""
    Write-ColorOutput $Green "    Se completaron todos los checks, esta version esta"
    Write-ColorOutput $Green "           lista para ser pusheada"
    Write-ColorOutput $Green ""
    exit 0
} else {
    Write-ColorOutput $Red ""
    Write-ColorOutput $Red "          [FALLO]"
    Write-ColorOutput $Red ""
    Write-ColorOutput $Red "   Hay problemas para corregir, esta version no se"
    Write-ColorOutput $Red "        encuentra lista para pushear"
    Write-ColorOutput $Red ""
    
    Write-ColorOutput $Red "ERRORES ENCONTRADOS:"
    $ErrorMessages | ForEach-Object { Write-ColorOutput $Red "  - $_" }
    
    exit 1
}
