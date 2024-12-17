package mediHub_be.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final StringRedisTemplate redisTemplate;

    // 블랙리스트에 토큰 추가
    public void addToBlacklist(String token, long expirationTime) {
        log.info("블랙리스트 추가 요청: token={}, expirationTime={}", token, expirationTime);

        try {
            long currentTime = System.currentTimeMillis();
            long ttl = expirationTime - currentTime;

            if (ttl > 0) {
                redisTemplate.opsForValue().set("blacklist:" + token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
                log.info("블랙리스트에 토큰 추가 완료: token={}, TTL={}ms", token, ttl);
            } else {
                log.warn("블랙리스트 추가 실패 - 이미 만료된 토큰: token={}", token);
            }
        } catch (Exception e) {
            log.error("블랙리스트에 토큰 추가 실패: token={}, error={}", token, e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isTokenBlacklisted(String token) {
        log.info("블랙리스트 상태 확인 요청: token={}", token);

        try {
            boolean isBlacklisted = Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
            log.info("블랙리스트 상태 확인 완료: token={}, isBlacklisted={}", token, isBlacklisted);
            return isBlacklisted;
        } catch (Exception e) {
            log.error("블랙리스트 상태 확인 실패: token={}, error={}", token, e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }

    // 리프레시 토큰 저장
    public void saveRefreshToken(String userId, String refreshToken, long expirationTime) {
        log.info("리프레시 토큰 저장 요청: userId={}, refreshToken={}, expirationTime={}ms", userId, refreshToken, expirationTime);

        try {
            if (userId == null || refreshToken == null) {
                log.error("리프레시 토큰 저장 실패 - 필수 필드 누락: userId={}, refreshToken={}", userId, refreshToken);
                throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
            }

            redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
            log.info("리프레시 토큰 저장 성공: userId={}, expirationTime={}ms", userId, expirationTime);
        } catch (Exception e) {
            log.error("리프레시 토큰 저장 실패: userId={}, error={}", userId, e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }

    // 리프레시 토큰 조회
    public String getRefreshToken(String userId) {
        log.info("리프레시 토큰 조회 요청: userId={}", userId);

        try {
            if (userId == null) {
                log.error("리프레시 토큰 조회 실패 - 필수 필드 누락: userId={}", userId);
                throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
            }

            String refreshToken = redisTemplate.opsForValue().get("refresh:" + userId);
            if (refreshToken == null) {
                log.warn("리프레시 토큰 조회 실패 - 존재하지 않음: userId={}", userId);
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }

            log.info("리프레시 토큰 조회 성공: userId={}, refreshToken={}", userId, refreshToken);
            return refreshToken;
        } catch (CustomException e) {
            log.error("리프레시 토큰 조회 중 사용자 정의 예외 발생: userId={}, error={}", userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("리프레시 토큰 조회 실패: userId={}, error={}", userId, e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }

    // 리프레시 토큰 삭제
    public void deleteRefreshToken(String userId) {
        log.info("리프레시 토큰 삭제 요청: userId={}", userId);

        try {
            if (userId == null) {
                log.error("리프레시 토큰 삭제 실패 - 필수 필드 누락: userId={}", userId);
                throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
            }

            if (!Boolean.TRUE.equals(redisTemplate.hasKey("refresh:" + userId))) {
                log.warn("리프레시 토큰 삭제 요청 실패 - 토큰이 존재하지 않음: userId={}", userId);
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }

            redisTemplate.delete("refresh:" + userId);
            log.info("리프레시 토큰 삭제 성공: userId={}", userId);
        } catch (CustomException e) {
            log.error("리프레시 토큰 삭제 중 사용자 정의 예외 발생: userId={}, error={}", userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("리프레시 토큰 삭제 실패: userId={}, error={}", userId, e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }
}
