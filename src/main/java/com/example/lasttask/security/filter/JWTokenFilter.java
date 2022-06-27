package com.example.lasttask.security.filter;

import com.example.lasttask.security.provider.JWTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JWTokenFilter extends OncePerRequestFilter {

  private final JWTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String token = getTokenFromHeader(request);

      if (token != null) {
        jwtTokenProvider.claimProvider(token, request);
      }
    }
    catch(JwtValidationException ex){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    filterChain.doFilter(request, response);
  }


  private String getTokenFromHeader(HttpServletRequest request) {
    String header = request.getHeader(AUTHORIZATION);
    if (header != null && header.startsWith("Bearer ")) {
      return header.substring("Bearer ".length());
    }
    return null;
  }
}





