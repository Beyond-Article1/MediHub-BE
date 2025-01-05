package mediHub_be.chatbot.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.chatbot.service.VectorizationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VectorizationScheduler {

    private final VectorizationService vectorizationService;

    // 매일 새벽 2시에 실행
    @Scheduled(cron = "0 0 2 * * *") // 초 분 시간 일 월 요일(* 매일)
    public void scheduleTableProcessing() {
        log.info("Processing all tables in parallel at 2 AM.");
        vectorizationService.processAllTables();
    }
}
