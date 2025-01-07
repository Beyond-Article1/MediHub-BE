package mediHub_be.chatbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.entity.ChatbotMessage;
import mediHub_be.chatbot.entity.ChatbotSession;
import mediHub_be.chatbot.service.ChatbotRestService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "챗봇 기능 API", description = "챗봇 관련 RESTFUL한 API")
public class ChatbotRestController {
    private final ChatbotRestService chatbotRestService;

    @Operation(summary = "챗봇 질문", description = "로그인 한 회원이 chatbot에 물어봄")
    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String answer = chatbotRestService.answerQuestion(question);

        if (answer == null || answer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("답변을 생성하지 못했습니다.");
        }
        return ResponseEntity.ok(answer);
    }

    @Operation(summary = "챗봇 세션 생성", description = "로그인 한 회원과의 새로운 AI 채팅 세션 생성")
    @PostMapping("/sessions")
    public ResponseEntity<ApiResponse<String>> createChatSession(
            @RequestParam String title) {
        String userId = SecurityUtil.getCurrentUserId();
        log.info("새로운 채팅 세션 생성 요청 - 사용자: {}, 제목: {}", userId, title);
        String sessionId = chatbotRestService.createNewChatSession(userId, title);
        return ResponseEntity.ok(ApiResponse.ok(sessionId));
    }

    @Operation(summary = "챗봇 세션 메세지 저장", description = "특정 채팅 세션의 메시지 저장")
    @PostMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<ApiResponse<Void>> saveMessage(
            @PathVariable String sessionId,
            @RequestParam String sender,
            @RequestParam String content) {

        String userId = SecurityUtil.getCurrentUserId();
        log.info("메시지 저장 요청 - 사용자: {}, 세션: {}, 발신자: {}, 내용: {}", userId, sessionId, sender, content);
        chatbotRestService.saveMessage(userId, sessionId, sender, content);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "채팅 세션 기록 조회", description = "특정 채팅 세션의 메세지 기록 불러오기")
    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<ApiResponse<List<ChatbotMessage>>> getSessionMessages(
            @PathVariable String sessionId) {
        log.info("채팅 세션 메시지 조회 요청 - 세션: {}", sessionId);
        List<ChatbotMessage> messages = chatbotRestService.getSessionMessages(sessionId);
        return ResponseEntity.ok(ApiResponse.ok(messages));
    }

    @Operation(summary = "챗봇 세션 전체 조회", description = "특정 유저가 생성한 모든 채팅 세션 불러오기")
    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<ChatbotSession>>> getUserSessions() {
        String userId = SecurityUtil.getCurrentUserId();
        log.info("사용자 채팅 세션 목록 조회 요청 - 사용자: {}", userId);
        List<ChatbotSession> sessions = chatbotRestService.getUserSessions(userId);
        return ResponseEntity.ok(ApiResponse.ok(sessions));
    }

    @Operation(summary = "챗봇 세션 삭제", description = "특정 유저의 채팅 세션 삭제")
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> deleteSession(
            @PathVariable String sessionId) {
        String userId = SecurityUtil.getCurrentUserId();
        log.info("채팅 세션 삭제 요청 - 사용자: {}, 세션: {}", userId, sessionId);
        chatbotRestService.deleteSession(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "챗봇 세션 이름 수정", description = "특정 채팅 세션의 이름 수정")
    @PatchMapping("/sessions/{sessionId}/title")
    public ResponseEntity<ApiResponse<Void>> updateSessionTitle(
            @PathVariable String sessionId,
            @RequestParam String newTitle) {
        String userId = SecurityUtil.getCurrentUserId();
        log.info("채팅 세션 제목 수정 요청 - 사용자: {}, 세션: {}, 새 제목: {}", userId, sessionId, newTitle);
        chatbotRestService.updateSessionTitle(userId, sessionId, newTitle);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

}
