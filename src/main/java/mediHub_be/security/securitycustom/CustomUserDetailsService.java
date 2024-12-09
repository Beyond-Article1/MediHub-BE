package mediHub_be.security.securitycustom;

import lombok.RequiredArgsConstructor;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        // userId를 기반으로 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userId));


        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        // GrantedAuthority 생성 (USER 권한)
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getUserAuth().name());

        // CustomUserDetails 생성 및 반환
        return new CustomUserDetails(
                user.getUserSeq(),
                user.getUserId(),
                user.getUserName(),
                user.getUserPassword(),
                Collections.singletonList(authority)
        );
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    public User authenticateUser(String userId, String password) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId);
                });

        if (!validatePassword(password, user.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        return user;
    }
}