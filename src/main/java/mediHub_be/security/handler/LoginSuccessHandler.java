package mediHub_be.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.security.securitycustom.CustomUserDetails;
import mediHub_be.security.service.TokenService;
import mediHub_be.security.util.JwtUtil;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final Environment env;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 - Security 관리 객체 정보: {}", authentication);

        try {
            // 사용자 정보 추출
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userSeq = customUserDetails.getUserSeq();
            String userId = customUserDetails.getUserId();
            String auth = String.join(",", authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .toList());

            log.info("userSeq: {}", userSeq);
            log.info("userId: {}", userId);
            log.info("권한: {}", auth);

            // 만료 시간 가져오기
            long accessTokenExpiration = Long.parseLong(env.getProperty("token.expiration_time", "3600000")); // 1시간
            long refreshTokenExpiration = Long.parseLong(env.getProperty("refresh.token.expiration_time", "604800000")); // 7일

            // 토큰 생성
            String accessToken = jwtUtil.generateAccessToken(userId, userSeq, auth, accessTokenExpiration);
            String refreshToken = jwtUtil.generateRefreshToken(userId, refreshTokenExpiration);

            // Refresh Token 저장
            tokenService.saveRefreshToken(userId, refreshToken, refreshTokenExpiration);
            log.info("Refresh Token 저장 완료 (userId: {}, expiration: {}ms)", userId, refreshTokenExpiration);

            // 응답 처리
            response.setHeader("Access-Token", accessToken);
            response.setHeader("Refresh-Token", refreshToken);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(String.format("{\"userSeq\": %d, \"refreshToken\": \"%s\"}", userSeq, refreshToken));

            log.info("토큰이 응답 헤더에 추가되었습니다.");
        } catch (Exception e) {
            log.error("토큰 생성 또는 저장 중 예외 발생: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "토큰 발급 실패");
        }
    }
}
