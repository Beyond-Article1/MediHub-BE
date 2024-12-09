package mediHub_be.admin.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminUpdateDTO;
import mediHub_be.admin.dto.UserCreateDTO;
import mediHub_be.admin.service.AdminUserService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.user.dto.UserUpdateDTO;
import mediHub_be.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerUser(@RequestBody UserCreateDTO userCreateDTO) {
        User createdUser = adminUserService.registerUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdUser.getUserSeq()));
    }

    @PutMapping("/{userSeq}")
    public ResponseEntity<ApiResponse<Long>> updateUser(
            @PathVariable Long userSeq,
            @RequestBody AdminUpdateDTO adminUpdateDTO
    ) {
        User updatedUser = adminUserService.updateUser(userSeq,adminUpdateDTO);
        return ResponseEntity.ok(ApiResponse.ok(updatedUser.getUserSeq()));
    }

    @PatchMapping("/{userSeq}/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable Long userSeq) {
        adminUserService.initializePassword(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{userSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userSeq) {
        adminUserService.deleteUser(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}

