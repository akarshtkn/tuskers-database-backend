package com.tuskers.backend.jwt.service.Implementation;

import com.tuskers.backend.jwt.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
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
public class JwtServiceImpl implements JwtService {

    Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Value("${spring.security.jwt.secretKey}")
    private String secretKey;

    @Value("${spring.security.jwt.expirationMs}")
    private long jwtExpirationMs;

//    public String getTokenFromHeader(HttpServletRequest request){
//        logger.info("Executing business logic to extract token from authorization header");
//        String bearerToken = request.getHeader("Authorization");
//        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String token = null;
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("token")){
                    token = cookie.getValue();
                }
            }
        }
        logger.info("Token : {}", token);
        return token;
    }

    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        logger.info("Extracted username : {}", username);
        return username;
    }

    public boolean isTokenExpired(String token) {
        logger.info("Executing business logic to check expiration of token");
        boolean isExpired = extractClaim(token, Claims::getExpiration).before(new Date());

        logger.info("Token expired : {}", isExpired);
        return isExpired;
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails) {
        logger.info("Executing business logic to generate token with extra claims");
        return buildToken(extraClaims, userDetails, jwtExpirationMs);
    }

    public String generateToken(UserDetails userDetails) {
        logger.info("Executing business logic to generate token without extra claims");
        return buildToken(new HashMap<>(), userDetails, jwtExpirationMs);
    }

    private String buildToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails,
            long jwtExpiration) {

        logger.info("Executing business logic to generate token");
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T>T extractClaim(String token, Function<Claims, T> claimsResolver) {
        logger.info("Executing business logic to extract claim");
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        logger.info("Executing business logic to extract all claims");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("Token has expired{}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token{}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token{}", e.getMessage());
            throw e;
        } catch (SecurityException e) {
            logger.error("JWT signature does not match locally computed signature{}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("JWT token compact of handler are invalid{}", e.getMessage());
            throw e;
        }
    }

    private Key getSignInKey() {
        logger.info("Executing business logic to build sign in key");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
