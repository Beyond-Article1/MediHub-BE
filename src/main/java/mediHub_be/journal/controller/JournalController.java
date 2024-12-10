package mediHub_be.journal.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.journal.dto.ResponseJournalLogDTO;
import mediHub_be.journal.dto.ResponseJournalSearchDTO;
import mediHub_be.journal.dto.ResponsePubmedDTO;
import mediHub_be.journal.service.JournalServiceImpl;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
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

    // 등록된 논문 조회 sortBy = select or bookmark(조회순 100개, 북마크순 100개)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponseJournalSearchDTO>>> getJournal(@RequestParam(name = "sortBy") String sortBy){

        return ResponseEntity.ok(
                ApiResponse.ok(journalService.getJournalTop100(sortBy))
        );
    }
    
    // 논문 북마크
    @PostMapping("/bookmark/{journalSeq}")
    public ResponseEntity<ApiResponse<?>> bookmark(@PathVariable("journalSeq") Long journalSeq){

        String currentUserId = SecurityUtil.getCurrentUserId();
        
        if (journalService.journalBookmark(currentUserId, journalSeq)){
            return ResponseEntity.ok(ApiResponse.ok("북마크 완료"));
        }
        return ResponseEntity.ok(ApiResponse.ok("북마크 해제"));
    }

    // 내가 조회한 논문 (북마크, 조회)
    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<List<?>>> getJournalSearch(@RequestParam("sortBy") String sortBy){

        String currentUserId = SecurityUtil.getCurrentUserId();

        // 조회 (select)
        if (sortBy.equals("select")){
            return ResponseEntity.ok(
                    ApiResponse.ok(journalService.getMySearchJournal(currentUserId)));
            // 북마크 (bookmark)
        } else if (sortBy.equals("bookmark")) {
            return ResponseEntity.ok(
                    ApiResponse.ok(journalService.getMyBookmarkJournal(currentUserId)));
            // 그 외
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST_INPUT)));
    }
}
