package com.nivtron.smartmoney.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  @Value("${jwt.secret}")
  private String SECRET;

  @Value("${jwt.expiration}")
  private long EXPIRATION;

  private Key getKey() {
    return Keys.hmacShaKeyFor(SECRET.getBytes());
  }

  public String generateToken(String email, String role) {
    return Jwts.builder()
        .subject(email)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(getKey())
        .compact();
  }

  public String extractUsername(String token) {
    return getClaims(token).getSubject();
  }

  public String extractRole(String token) {
    return getClaims(token).get("role", String.class);
  }

  public boolean isValid(String token, String email) {
    return extractUsername(token).equals(email);
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
