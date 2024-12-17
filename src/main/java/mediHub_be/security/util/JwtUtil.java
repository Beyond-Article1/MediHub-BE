package mediHub_be.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.security.securitycustom.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtUtil(
            @Value("${token.secret}") String secretKey,
            CustomUserDetailsService customUserDetailsService) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("토큰 에러.");
        }
        this.customUserDetailsService = customUserDetailsService;
    }

    // Access Token 생성
    public String generateAccessToken(String userId, long expirationTime) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(String userId, long expirationTime) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /* 토큰 만료 시간 추출 */
    public long getExpiration(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getExpiration().getTime();
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰에서 만료 시간을 가져옵니다.");
            return e.getClaims().getExpiration().getTime();
        } catch (Exception e) {
            log.error("토큰 만료 시간 확인 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("토큰 만료 시간 확인 실패");
        }
    }

    /* Token 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("Token is valid: {}", token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT Token claims empty {}", e);
        }
        return false;
    }

    /* Authentication 객체 추출 */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String userId = claims.getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /* Claims 추출 */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /* Token에서 사용자 ID 추출 */
    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }
}
