package mediHub_be.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminResponseDTO;
import mediHub_be.admin.dto.AdminUpdateDTO;
import mediHub_be.admin.dto.AdminUserDetailResponseDTO;
import mediHub_be.admin.dto.UserCreateDTO;
import mediHub_be.admin.service.AdminUserService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "관리자 회원 관리", description = "관리자 회원관리 API")
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "회원 등록", description = "회원의 정보를 입력 받아 새 회원을 등록")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Long>> registerUser(
            @RequestParam("partSeq") Long partSeq,
            @RequestParam("rankingSeq") Long rankingSeq,
            @RequestParam("userName") String userName,
            @RequestParam("userId") String userId,
            @RequestParam("userPassword") String userPassword,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userPhone") String userPhone,
            @RequestParam(value = "userAuth", required = false) UserAuth userAuth,
            @RequestParam(value = "userStatus", required = false) UserStatus userStatus,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {

        // 현재 사용자 ID 가져오기
        String currentUserId = SecurityUtil.getCurrentUserId();

        // DTO 생성 및 데이터 설정
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setPartSeq(partSeq);
        userCreateDTO.setRankingSeq(rankingSeq);
        userCreateDTO.setUserName(userName);
        userCreateDTO.setUserId(userId);
        userCreateDTO.setUserPassword(userPassword);
        userCreateDTO.setUserEmail(userEmail);
        userCreateDTO.setUserPhone(userPhone);
        userCreateDTO.setUserAuth(userAuth != null ? userAuth : UserAuth.USER);
        userCreateDTO.setUserStatus(userStatus != null ? userStatus : UserStatus.ACTIVE);

        // 서비스 호출
        User createdUser = adminUserService.registerUser(userCreateDTO, profileImage, currentUserId);

        // 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdUser.getUserSeq()));
    }

    // 비밀번호 초기화
    @Operation(summary = "비밀번호 초기화", description = "비밀번호를 초기화 합니다.")
    @PatchMapping("/{userSeq}/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable Long userSeq) {
        adminUserService.initializePassword(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // 회원 정보 수정
    @Operation(summary = "회원 수정", description = "회원 정보를 수정합니다.")
    @PutMapping(value = "/{userSeq}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Long>> updateUser(
            @PathVariable Long userSeq,
            @RequestParam("partSeq") Long partSeq,
            @RequestParam("rankingSeq") Long rankingSeq,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userPhone") String userPhone,
            @RequestParam(value = "userAuth", required = false) UserAuth userAuth,
            @RequestParam(value = "userStatus", required = false) UserStatus userStatus,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {
        AdminUpdateDTO adminUpdateDTO = new AdminUpdateDTO();
        adminUpdateDTO.setPartSeq(partSeq);
        adminUpdateDTO.setRankingSeq(rankingSeq);
        adminUpdateDTO.setUserEmail(userEmail);
        adminUpdateDTO.setUserPhone(userPhone);
        adminUpdateDTO.setUserAuth(userAuth);
        adminUpdateDTO.setUserStatus(userStatus);

        User updatedUser = adminUserService.updateUser(userSeq, adminUpdateDTO, profileImage);

        return ResponseEntity.ok(ApiResponse.ok(updatedUser.getUserSeq()));
    }

    // 회원 삭제
    @Operation(summary = "회원 삭제", description = "회원의 상태를 DELETE로 변경하고 삭제 시간을 기록합니다.")
    @DeleteMapping("/{userSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userSeq) {
        adminUserService.deleteUser(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // 회원 전체 조회
    @Operation(summary = "회원 전체 조회", description = "전체 회원 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminResponseDTO>>> getAllUsers() {
        List<AdminResponseDTO> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    // 특정 회원 조회
    @Operation(summary = "특정 유저 조회", description = "특정 userSeq로 유저 정보를 조회합니다.")
    @GetMapping("/{userSeq}")
    public ResponseEntity<ApiResponse<AdminUserDetailResponseDTO>> getUserBySeq(@PathVariable Long userSeq) {
        AdminUserDetailResponseDTO userDetail = adminUserService.getUserBySeq(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(userDetail));
    }

}

