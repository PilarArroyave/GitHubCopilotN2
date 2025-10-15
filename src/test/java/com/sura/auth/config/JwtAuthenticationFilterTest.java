package com.sura.auth.config;

import com.sura.auth.service.CustomUserDetailsService;
import com.sura.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JwtAuthenticationFilter
 *
 * Covers: valid token, invalid token, no token, exception handling.
 */
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    /**
     * Test: Should authenticate user when token is valid
     * Arrange: Mock valid JWT, username, userDetails, and validation
     * Act: Call doFilterInternal
     * Assert: SecurityContext is set, filterChain proceeds
     */
    @Test
    void doFilterInternal_validToken_authenticatesUser() throws ServletException, IOException {
        String jwt = "valid.jwt.token";
        String username = "testuser";
        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(jwt, username)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isInstanceOf(UsernamePasswordAuthenticationToken.class)
            .extracting(auth -> ((UsernamePasswordAuthenticationToken) auth).getPrincipal())
            .isEqualTo(userDetails);
        verify(filterChain).doFilter(request, response);
    }

    /**
     * Test: Should not authenticate when token is invalid
     * Arrange: Mock invalid JWT, username, userDetails, and validation
     * Act: Call doFilterInternal
     * Assert: SecurityContext remains null, filterChain proceeds
     */
    @Test
    void doFilterInternal_invalidToken_doesNotAuthenticate() throws ServletException, IOException {
        String jwt = "invalid.jwt.token";
        String username = "testuser";
        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(jwt, username)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    /**
     * Test: Should not authenticate when no token is present
     * Arrange: No Authorization header
     * Act: Call doFilterInternal
     * Assert: SecurityContext remains null, filterChain proceeds
     */
    @Test
    void doFilterInternal_noToken_doesNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    /**
     * Test: Should handle exception and continue filter chain
     * Arrange: Mock extractUsername to throw exception
     * Act: Call doFilterInternal
     * Assert: SecurityContext remains null, filterChain proceeds
     */
    @Test
    void doFilterInternal_exceptionInProcess_logsErrorAndContinues() throws ServletException, IOException {
        String jwt = "jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenThrow(new RuntimeException("JWT error"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}
