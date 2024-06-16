package com.tuskers.backend.jwt.service;

import com.tuskers.backend.jwt.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.security.jwt.expirationMs}")
    private long jwtExpirationMs;

    public String getTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpirationMs);
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpirationMs);
    }

    private String buildToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails,
            long jwtExpiration) {

        logger.info("generating token");
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T>T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.err.println("Token has expired" + e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            System.err.println("Unsupported JWT token" + e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            System.err.println("Malformed JWT token" + e.getMessage());
            throw e;
        } catch (SecurityException e) {
            System.err.println("JWT signature does not match locally computed signature" + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("JWT token compact of handler are invalid" + e.getMessage());
            throw e;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
