package mediHub_be.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateChatroomDTO {
    @NotBlank(message = "채팅방 이름을 입력하지 않았습니다.")
    private String chatroomCustomName;
}
