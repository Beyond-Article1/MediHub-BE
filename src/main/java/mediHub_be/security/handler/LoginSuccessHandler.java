package mediHub_be.security.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.security.securitycustom.CustomUserDetails;
import mediHub_be.security.service.TokenService;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final Environment env;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 - Security 관리 객체 정보: {}", authentication);

        try {
            /* 권한 리스트 생성 */
            List<String> authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            log.info("권한 리스트: {}", authorities);

            // 사용자 정보 가져오기
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userSeq = customUserDetails.getUserSeq();
            String username = customUserDetails.getUserId();

            log.info("userSeq : {}", userSeq);
            log.info("userName : {}", username);

            /* Access Token Claims 생성 */
            Claims accessTokenClaims = Jwts.claims().setSubject(authentication.getName());
            accessTokenClaims.put("userSeq", userSeq);
            accessTokenClaims.put("userName", username);
            accessTokenClaims.put("auth", authorities);

            /* Access Token 생성 */
            String accessTokenExpirationStr = env.getProperty("token.expiration_time", "3600000"); // 기본값: 1시간
            long accessTokenExpiration = Long.parseLong(accessTokenExpirationStr);
            String accessToken = Jwts.builder()
                    .setClaims(accessTokenClaims)
                    .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                    .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                    .compact();
            log.info("Access Token 생성 성공: {}", accessToken);

            /* Refresh Token 생성 */
            String refreshTokenExpirationStr = env.getProperty("refresh.token.expiration_time", "604800000"); // 기본값: 7일
            long refreshTokenExpiration = Long.parseLong(refreshTokenExpirationStr);
            String refreshToken = Jwts.builder()
                    .setSubject(authentication.getName())
                    .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                    .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                    .compact();
            log.info("Refresh Token 생성 성공: {}", refreshToken);

            /* Refresh Token 저장 */
            tokenService.saveRefreshToken(username, refreshToken, refreshTokenExpiration);
            log.info("Refresh Token 저장 완료 (userId: {}, expiration: {}ms)", username, refreshTokenExpiration);

            /* 응답 헤더에 토큰 추가 */
            response.setHeader("Authorization", "Bearer " + accessToken);
            response.setHeader("Refresh-Token", refreshToken);

            /* 응답 본문에 userSeq와 refreshToken 추가 */
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(String.format(
                    "{\"userSeq\": %d, \"refreshToken\": \"%s\"}",
                    userSeq, refreshToken
            ));

            log.info("토큰이 응답 헤더에 추가되었습니다.");
        } catch (NumberFormatException e) {
            log.error("토큰 만료 시간 파싱 실패 - 환경변수 설정 확인 필요: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "토큰 발급 실패: 잘못된 만료 시간 설정");
        } catch (Exception e) {
            log.error("토큰 생성 또는 저장 중 예외 발생: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "토큰 발급 실패");
        }
    }
}
