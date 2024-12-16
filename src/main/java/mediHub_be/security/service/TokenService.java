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
        try {
            long currentTime = System.currentTimeMillis();
            long ttl = expirationTime - currentTime;

            if (ttl > 0) {
                redisTemplate.opsForValue().set("blacklist:" + token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
                log.info("블랙리스트에 토큰 추가: {}", token);
            } else {
                log.warn("이미 만료된 토큰입니다. 블랙리스트에 추가하지 않습니다: {}", token);
            }
        } catch (Exception e) {
            log.error("블랙리스트에 토큰 추가 실패: {}", token, e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isTokenBlacklisted(String token) {
        try {
            boolean isBlacklisted = Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
            log.info("블랙리스트 토큰 조회: {} -> {}", token, isBlacklisted);
            return isBlacklisted;
        } catch (Exception e) {
            log.error("블랙리스트 상태 확인 실패: {}", token, e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }

    // 리프레시 토큰 저장
    public void saveRefreshToken(String userId, String refreshToken, long expirationTime) {
        try {
            if (userId == null || refreshToken == null) {
                throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
            }

            redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
            log.info("리프레시 토큰 저장 완료. userId: {}", userId);
        } catch (Exception e) {
            log.error("리프레시 토큰 저장 실패. userId: {}", userId, e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }

    // 리프레시 토큰 조회
    public String getRefreshToken(String userId) {
        try {
            if (userId == null) {
                throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
            }

            String refreshToken = redisTemplate.opsForValue().get("refresh:" + userId);
            if (refreshToken == null) {
                log.warn("리프레시 토큰을 찾을 수 없습니다. userId: {}", userId);
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }

            log.info("리프레시 토큰 조회 완료. userId: {}", userId);
            return refreshToken;
        } catch (CustomException e) {
            throw e; // 명시적으로 발생시킨 예외는 그대로 던짐
        } catch (Exception e) {
            log.error("리프레시 토큰 조회 실패. userId: {}", userId, e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }

    // 리프레시 토큰 삭제
    public void deleteRefreshToken(String userId) {
        try {
            if (userId == null) {
                throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
            }

            if (!Boolean.TRUE.equals(redisTemplate.hasKey("refresh:" + userId))) {
                log.warn("리프레시 토큰이 존재하지 않아 삭제하지 않습니다. userId: {}", userId);
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }

            redisTemplate.delete("refresh:" + userId);
            log.info("리프레시 토큰 삭제 완료. userId: {}", userId);
        } catch (CustomException e) {
            throw e; // 명시적으로 발생시킨 예외는 그대로 던짐
        } catch (Exception e) {
            log.error("리프레시 토큰 삭제 실패. userId: {}", userId, e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }
}
