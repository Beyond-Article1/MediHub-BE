package mediHub_be.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminCreateDTO;
import mediHub_be.admin.service.AdminService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "관리자", description = "관리자 회원 관리 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "회원 등록", description = "관리자가 새로운 회원을 등록합니다.")
    @PostMapping("/register-user")
    public ResponseEntity<ApiResponse<Long>> registerUser(@RequestBody AdminCreateDTO adminCreateDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        Long newUserSeq = adminService.registerUser(adminCreateDTO).getUserSeq();
        return ResponseEntity.ok(ApiResponse.created(newUserSeq));
    }
}
