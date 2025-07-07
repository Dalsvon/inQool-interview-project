package cz.svonavec.tennis.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * This service provides support for authentication with JWT tokens. It provides creation, validation and parsing of JWT
 * tokens.
 */
@Slf4j
@Service
public class JwtService {
    @Value("${application.jwt.secret}")
    private String secretKey;

    @Value("${application.jwt.expiration-time:1800000}")
    private long jwtExpiration;

    @Value("${application.jwt.refresh.expiration-time:60480000}")
    private long jwtRefreshExpiration;

    public long getJwtExpiration() {
        return jwtExpiration;
    }

    public long getJwtRefreshExpiration() {
        return jwtRefreshExpiration;
    }

    public String generateToken(UserDetails user) {
        String phone = user.getUsername();
        return generateTokenFromPhone(phone);
    }

    /**
     * Generates a new token for a user with given phone (phone is the unique username)
     *
     * @param phone unique phone number
     * @return new access token
     */
    public String generateTokenFromPhone(String phone) {
        return generateTokenFromPhone(phone, "access");
    }

    /**
     * Generates a new token for a user with given phone (phone is the unique username)
     *
     * @param phone unique phone number
     * @param type can be access or refresh token
     * @return new token
     */
    public String generateTokenFromPhone(String phone, String type) {
        String token = Jwts.builder()
                .setSubject(phone)
                .claim("type", type)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getJwtExpiration()))
                .signWith(getSigningKey())
                .compact();
        log.info("New token was generated for user with phone = {}", phone);
        return token;
    }

    public String generateRefreshToken(UserDetails user) {
        String phone = user.getUsername();
        return generateTokenFromPhone(phone, "refresh");
    }

    /**
     * Extracts claims from given JWT token
     *
     * @param token JWT token
     * @return claims
     */
    public Claims extractTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails user, String type) {
        if (user != null && user.getUsername() != null) {
            return validateTokenPhone(token, user.getUsername(), type);
        }
        return false;
    }

    /**
     * Validates token. If token has wrong structure, another secret, is expired, or is token of other user or type,
     * this method returns false
     *
     * @param token JWT token
     * @param phone unique phone number
     * @param type access or refresh type of token
     * @return true, if the token is valid, else false
     */
    public boolean validateTokenPhone(String token, String phone, String type) {
        try {
            if (type == null || type.isEmpty()) {
                type = "access";
            }

            return phone.equals(extractUsername(token)) &&
                    extractExpiration(token).after(new Date()) &&
                    type.equals(extractType(token));
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
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
