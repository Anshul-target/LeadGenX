package com.example.lead_genX.auth.service;

import com.example.lead_genX.auth.entiy.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final Key signingKey;
    private final Duration accessTokenValidity;
    private final Duration refreshTokenValidity;

    public JwtService(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.access-token-expiration:1800}") long accessTokenSeconds,
            @Value("${jwt.refresh-token-expiration:1209600}") long refreshTokenSeconds
    ) {

        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);

        this.accessTokenValidity = Duration.ofSeconds(accessTokenSeconds);
        this.refreshTokenValidity = Duration.ofSeconds(refreshTokenSeconds);
    }



    public String generateAccessToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();

        String roles=user.getRole();
        claims.put("roles", roles);

        return buildToken(claims, user.getEmail(), accessTokenValidity);
    }


    public String generateRefreshToken(UserEntity userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("typ", "refresh");
        return buildToken(claims, userDetails.getEmail(), refreshTokenValidity);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, Duration validity) {
        Instant now = Instant.now();
        Instant expiry = now.plus(validity);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    public boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp.before(Date.from(Instant.now()));
    }

    /**
     * Validates token for the provided user details.
     * Checks signature (implicitly via parse) + subject + expiration.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            // parsing exceptions (signature invalid, malformed, expired) will be handled here
            return false;
        }
    }

    // --------------------------
    // Optional helpers (roles)
    // --------------------------

    /**
     * Extract roles stored in token as List<String>.
     * Returns empty list if claim absent or not a list.
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        try {
            Object rolesObj = extractClaim(token, claims -> claims.get("roles"));
            if (rolesObj instanceof Collection<?>) {
                return ((Collection<?>) rolesObj).stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
