package mediHub_be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.dto.UserResponseDTO;
import mediHub_be.user.dto.UserSearchDTO;
import mediHub_be.user.dto.UserUpdateRequestDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "회원", description = "회원정보 API")
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

//    @Operation(summary = "내 정보 수정", description = "로그인된 사용자가 자신의 정보를 수정합니다.")
//    @PutMapping(value = "/userInfo", consumes = {"multipart/form-data"})
//    public ResponseEntity<ApiResponse<Long>> updateMyInfo(
//            @RequestPart(value = "data", required = true) UserUpdateRequestDTO userUpdateRequestDTO,
//            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
//    ) throws IOException {
//
//        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();
//
//        // 사용자 정보 수정 서비스 호출
//        User updatedUser = userService.updateUser(currentUserSeq, userUpdateRequestDTO, profileImage);
//
//        // 응답으로 수정된 사용자 ID 반환
//        return ResponseEntity.ok(ApiResponse.ok(updatedUser.getUserSeq()));
//    }


    @Operation(summary = "내 정보 수정", description = "로그인된 사용자가 자신의 정보를 수정합니다.")
    @PutMapping(value = "/userInfo", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Long>> updateMyInfo(
            @RequestParam(value = "userEmail", required = false) String userEmail,
            @RequestParam(value = "userPhone", required = false) String userPhone,
            @RequestParam(value = "userPassword", required = false) String userPassword,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {

        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        // 사용자 정보 수정 서비스 호출
        User updatedUser = userService.updateUser(currentUserSeq, userEmail, userPhone, userPassword, profileImage);

        // 응답으로 수정된 사용자 ID 반환
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

