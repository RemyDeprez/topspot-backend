package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            // Essaye d'appeler parserBuilder (jjwt >= 0.11)
            Method parserBuilderMethod = Jwts.class.getMethod("parserBuilder");
            Object parserBuilder = parserBuilderMethod.invoke(null);
            Method setSigningKey = parserBuilder.getClass().getMethod("setSigningKey", Key.class);
            Object builderWithKey = setSigningKey.invoke(parserBuilder, getSignInKey());
            Method buildMethod = builderWithKey.getClass().getMethod("build");
            Object parser = buildMethod.invoke(builderWithKey);
            Method parseClaimsJws = parser.getClass().getMethod("parseClaimsJws", String.class);
            Object jws = parseClaimsJws.invoke(parser, token);
            Method getBody = jws.getClass().getMethod("getBody");
            return (Claims) getBody.invoke(jws);
        } catch (NoSuchMethodException nsme) {
            try {
                // Fallback vers l'ancienne API : Jwts.parser()
                Method parserMethod = Jwts.class.getMethod("parser");
                Object parser = parserMethod.invoke(null);
                Method setSigningKey = parser.getClass().getMethod("setSigningKey", Key.class);
                Object parserWithKey = setSigningKey.invoke(parser, getSignInKey());
                Method parseClaimsJws = parserWithKey.getClass().getMethod("parseClaimsJws", String.class);
                Object jws = parseClaimsJws.invoke(parserWithKey, token);
                Method getBody = jws.getClass().getMethod("getBody");
                return (Claims) getBody.invoke(jws);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to parse JWT with fallback parser()", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT", e);
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
