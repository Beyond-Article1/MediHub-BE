package mediHub_be.openai.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.openai.dto.ResponsePubmedDTO;
import mediHub_be.openai.service.JournalServiceImpl;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/journal")
public class JournalController {

    private final JournalServiceImpl journalService;

    // 논문 검색
    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<List<ResponsePubmedDTO>>> chat(@RequestParam(name = "prompt") String prompt){

        return ResponseEntity.ok(
                ApiResponse.ok(journalService.getPubmedKeywords(prompt))
        );
    }

    // 논문 자세히 보기 (조회 이력에 남음 및 해당 논문 Entity 생성됨)
    @PostMapping("/{journalPmid}")
    public ResponseEntity<ApiResponse<?>> journal(@PathVariable("journalPmid") String journalPmid,
                                                  @RequestBody ResponsePubmedDTO requestDTO){

        String currentUserId = SecurityUtil.getCurrentUserId();

        return ResponseEntity.ok(
                ApiResponse.ok(journalService.summarizeAbstractByPmid(currentUserId, journalPmid, requestDTO))
        );
    }
}
