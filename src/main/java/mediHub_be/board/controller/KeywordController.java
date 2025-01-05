package mediHub_be.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.service.KeywordService;
import mediHub_be.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("keyword")
@RequiredArgsConstructor
@Tag(name = "키워드", description = "키워드 API")
public class KeywordController {

    private final KeywordService keywordService;

    @Operation(summary = "키워드 전체 목록 조회", description = "키워드 전체 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Keyword>>> getAllKeywords() {

        List<Keyword> keywordList = keywordService.findAllKeyword();

        return ResponseEntity.ok(ApiResponse.ok(keywordList));
    }
}