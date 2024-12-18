package mediHub_be.notify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.notify.dto.NotifyDTO;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.notify.service.NotifyServiceImlp;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/notify")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "SSE 연결과 알림", description = "SSE 연결과 알림")
public class NotifyController {

    private final NotifyServiceImlp notifyService;

    private final FlagRepository flagRepository;
    private final UserRepository userRepository;

    @Operation(summary = "sse 세션연결", description = "SSE의 세션을 연결한다.")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){

        String userId = SecurityUtil.getCurrentUserId();
        log.debug(userId);

        return notifyService.subscribe(userId, lastEventId);
    }
    
    @Operation(summary = "알림 전체 조회", description = "내게 온 알림들을 조회한다. (삭제 상태 제외)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotifyDTO>>> notiAll(){
        String userId = SecurityUtil.getCurrentUserId();

        return ResponseEntity.ok(
                ApiResponse.ok(notifyService.notiAll(userId))
        );
    }

    @Operation(summary = "알림 단일 읽음", description = "알림을 읽음 처리 한다.")
    @GetMapping("/{notiSeq}")
    public ResponseEntity<ApiResponse<String>> readNoti(@RequestParam(name = "notiSeq") Long notiSeq){

        String userId = SecurityUtil.getCurrentUserId();
        notifyService.readNotify(userId, notiSeq);

        return ResponseEntity.ok(
                ApiResponse.ok("OK")
        );
    }

    @Operation(summary = "알림 단일 삭제", description = "알림을 삭제 처리 한다.")
    @DeleteMapping("/{notiSeq}")
    public ResponseEntity<ApiResponse<String>> deleteNoti(@RequestParam(name = "notiSeq") Long notiSeq){

        String userId = SecurityUtil.getCurrentUserId();
        notifyService.deleteNotify(userId, notiSeq);

        return ResponseEntity.ok(
                ApiResponse.ok("OK")
        );
    }

    @Operation(summary = "알림 전체 읽음", description = "나한테 온 알림들을 전체 읽음처리한다.")
    @GetMapping("/read")
    public ResponseEntity<ApiResponse<String>> readAllNoti(){

        String userId = SecurityUtil.getCurrentUserId();
        notifyService.readAllNotify(userId);

        return ResponseEntity.ok(
                ApiResponse.ok("OK")
        );
    }

    @Operation(summary = "알림 전체 삭제", description = "나한테 온 알림들을 전체 삭제한다.")
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteAllNoti(){

        String userId = SecurityUtil.getCurrentUserId();
        notifyService.deleteAllNotify(userId);

        return ResponseEntity.ok(
                ApiResponse.ok("OK")
        );
    }

    @Operation(summary = "알림 테스트", description = "테스트 알림을 생성하고, SSE를 확인한다.")
    @GetMapping("/send")
    public void sendNotify(){

        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        User user = userRepository.findByUserSeq(currentUserSeq).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Long flagSeq = 16L;
        Flag flag = flagRepository.findById(flagSeq).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        notifyService.send(user, user, flag, NotiType.BOARD, "/journal/best");
    }
}
