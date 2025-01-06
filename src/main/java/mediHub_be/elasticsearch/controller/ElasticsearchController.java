package mediHub_be.elasticsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.elasticsearch.document.*;
import mediHub_be.elasticsearch.service.ElasticsearchService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/find")
@Tag(name = "엘라스틱서치", description = "엘라스틱서치 조회 API")
@ConditionalOnProperty(
        name = "spring.data.elasticsearch.repositories.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    @GetMapping("/anonymousBoard/{findAnonymousBoardTitle}")
    @Operation(summary = "익명 게시판 제목 검색", description = "엘라스틱서치로 익명 게시판 제목 검색")
    public ResponseEntity<ApiResponse<List<AnonymousBoardDocument>>> findAnonymousBoard(
            @PathVariable String findAnonymousBoardTitle
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.findAnonymousBoard(findAnonymousBoardTitle)));
    }

    @GetMapping("/anonymousBoard/autoComplete/{queryAnonymousBoardTitle}")
    @Operation(summary = "익명 게시판 검색어 자동 완성", description = "엘라스틱서치로 익명 게시판 검색어 자동 완성")
    public ResponseEntity<ApiResponse<List<String>>> queryAnonymousBoard(
            @PathVariable String queryAnonymousBoardTitle
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.queryAnonymousBoard(queryAnonymousBoardTitle)));
    }

    @PostMapping("/index/anonymousBoard")
    @Operation(summary = "익명 게시판 엘라스틱서치 인덱스 생성", description = "익명 게시판 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexAnonymousBoard() {

        elasticsearchService.indexAnonymousBoard();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/caseSharing/{findCaseSharingTitle}")
    @Operation(summary = "케이스 공유 제목 검색", description = "엘라스틱서치로 케이스 공유 제목 검색")
    public ResponseEntity<ApiResponse<List<CaseSharingDocument>>> findCaseSharing(
            @PathVariable String findCaseSharingTitle
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.findCaseSharing(findCaseSharingTitle)));
    }

    @GetMapping("/caseSharing/autoComplete/{queryCaseSharingTitle}")
    @Operation(summary = "케이스 공유 검색어 자동 완성", description = "엘라스틱서치로 케이스 공유 검색어 자동 완성")
    public ResponseEntity<ApiResponse<List<String>>> queryCaseSharing(
            @PathVariable String queryCaseSharingTitle
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.queryCaseSharing(queryCaseSharingTitle)));
    }

    @PostMapping("/index/caseSharing")
    @Operation(summary = "케이스 공유 엘라스틱서치 인덱스 생성", description = "케이스 공유 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexCaseSharing() {

        elasticsearchService.indexCaseSharing();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/cp/{findCpName}")
    @Operation(summary = "CP 제목 검색", description = "엘라스틱서치로 CP 제목 검색")
    public ResponseEntity<ApiResponse<List<CpDocument>>> findCp(
            @PathVariable String findCpName
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.findCp(findCpName)));
    }

    @GetMapping("/cp/autoComplete/{queryCpName}")
    @Operation(summary = "CP 검색어 자동 완성", description = "엘라스틱서치로 CP 검색어 자동 완성")
    public ResponseEntity<ApiResponse<List<String>>> queryCp(
            @PathVariable String queryCpName
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.queryCp(queryCpName)));
    }

    @PostMapping("/index/cp")
    @Operation(summary = "CP 엘라스틱서치 인덱스 생성", description = "CP DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexCp() {

        elasticsearchService.indexCp();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/journal/{findJournalKoreanTitle}")
    @Operation(summary = "논문 한글 제목 검색", description = "엘라스틱서치로 논문 한글 제목 검색")
    public ResponseEntity<ApiResponse<List<JournalDocument>>> findJournal(
            @PathVariable String findJournalKoreanTitle
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.findJournal(findJournalKoreanTitle)));
    }

    @GetMapping("/journal/autoComplete/{queryJournalKoreanTitle}")
    @Operation(summary = "논문 검색어 자동 완성", description = "엘라스틱서치로 논문 검색어 자동 완성")
    public ResponseEntity<ApiResponse<List<String>>> queryJournal(
            @PathVariable String queryJournalKoreanTitle
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.queryJournal(queryJournalKoreanTitle)));
    }

    @PostMapping("/index/journal")
    @Operation(summary = "논문 엘라스틱서치 인덱스 생성", description = "논문 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexJournal() {

        elasticsearchService.indexJournal();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/medicalLife/{findMedicalLifeTitle}")
    @Operation(summary = "메디컬 라이프 제목 검색", description = "엘라스틱서치로 메디컬 라이프 제목 검색")
    public ResponseEntity<ApiResponse<List<MedicalLifeDocument>>> findMedicalLife(
            @PathVariable String findMedicalLifeTitle
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.findMedicalLife(findMedicalLifeTitle)));
    }

    @GetMapping("/medicalLife/autoComplete/{queryMedicalLifeTitle}")
    @Operation(summary = "메디컬 라이프 검색어 자동 완성", description = "엘라스틱서치로 메디컬 라이프 검색어 자동 완성")
    public ResponseEntity<ApiResponse<List<String>>> queryMedicalLife(
            @PathVariable String queryMedicalLifeTitle
    ) {

        return ResponseEntity.ok(ApiResponse.ok(elasticsearchService.queryMedicalLife(queryMedicalLifeTitle)));
    }

    @PostMapping("/index/medicalLife")
    @Operation(summary = "메디컬 라이프 엘라스틱서치 인덱스 생성", description = "메디컬 라이프 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexMedicalLife() {

        elasticsearchService.indexMedicalLife();

        return ResponseEntity.ok().build();
    }
}