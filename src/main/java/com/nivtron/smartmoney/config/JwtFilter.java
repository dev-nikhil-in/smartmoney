package com.nivtron.smartmoney.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    try {
      String token = header.substring(7);
      String email = jwtService.extractUsername(token);
      String role = jwtService.extractRole(token);

      if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        if (jwtService.isValid(token, email)) {
          SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
    }

    chain.doFilter(request, response);
  }
}
