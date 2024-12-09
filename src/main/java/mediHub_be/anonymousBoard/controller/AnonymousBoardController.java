package mediHub_be.anonymousBoard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.anonymousBoard.dto.*;
import mediHub_be.anonymousBoard.service.AnonymousBoardService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Tag(
        name = "익명 게시판",
        description = "익명 게시글 API"
)
@RestController
@RequestMapping(value = "anonymousBoard")
@RequiredArgsConstructor
@Slf4j
public class AnonymousBoardController {

    private final AnonymousBoardService anonymousBoardService;

    // 익명 게시글 목록 조회
    @Operation(
            summary = "익명 게시글 목록 조회", description = "익명 게시글 목록 반환"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<AnonymousBoardListDTO>>> getAllAnonymousBoards() {

        String userId = SecurityUtil.getCurrentUserId();
        List<AnonymousBoardListDTO> anonymousBoardList = anonymousBoardService.getAnonymousBoardList(userId);

        return ResponseEntity.ok(ApiResponse.ok(anonymousBoardList));
    }

    // 익명 게시글 댓글 목록 조회
    @Operation(
            summary = "익명 게시글 댓글 목록 조회", description = "익명 게시글 댓글 목록 반환"
    )
    @GetMapping(value = "/{anonymousBoardSeq}/comment")
    public ResponseEntity<ApiResponse<List<AnonymousBoardCommentListDTO>>> getAllAnonymousBoardComments(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq
    ) {

        String userId = SecurityUtil.getCurrentUserId();
        List<AnonymousBoardCommentListDTO> anonymousBoardCommentList = anonymousBoardService
                .getAnonymousBoardCommentList(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(anonymousBoardCommentList));
    }

    // 익명 게시글 조회
    @Operation(
            summary = "특정 익명 게시글 상세 조회",
            description = "익명 게시글 번호, 작성자, 제목, 내용, 상태, 키워드, 조회 수, 작성일, 수정일, 삭제일, 첨부 사진을 반환"
    )
    @GetMapping(value = "/{anonymousBoardSeq}")
    public ResponseEntity<ApiResponse<AnonymousBoardDetailDTO>> getAnonymousBoardDetail(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String userId = SecurityUtil.getCurrentUserId();
        AnonymousBoardDetailDTO anonymousBoardDetailDTO = anonymousBoardService.getAnonymousBoardDetail(
                anonymousBoardSeq,
                userId,
                request,
                response
        );

        return ResponseEntity.ok(ApiResponse.ok(anonymousBoardDetailDTO));
    }

    // 익명 게시글 등록
    @Operation(
            summary = "익명 게시글 등록",
            description = "새로운 익명 게시글의 제목, 내용, 첨부 사진을 입력 받아 새로운 익명 게시글 등록"
    )
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Long>> createAnonymousBoard(
            @RequestParam("anonymousBoardTitle") String anonymousBoardTitle,
            @RequestParam("anonymousBoardContent") String anonymousBoardContent,
            @RequestParam("keywords") List<String> keywords,
            @RequestParam(value = "imageList", required = false) List<MultipartFile> imageList
    ) throws IOException {

        String userId = SecurityUtil.getCurrentUserId();

        AnonymousBoardCreateRequestDTO requestDTO = new AnonymousBoardCreateRequestDTO();

        requestDTO.setAnonymousBoardTitle(anonymousBoardTitle);
        requestDTO.setAnonymousBoardContent(anonymousBoardContent);
        requestDTO.setKeywords(keywords);

        Long anonymousBoardSeq = anonymousBoardService.createAnonymousBoard(requestDTO, imageList, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(anonymousBoardSeq));
    }

    // 익명 게시글 댓글 등록
    @Operation(
            summary = "익명 게시글 댓글 등록",
            description = "새로운 익명 게시글 댓글의 내용을 입력 받아 새로운 익명 게시글 댓글 등록"
    )
    @PostMapping(value = "/{anonymousBoardSeq}/comment")
    public ResponseEntity<ApiResponse<Long>> createAnonymousBoardComment(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq,
            @RequestParam("commentContent") String commentContent
    ) {

        String userId = SecurityUtil.getCurrentUserId();
        Long commentSeq = anonymousBoardService.createAnonymousBoardComment(anonymousBoardSeq, commentContent, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(commentSeq));
    }

    // 익명 게시글 수정
    @Operation(
            summary = "익명 게시글 수정", description = "등록된 익명 게시글의 제목, 내용, 첨부 사진을 수정 (작성자만 가능)"
    )
    @PutMapping(value = "/{anonymousBoardSeq}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<String>> updateAnonymousBoard(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq,
            @RequestParam("anonymousBoardTitle") String anonymousBoardTitle,
            @RequestParam("anonymousBoardContent") String anonymousBoardContent,
            @RequestParam("keywords") List<String> keywords,
            @RequestParam(value = "imageList", required = false) List<MultipartFile> imageList
    ) throws IOException {

        String userId = SecurityUtil.getCurrentUserId();

        AnonymousBoardUpdateRequestDTO requestDTO = new AnonymousBoardUpdateRequestDTO();

        requestDTO.setAnonymousBoardTitle(anonymousBoardTitle);
        requestDTO.setAnonymousBoardContent(anonymousBoardContent);
        requestDTO.setKeywords(keywords);

        anonymousBoardService.updateAnonymousBoard(anonymousBoardSeq, requestDTO, imageList, userId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/anonymousBoard/{anonymousBoardSeq}")
                .buildAndExpand(anonymousBoardSeq)
                .toUri();

        return ResponseEntity.created(location).body(ApiResponse.ok("익명 게시글이 성공적으로 수정되었습니다."));
    }

    // 익명 게시글 댓글 수정
    @Operation(
            summary = "익명 게시글 댓글 수정", description = "등록된 익명 게시글 댓글의 내용을 수정 (작성자만 가능)"
    )
    @PutMapping(value = "/{anonymousBoardSeq}/comment/{commentSeq}")
    public ResponseEntity<ApiResponse<String>> updateAnonymousBoardComment(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq,
            @PathVariable("commentSeq") Long commentSeq,
            @RequestParam("commentContent") String commentContent
    ) {

        String userId = SecurityUtil.getCurrentUserId();

        anonymousBoardService.updateAnonymousBoardComment(anonymousBoardSeq, commentSeq, commentContent, userId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/anonymousBoard/{anonymousBoardSeq}/comment/{commentSeq}")
                .buildAndExpand(anonymousBoardSeq, commentSeq)
                .toUri();

        return ResponseEntity.created(location).body(ApiResponse.ok("익명 게시글 댓글이 성공적으로 수정되었습니다."));
    }

    // 익명 게시글 삭제
    @Operation(
            summary = "익명 게시글 삭제", description = "익명 게시글 삭제 (작성자 또는 관리자만 가능)"
    )
    @DeleteMapping(value = "/{anonymousBoardSeq}")
    public ResponseEntity<ApiResponse<String>> deleteAnonymousBoard(@PathVariable Long anonymousBoardSeq) {

        String userId = SecurityUtil.getCurrentUserId();
        boolean isDeleted = anonymousBoardService.deleteAnonymousBoard(anonymousBoardSeq, userId);

        // 삭제 성공 시 204 No Content 반환
        if (isDeleted) {
            return ResponseEntity.noContent().build();
          // 익명 게시글이 존재하지 않거나 삭제 실패 시 404 Not Found, 메시지 본문에 추가
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.ok(
                "익명 게시글을 찾을 수 없습니다."
        ));
    }

    // 익명 게시글 댓글 삭제
    @Operation(
            summary = "익명 게시글 댓글 삭제", description = "익명 게시글 댓글 삭제 (작성자 또는 관리자만 가능)"
    )
    @DeleteMapping(value = "/{anonymousBoardSeq}/comment/{commentSeq}")
    public ResponseEntity<ApiResponse<String>> deleteAnonymousBoardComment(
            @PathVariable Long anonymousBoardSeq,
            @PathVariable Long commentSeq
    ) {

        String userId = SecurityUtil.getCurrentUserId();
        boolean isDeleted = anonymousBoardService.deleteAnonymousBoardComment(anonymousBoardSeq, commentSeq, userId);

        // 삭제 성공 시 204 No Content 반환
        if (isDeleted) {
            return ResponseEntity.noContent().build();
            // 익명 게시글 댓글이 존재하지 않거나 삭제 실패 시 404 Not Found, 메시지 본문에 추가
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.ok(
                "익명 게시글 댓글을 찾을 수 없습니다."
        ));
    }

    @Operation(summary = "익명 게시글 북마크", description = "익명 게시글 북마크를 등록/해제")
    @PatchMapping("/{anonymousBoardSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> toggleBookmark(@PathVariable Long anonymousBoardSeq) {

        String userId = SecurityUtil.getCurrentUserId();
        boolean isBookmarked = anonymousBoardService.toggleBookmark(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "북마크 여부 확인", description = "익명 게시글에 대해 북마크 여부 확인")
    @GetMapping("/{anonymousBoardSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(@PathVariable Long anonymousBoardSeq) {

        String userId = SecurityUtil.getCurrentUserId();
        boolean isBookmarked = anonymousBoardService.isBookmarked(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "익명 게시글 좋아요", description = "익명 게시글 좋아요를 등록/해제")
    @PatchMapping("/{anonymousBoardSeq}/prefer")
    public ResponseEntity<ApiResponse<Boolean>> togglePrefer(@PathVariable Long anonymousBoardSeq) {

        String userId = SecurityUtil.getCurrentUserId();
        boolean isPreferred = anonymousBoardService.togglePrefer(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isPreferred));
    }

    @Operation(summary = "좋아요 여부 확인", description = "익명 게시글에 대해 좋아요 여부 확인")
    @GetMapping("/{anonymousBoardSeq}/prefer")
    public ResponseEntity<ApiResponse<Boolean>> isPreferred(@PathVariable Long anonymousBoardSeq) {

        String userId = SecurityUtil.getCurrentUserId();
        boolean isPreferred = anonymousBoardService.isPreferred(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isPreferred));
    }
}