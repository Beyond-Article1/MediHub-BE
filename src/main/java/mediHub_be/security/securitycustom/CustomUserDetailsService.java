package mediHub_be.security.securitycustom;

import lombok.RequiredArgsConstructor;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // userId를 기반으로 사용자 조회
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

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
}