package com.example.lasttask.security.provider;

import com.example.lasttask.model.entity.UserEntity;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;

@Service
public class JWTokenProvider {

  @Value("${jwt.secret.key.access}")
  private  String jwtAccessSecret;

  @Value("${jwt.secret.expiration.access}")
  private int accessTokenExpiration;


  public String generateAccessToken(UserEntity user) {
    String subj = user.getEmail();
    return "Bearer " +  Jwts.builder().setSubject(subj).claim("role", user.getRole()).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + accessTokenExpiration)).signWith(SignatureAlgorithm.HS512, jwtAccessSecret)
            .compact();
  }


  public void claimProvider(String token, HttpServletRequest request) {
    Jws<Claims> claimsJws =  Jwts.parser().setSigningKey(jwtAccessSecret).parseClaimsJws(token);

    if (claimsJws != null){
      String subject = claimsJws.getBody().getSubject();

      UsernamePasswordAuthenticationToken authentication = new  UsernamePasswordAuthenticationToken(subject, null,
              Collections.singletonList(new SimpleGrantedAuthority(claimsJws.getBody().get("role", String.class))));
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } else {
      UsernamePasswordAuthenticationToken authentication = new  UsernamePasswordAuthenticationToken(null, null,
              null);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

  }
}
