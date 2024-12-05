package mediHub_be.notify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.notify.service.NotifyService;
import mediHub_be.notify.service.NotifyServiceImlp;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "SSE 연결", description = "SSE 연결")
public class NotifyController {

    private final NotifyServiceImlp notifyService;

    @Operation(summary = "sse 세션연결")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
//            @AuthenticationPrincipal UserDetails principal,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){

        String userId = SecurityUtil.getCurrentUserId();
        log.info(userId);

        return notifyService.subscribe(userId, lastEventId);
    }

//    @Operation(summary = "sse 연결된 사람한테 테스트 알림 전송")
//    @GetMapping("/send/connects")
//    public ResponseEntity<ApiResponse<?>> sendConnects(@AuthenticationPrincipal User principal){
//
//        notifyService.send(NotiType.BOARD, "알림 발생", "/test/ui");
//
//        return ResponseEntity.ok(
//                ApiResponse.ok("ok")
//        );
//    }
}
