package mediHub_be.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.Optional;

@Slf4j
@Component
public class SecurityUtil {

    // 현재 인증된 사용자의 UserDetails 반환
    public static Optional<UserDetails> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return Optional.of((UserDetails) authentication.getPrincipal());
        }

        return Optional.empty();
    }

    // 현재 인증된 사용자의 권한 반환
    public static String getCurrentUserAuthorities() {
        return getCurrentUserDetails()
                .map(userDetails -> userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority) // 권한을 String으로 변환
                        .collect(Collectors.joining(",")))  // 권한들을 ','로 구분하여 반환
                .orElse("");  // 권한이 없을 경우, 빈 문자열 반환
    }


    // 현재 인증된 사용자의 userSeq 반환
    public static Long getCurrentUserSeq() {
        return getCurrentUserDetails()
                .map(userDetails -> {
                    try {
                        // userSeq를 Long으로 반환
                        return Long.parseLong(userDetails.getUsername());
                    } catch (NumberFormatException e) {
                        log.error("Invalid userSeq format: {}", userDetails.getUsername(), e);

                        return null; // 숫자 형식이 아닌 경우, null 반환
                    }
                })
                .orElse(null); // 인증된 사용자가 없을 경우, null 반환
    }

}
