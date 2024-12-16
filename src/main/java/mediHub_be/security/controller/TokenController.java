package mediHub_be.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.service.TokenService;
import mediHub_be.security.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "토큰", description = "JWT 토큰 관련 API")
@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Operation(summary = "로그아웃", description = "사용자의 액세스 토큰을 블랙리스트에 등록하고 리프레시 토큰을 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        // Authorization 헤더 유효성 확인
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String accessToken = token.substring(7);

        // 토큰 유효성 검사
        if (!jwtUtil.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        long expirationTime = jwtUtil.getExpiration(accessToken);
        if (System.currentTimeMillis() >= expirationTime) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰 블랙리스트 등록 및 리프레시 토큰 삭제
        tokenService.addToBlacklist(accessToken, expirationTime);
        tokenService.deleteRefreshToken(jwtUtil.getUserId(accessToken));

        return ResponseEntity.ok(ApiResponse.ok("로그아웃 성공"));
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<String>> reissueToken(
            @RequestHeader("Refresh-Token") String refreshToken
    ) {
        // 리프레시 토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String userId = jwtUtil.getUserId(refreshToken);
        String storedRefreshToken = tokenService.getRefreshToken(userId);

        // 리프레시 토큰 일치 여부 확인
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 새로운 토큰 생성
        String newAccessToken = jwtUtil.generateAccessToken(userId, 3600000);   // 1시간
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, 604800000);   // 7일

        tokenService.saveRefreshToken(userId, newRefreshToken, 604800000);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + newAccessToken);
        headers.add("Refresh-Token", newRefreshToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.ok("토큰 재발급 성공"));
    }
}
