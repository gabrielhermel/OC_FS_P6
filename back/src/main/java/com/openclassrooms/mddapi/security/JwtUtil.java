package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for JWT token operations. Handles token generation, validation, and claims
 * extraction.
 */
@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  /**
   * Generates the signing key from the secret.
   *
   * @return SecretKey for signing tokens
   */
  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Extracts user ID from token.
   *
   * @param token JWT token
   * @return user ID
   * @throws JwtException if token subject is not a valid user ID
   */
  public Long extractUserId(String token) {
    try {
      return Long.parseLong(extractClaim(token, Claims::getSubject));
    } catch (NumberFormatException e) {
      throw new JwtException("Invalid token subject");
    }
  }

  /**
   * Extracts expiration date from token.
   *
   * @param token JWT token
   * @return expiration date
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts a specific claim from token.
   *
   * @param token          JWT token
   * @param claimsResolver function to extract claim
   * @return extracted claim
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extracts all claims from token.
   *
   * @param token JWT token
   * @return all claims
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Checks if token is expired.
   *
   * @param token JWT token
   * @return true if expired
   */
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Generates token for user ID.
   *
   * @param userId user ID
   * @return JWT token
   */
  public String generateToken(Long userId) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userId.toString());
  }

  /**
   * Creates token with claims and subject.
   *
   * @param claims  additional claims
   * @param subject token subject (user ID)
   * @return JWT token
   */
  private String createToken(Map<String, Object> claims, String subject) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * Validates token against user ID.
   *
   * @param token  JWT token
   * @param userId user ID to validate against
   * @return true if valid
   */
  public Boolean validateToken(String token, Long userId) {
    final Long extractedUserId = extractUserId(token);
    return (extractedUserId.equals(userId) && !isTokenExpired(token));
  }
}