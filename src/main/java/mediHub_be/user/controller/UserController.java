package mediHub_be.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.security.dto.LoginRequest;
import mediHub_be.security.util.JwtUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User authenticatedUser = userService.authenticateUser(loginRequest.getUserId(), loginRequest.getUserPassword());
            String token = jwtUtil.createToken(authenticatedUser.getUserId(),
                    authenticatedUser.getUserAuth().name());

            return ResponseEntity.ok().body(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("인증 실패: " + e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body("인증 실패: 사용자 ID를 찾을 수 없습니다.");
        }
    }

}