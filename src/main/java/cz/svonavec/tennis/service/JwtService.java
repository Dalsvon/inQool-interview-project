package cz.svonavec.tennis.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${application.jwt.secret}")
    private String secretKey;

    @Value("${application.jwt.expiration-time:86400000}")
    private long jwtExpiration;

    @Value("${application.jwt.refresh.expiration-time:604800000}")
    private long jwtRefreshExpiration;

    public long getJwtExpiration() {
        return jwtExpiration;
    }

    public long getJwtRefreshExpiration() {
        return jwtRefreshExpiration;
    }

    public String generateToken(UserDetails user) {
        String phone = user.getUsername();
        return Jwts.builder()
                .setSubject(phone)
                .claim("type", "access")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getJwtExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        String phone = user.getUsername();
        return Jwts.builder()
                .setSubject(phone)
                .claim("type", "refresh")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getJwtRefreshExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails user, String type) {
        if (type == null) {
            type = "access";
        }
        String phone = user.getUsername();
        return phone.equals(extractUsername(token)) &&
                extractExpiration(token).after(new Date()) &&
                type.equals(extractType(token));
    }

    public String extractUsername(String token) {
        return extractTokenClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractTokenClaims(token).getExpiration();
    }

    public String extractType(String token) {
        return extractTokenClaims(token).get("type", String.class);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
