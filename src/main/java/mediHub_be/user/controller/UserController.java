package mediHub_be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminResponseDTO;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.dto.UserResponseDTO;
import mediHub_be.user.dto.UserSearchDTO;
import mediHub_be.user.dto.UserUpdateRequestDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "로그인된 사용자의 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserInfo() {
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();
        UserResponseDTO userInfo = userService.getUserInfo(currentUserSeq);
        return ResponseEntity.ok(ApiResponse.ok(userInfo));
    }

    @Operation(summary = "내 정보 수정", description = "로그인된 사용자가 자신의 정보를 수정합니다.")
    @PutMapping(value = "/userInfo", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Long>> updateMyInfo(
            @ModelAttribute @Valid UserUpdateRequestDTO requestDTO
    ) throws IOException {
        // 현재 로그인된 사용자의 고유 ID를 가져옵니다.
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        // 사용자 정보 수정
        User updatedUser = userService.updateUser(currentUserSeq, requestDTO);

        // 응답으로 수정된 사용자 고유 ID 반환
        return ResponseEntity.ok(ApiResponse.ok(updatedUser.getUserSeq()));
    }

    // 회원 전체 조회
    @Operation(summary = "전체 회원 조회", description = "모든 회원 정보를 조회합니다.")
    @GetMapping(value = "/allUser")
    public ResponseEntity<ApiResponse<List<UserSearchDTO>>> getAllUsers() {
        // 서비스 호출하여 전체 회원 조회
        List<UserSearchDTO> allUsers = userService.getAllUsers();

        // 성공 응답 반환
        return ResponseEntity.ok(ApiResponse.ok(allUsers));
    }
}

