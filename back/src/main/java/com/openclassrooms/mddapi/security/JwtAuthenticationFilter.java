package com.openclassrooms.mddapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT authentication filter. Intercepts requests, extracts JWT token from Authorization header,
 * validates it, and sets authentication in security context.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  /**
   * Filters each HTTP request to extract and validate JWT tokens. Extracts the JWT from the
   * Authorization header, validates it, and sets the authentication in the security context if
   * valid.
   *
   * @param request     HTTP request
   * @param response    HTTP response
   * @param filterChain filter chain to continue request processing
   * @throws ServletException if servlet error occurs
   * @throws IOException      if I/O error occurs
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    Long userId = null;
    String jwt = null;

    // Extract JWT from Authorization header
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      userId = jwtUtil.extractUserId(jwt);
    }

    // Validate token and set authentication
    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = ((UserDetailsServiceImpl) userDetailsService).loadUserById(userId);

      if (jwtUtil.validateToken(jwt, userId)) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}