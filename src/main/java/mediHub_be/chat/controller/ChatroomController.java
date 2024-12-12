package mediHub_be.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chat.dto.ChatroomDTO;
import mediHub_be.chat.dto.ResponseChatUserDTO;
import mediHub_be.chat.dto.ResponseChatroomDTO;
import mediHub_be.chat.dto.UpdateChatroomDTO;
import mediHub_be.chat.service.ChatroomService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
@Tag(name = "채팅방", description = "채팅방 API")
public class ChatroomController {

    private final ChatroomService chatroomService;

    @Operation(summary = "채팅방 생성", description = "새로운 채팅방 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createChatroom(@RequestBody ChatroomDTO chatroomDTO) {
        Long chatroomSeq = chatroomService.createChatroom(chatroomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(chatroomSeq));
    }

    @Operation(summary = "대화상대 초대", description = "기존에 있던 채팅방에 대화상대 초대(추가)")
    @PostMapping("/{chatroomSeq}")
    public ResponseEntity<ApiResponse<Long>> updateChatroomMember(@PathVariable Long chatroomSeq, @RequestBody ChatroomDTO chatroomDTO) {
        chatroomService.updateChatroomMember(chatroomSeq, chatroomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(chatroomSeq));
    }

    @Operation(summary = "채팅방 이름 수정", description = "채팅방 이름 수정")
    @PutMapping("/{chatroomSeq}")
    public ResponseEntity<ApiResponse<Void>> updateChatroomName(@PathVariable Long chatroomSeq, @RequestBody @Valid UpdateChatroomDTO updatechatroomDTO) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        chatroomService.updateChatroomName(userSeq, chatroomSeq, updatechatroomDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(null));
    }

    @Operation(summary = "채팅방 나가기", description = "채팅방 나가기")
    @DeleteMapping("/{chatroomSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteChatroom(@PathVariable Long chatroomSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        chatroomService.deleteChatroom(userSeq, chatroomSeq);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(null));
    }

    @Operation(summary = "채팅방 목록 조회", description = "채팅방 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ResponseChatroomDTO>>> getChatroomListByUserSeq() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<ResponseChatroomDTO> chatrooms = chatroomService.getChatroomListByUserSeq(userSeq);
        return ResponseEntity.ok(ApiResponse.ok(chatrooms));
    }

    @Operation(summary = "채팅방 정보 조회", description = "채팅방 정보 조회")
    @GetMapping("/{chatroomSeq}")
    public ResponseEntity<ApiResponse<ResponseChatroomDTO>> getChatroomInfoBySeq(@PathVariable Long chatroomSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        ResponseChatroomDTO chatroom = chatroomService.getChatroomInfoBySeq(userSeq, chatroomSeq);
        return ResponseEntity.ok(ApiResponse.ok(chatroom));
    }

    @Operation(summary = "채팅방 참여자 조회", description = "특정 채팅방 참여자 조회")
    @GetMapping("/{chatroomSeq}/user")
    public ResponseEntity<ApiResponse<List<ResponseChatUserDTO>>> getChatUsers(@PathVariable Long chatroomSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<ResponseChatUserDTO> users = chatroomService.getChatUsers(userSeq, chatroomSeq);
        return ResponseEntity.ok(ApiResponse.ok(users));
    }

}
