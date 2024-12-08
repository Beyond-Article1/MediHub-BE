package mediHub_be.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminDTO;
import mediHub_be.admin.service.AdminService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "관리자", description = "관리자 회원 관리 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "회원 등록", description = "관리자가 새로운 회원을 등록합니다.")
    @PostMapping("/register-user")
    public ResponseEntity<ApiResponse<Long>> registerUser(@RequestBody AdminDTO adminDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        Long newUserSeq = adminService.registerUser(adminDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(newUserSeq));
    }

    @Operation(summary = "회원 수정", description = "관리자가 기존 회원의 정보를 수정합니다.")
    @PostMapping("/update/{userSeq}")
    public ResponseEntity<ApiResponse<Long>> updateUser(@PathVariable Long userSeq, @RequestBody AdminDTO adminDTO) {
        Long updatedUserSeq = adminService.updateUser(userSeq, adminDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(updatedUserSeq));
    }

    @Operation(summary = "전체 회원 조회", description = "관리자가 모든 회원 정보를 조회합니다.")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<AdminDTO>>> getAllUsers() {
        List<AdminDTO> users = adminService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(users));
    }

    @Operation(summary = "특정 회원 조회", description = "관리자가 특정 회원의 상세 정보를 조회합니다.")
    @GetMapping("/user/{userSeq}")
    public ResponseEntity<ApiResponse<AdminDTO>> getUserById(@PathVariable Long userSeq) {
        AdminDTO user = adminService.getUserById(userSeq);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(user));
    }
}

