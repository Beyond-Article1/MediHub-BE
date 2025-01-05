package mediHub_be.chatbot.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.chatbot.service.VectorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class VectorizationController {

    private final VectorizationService vectorizationService;

    @PostMapping("/process-tables")
    public String processTablesInParallel() {
        vectorizationService.processAllTables();
        return "All tables are being processed in parallel!";
    }

    // 저장된 모든 벡터 데이터를 로그로 출력하는 API
    @GetMapping("/log")
    public ResponseEntity<String> logAllVectors() {
        try {
            vectorizationService.logAllVectorizedData();
            return ResponseEntity.ok("Vectorized data logged successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while logging vectorized data.");
        }
    }
}
