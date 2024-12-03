package mediHub_be.notify.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.notify.service.NotifyService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @Operation(summary = "sse 세션연결")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
//            @AuthenticationPrincipal User principal,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){

        SseEmitter subscribe = notifyService.subscribe(lastEventId);

        return subscribe;
    }
}
