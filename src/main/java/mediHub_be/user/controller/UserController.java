package mediHub_be.user.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.security.util.JwtUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User authenticatedUser = userService.authenticateUser(loginRequest.getUserId(), loginRequest.getUserPassword());
        String token = jwtUtil.createToken(authenticatedUser.getUserId(), authenticatedUser.getUserAuth().name());

        return ResponseEntity.ok().body(token);
    }
}
