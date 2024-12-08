package mediHub_be.chatbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.dto.ChatbotRequestDTO;
import mediHub_be.chatbot.service.ChatbotService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "챗봇 API", description = "챗봇 관련 API")
public class ChatbotController {
    private final ChatbotService chatbotService;

    @Operation(summary = "ChatGPT 사용자 요청에 대한 응답 반환", description = "사용자 입력 메시지를 ChatGPT API에 전달하고 결과를 반환합니다.")
    @PostMapping("/message")
    public ResponseEntity<ApiResponse<String>> getChatResponse(@RequestBody ChatbotRequestDTO chatRequest) {
        log.info("ChatGPT 요청 수신: {}", chatRequest);

        try {
            String response = chatbotService.getChatbotResponse(chatRequest.getMessages().get(0).getContent());
            log.info("ChatGPT 응답: {}", response);

            // 성공 응답 반환
            return ResponseEntity.ok(ApiResponse.ok(response));

        } catch (IllegalArgumentException e) {
            log.error("잘못된 요청: {}", e.getMessage());

            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(new CustomException(ErrorCode.INVALID_SELECT_SEQ))
            );
        } catch (Exception e) {
            log.error("ChatGPT 응답 처리 중 오류 발생: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR))
            );
        }

    }
}