package futurenet.fullstack.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import futurenet.fullstack.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "this-is-a-very-long-secret-key-for-jwt-auth-service-123456";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1??

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    //??? ?? ?? JWT ??
    public String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(user.getEmail())           //jwt? ??
                .claim("userId", user.getUserId())  //??? ?? ??
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(now)                      //?? ?????
                .expiration(expiration)             //???
                .signWith(getSigningKey())          //???? ????
                .compact();
    }

    //JWT ??? Payload ??
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //JWT ? subject(email ???)
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    //?? ??? ??
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}