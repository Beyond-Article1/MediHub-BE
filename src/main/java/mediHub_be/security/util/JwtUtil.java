package mediHub_be.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
    private final UserService userService;

    public JwtUtil(
            @Value("${token.secret}") String secretKey,
            UserService userService
    ) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("토큰 에러.");
        }
        this.userService = userService;
    }

    public String createToken(String userId, String role) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /* Token 검증(Bearer 토큰이 넘어왔고, 우리 사이트의 secret key로 만들어 졌는가, 만료되었는지와 내용이 비어있진 않은지) */
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
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

    /* 넘어온 AccessToken으로 인증 객체 추출 */
    public Authentication getAuthentication(String token) {

        /* 토큰을 들고 왔던 들고 오지 않았던(로그인 시) 동일하게 security가 관리 할 UserDetails 타입을 정의 */
        UserDetails userDetails = userService.loadUserByUsername(this.getUserId(token));


        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /* Token에서 Claims 추출 */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /* Token에서 사용자의 id(subject 클레임) 추출 */
    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }


}