package com.amazon.test_task.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filter for intercepting HTTP requests to process JWT authentication.
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils; // Utility for handling JWT operations.

    // Process each HTTP request to authenticate users based on JWT tokens.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the Authorization header from the request.
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Check if the Authorization header contains a JWT starting with "Bearer ".
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Extract the JWT token.
            try {
                username = jwtUtils.getEmail(jwt); // Retrieve the username from the JWT.
            } catch (ExpiredJwtException e) {
                System.out.println("JWT has expired"); // Handle expired token.
            }
        }

        // If the token contains a valid username and the user is not yet authenticated, set up authentication.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username, null, jwtUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList());
            SecurityContextHolder.getContext().setAuthentication(token); // Set the authentication in the security context.
        }

        // Continue processing the filter chain.
        filterChain.doFilter(request, response);
    }
}
