package mediHub_be.elasticsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.elasticsearch.document.*;
import mediHub_be.elasticsearch.service.ElasticsearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "find")
@Tag(name = "Elasticsearch", description = "엘라스틱서치 조회 API")
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    @GetMapping("/anonymousBoard/{findAnonymousBoardTitle}")
    @Operation(summary = "익명 게시판 제목 검색", description = "엘라스틱서치로 익명 게시판 제목 검색")
    public ResponseEntity<List<AnonymousBoardDocument>> findAnonymousBoard(
            @PathVariable String findAnonymousBoardTitle
    ) {

        return ResponseEntity.ok(elasticsearchService.findAnonymousBoard(findAnonymousBoardTitle));
    }

    @GetMapping("/anonymousBoard/autoComplete/{queryAnonymousBoardTitle}")
    @Operation(summary = "익명 게시판 검색어 자동 완성", description = "엘라스틱서치로 익명 게시판 검색어 자동 완성")
    public ResponseEntity<List<String>> queryAnonymousBoard(
            @PathVariable String queryAnonymousBoardTitle
    ) {

        return ResponseEntity.ok(elasticsearchService.queryAnonymousBoard(queryAnonymousBoardTitle));
    }

    @PostMapping("/index/anonymousBoard")
    @Operation(summary = "익명 게시판 엘라스틱서치 인덱스 생성", description = "익명 게시판 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexAnonymousBoard() {

        elasticsearchService.indexAnonymousBoard();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/caseSharing/{findCaseSharingTitle}")
    @Operation(summary = "케이스 공유 제목 검색", description = "엘라스틱서치로 케이스 공유 제목 검색")
    public ResponseEntity<List<CaseSharingDocument>> findCaseSharing(@PathVariable String findCaseSharingTitle) {

        return ResponseEntity.ok(elasticsearchService.findCaseSharing(findCaseSharingTitle));
    }

    @PostMapping("/index/caseSharing")
    @Operation(summary = "케이스 공유 엘라스틱서치 인덱스 생성", description = "케이스 공유 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexCaseSharing() {

        elasticsearchService.indexCaseSharing();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/cp/{findCpName}")
    @Operation(summary = "CP cp명 검색", description = "엘라스틱서치로 CP cp명 검색")
    public ResponseEntity<List<CpDocument>> findCp(@PathVariable String findCpName) {

        return ResponseEntity.ok(elasticsearchService.findCp(findCpName));
    }

    @PostMapping("/index/cp")
    @Operation(summary = "CP 엘라스틱서치 인덱스 생성", description = "CP DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexCp() {

        elasticsearchService.indexCp();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/journal/{findJournalKoreanTitle}")
    @Operation(summary = "논문 한글 제목 검색", description = "엘라스틱서치로 논문 한글 제목 검색")
    public ResponseEntity<List<JournalDocument>> findJournal(@PathVariable String findJournalKoreanTitle) {

        return ResponseEntity.ok(elasticsearchService.findJournal(findJournalKoreanTitle));
    }

    @PostMapping("/index/journal")
    @Operation(summary = "논문 엘라스틱서치 인덱스 생성", description = "논문 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexJournal() {

        elasticsearchService.indexJournal();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/medicalLife/{findMedicalLifeTitle}")
    @Operation(summary = "메디컬 라이프 제목 검색", description = "엘라스틱서치로 메디컬 라이프 제목 검색")
    public ResponseEntity<List<MedicalLifeDocument>> findMedicalLife(@PathVariable String findMedicalLifeTitle) {

        return ResponseEntity.ok(elasticsearchService.findMedicalLife(findMedicalLifeTitle));
    }

    @PostMapping("/index/medicalLife")
    @Operation(summary = "메디컬 라이프 엘라스틱서치 인덱스 생성", description = "메디컬 라이프 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexMedicalLife() {

        elasticsearchService.indexMedicalLife();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{findUserName}")
    @Operation(summary = "직원 이름 검색", description = "엘라스틱서치로 직원 이름 검색")
    public ResponseEntity<List<UserDocument>> findUser(@PathVariable String findUserName) {

        return ResponseEntity.ok(elasticsearchService.findUser(findUserName));
    }

    @PostMapping("/index/user")
    @Operation(summary = "직원 엘라스틱서치 인덱스 생성", description = "직원 DB 데이터를 엘라스틱서치에 동기화")
    public ResponseEntity<Void> indexUser() {

        elasticsearchService.indexUser();

        return ResponseEntity.ok().build();
    }
}