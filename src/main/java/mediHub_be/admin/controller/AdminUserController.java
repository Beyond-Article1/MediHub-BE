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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<Long>> registerUser(
            @RequestPart UserCreateDTO userCreateDTO,
            @RequestPart(required = false) MultipartFile profileImage) throws IOException {
        User createdUser = adminUserService.registerUser(userCreateDTO, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdUser.getUserSeq()));
    }

    @PatchMapping("/{userSeq}/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable Long userSeq) {
        adminUserService.initializePassword(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

}

