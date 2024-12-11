package mediHub_be.journal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "논문", description = "논문과 북마크")
@RequiredArgsConstructor
@RestController
@RequestMapping("/journal")
public class JournalController {

    private final JournalServiceImpl journalService;

    // 논문 검색
    @Operation(summary = "논문 검색", description = "자연어를 통해 논문들을 검색한다.")
    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<List<ResponsePubmedDTO>>> chat(@RequestParam(name = "prompt") String prompt){

        return ResponseEntity.ok(
                ApiResponse.ok(journalService.getPubmedKeywords(prompt))
        );
    }

    // 논문 자세히 보기 (조회 이력에 남음 및 해당 논문 Entity 생성됨)
    @Operation(summary = "논문 초록 요약 보기", description = "논문 자세히 보기, 상세 조회시 조회이력이 남는다.")
    @PostMapping("/{journalPmid}")
    public ResponseEntity<ApiResponse<?>> journal(@PathVariable("journalPmid") String journalPmid,
                                                  @RequestBody ResponsePubmedDTO requestDTO){

        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();
        return ResponseEntity.ok(
                ApiResponse.ok(journalService.summarizeAbstractByPmid(currentUserSeq, journalPmid, requestDTO))
        );
    }

    // 등록된 논문 조회 sortBy = select or bookmark(조회순 100개, 북마크순 100개)
    @Operation(summary = "등록된 논문 조회", description = "등록된 논문 조회 sortBy = select or bookmark(조회순 100개, 북마크순 100개)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponseJournalSearchDTO>>> getJournal(@RequestParam(name = "sortBy") String sortBy){

        return ResponseEntity.ok(
                ApiResponse.ok(journalService.getJournalTop100(sortBy))
        );
    }
    
    // 논문 북마크
    @Operation(summary = "논문 북마크", description = "논문을 북마크 한다.")
    @PostMapping("/bookmark/{journalSeq}")
    public ResponseEntity<ApiResponse<?>> bookmark(@PathVariable("journalSeq") Long journalSeq){

        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();
        
        if (journalService.journalBookmark(currentUserSeq, journalSeq)){
            return ResponseEntity.ok(ApiResponse.ok("북마크 완료"));
        }
        return ResponseEntity.ok(ApiResponse.ok("북마크 해제"));
    }

    // 내가 조회한 논문 (북마크, 조회)
    @Operation(summary = "조회 또는 북마크한 논문들 확인", description = "내가 조회한 논문 (북마크, 조회) sortBy = select or bookmark")
    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<List<?>>> getJournalSearch(@RequestParam("sortBy") String sortBy){

        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        // 조회 (select)
        if (sortBy.equals("select")){
            return ResponseEntity.ok(
                    ApiResponse.ok(journalService.getMySearchJournal(currentUserSeq)));
            // 북마크 (bookmark)
        } else if (sortBy.equals("bookmark")) {
            return ResponseEntity.ok(
                    ApiResponse.ok(journalService.getMyBookmarkJournal(currentUserSeq)));
            // 그 외
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST_INPUT)));
    }
}
