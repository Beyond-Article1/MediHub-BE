package mediHub_be.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminUpdateDTO;
import mediHub_be.admin.dto.UserCreateDTO;
import mediHub_be.admin.service.AdminUserService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.dto.UserUpdateDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(
            summary = "회원 등록",
            description = "회원의 정보를 입력 받아 새 회원을 등록"
    )
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

    @PatchMapping("/{userSeq}/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable Long userSeq) {
        adminUserService.initializePassword(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

}

