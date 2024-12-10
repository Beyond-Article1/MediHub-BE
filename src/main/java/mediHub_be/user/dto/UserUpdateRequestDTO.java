package mediHub_be.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequestDTO {
    private String userEmail;
    private String userPhone;
    private String userPassword;

    @NotNull(message = "Profile image is required.")
    private MultipartFile profileImage;
}
