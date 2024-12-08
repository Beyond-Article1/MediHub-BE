package mediHub_be.security.util;

import lombok.extern.slf4j.Slf4j;
import mediHub_be.security.securitycustom.CustomUserDetails;
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
                        .collect(Collectors.joining(",")))
                .orElse("");
    }


    // 현재 인증된 사용자의 userSeq 반환
    public static Long getCurrentUserSeq() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("getCurrentUserSeq method 확인 Authentication: {}", authentication);

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUserSeq();
        }

        return null; // 인증되지 않은 경우 null 반환
    }

    // 현재 인증된 사용자의 userId 반환
    public static String getCurrentUserId() {
        return getCurrentUserDetails()
                .map(UserDetails::getUsername)
                .orElse(null); // 인증된 사용자가 없을 경우, null 반환
    }

}
