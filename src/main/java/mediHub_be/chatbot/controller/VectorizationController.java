package mediHub_be.chatbot.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.chatbot.service.VectorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class VectorizationController {

    private final VectorizationService vectorizationService;

    @PostMapping("/process-tables")
    public String processTablesInParallel() {
        vectorizationService.processAllTables();
        return "All tables are being processed in parallel!";
    }
}
