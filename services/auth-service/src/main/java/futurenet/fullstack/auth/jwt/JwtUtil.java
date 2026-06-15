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
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    //사용자 정보 받아 JWT 생성
    public String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(user.getEmail())           //jwt의 주인
                .claim("userId", user.getUserId())  //사용자 정의 정보
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(now)                      //언제 발급했는지
                .expiration(expiration)             //만료일
                .signWith(getSigningKey())          //시그니처 생성부분
                .compact();
    }

    //JWT 내부의 Payload 추출
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //JWT 내 subject(email 꺼내기)
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    //토근 유효성 검사
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}