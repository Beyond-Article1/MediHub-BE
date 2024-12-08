package mediHub_be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.user.dto.UserDTO;
import mediHub_be.user.dto.UserUpdateDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import mediHub_be.user.service.UserService;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관리 API")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "전체 회원 조회", description = "모든 회원 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(users));
    }

    @Operation(summary = "특정 회원 조회", description = "특정 회원의 상세 정보를 조회합니다.")
    @GetMapping("/{userSeq}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long userSeq) {
        UserDTO user = userService.getUserById(userSeq);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(user));
    }

    @Operation(summary = "자기 정보 수정", description = "회원이 자신의 정보를 수정합니다.")
    @PutMapping("")
    public ResponseEntity<ApiResponse<Void>> updateUser(@RequestBody UserUpdateDTO updateDTO) {
        String userId = SecurityUtil.getCurrentUserId();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        userService.updateUser(user.getUserSeq(), updateDTO);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(null));
    }
}
