package mediHub_be.anonymousBoard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.anonymousBoard.dto.*;
import mediHub_be.anonymousBoard.service.AnonymousBoardService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "anonymous-board")
@RequiredArgsConstructor
@Tag(name = "익명 게시판", description = "익명 게시판 API")
public class AnonymousBoardController {

    private final AnonymousBoardService anonymousBoardService;

    @Operation(summary = "익명 게시판 전체 목록 조회", description = "필터링 되지 않은 익명 게시판 전체 글 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AnonymousBoardDTO>>> getAllBoards() {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<AnonymousBoardDTO> anonymousBoardDTOList = anonymousBoardService.getBoardList(userSeq);

        return ResponseEntity.ok(ApiResponse.ok(anonymousBoardDTOList));
    }

    @Operation(summary = "내가 작성한 익명 게시글 목록 조회", description = "내가 작성한, 삭제되지 않은 익명 게시글 목록 조회")
    @GetMapping("/myPage")
    public ResponseEntity<ApiResponse<List<AnonymousBoardMyPageDTO>>> getMyPageBoards() {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<AnonymousBoardMyPageDTO> anonymousBoardMyPageDTOList = anonymousBoardService.getMyPageBoardList(userSeq);

        return ResponseEntity.ok(ApiResponse.ok(anonymousBoardMyPageDTOList));
    }

    @Operation(summary = "내가 북마크 한 익명 게시글 목록 조회", description = "내가 북마크 한, 삭제되지 않은 익명 게시글 목록 조회")
    @GetMapping("/myPage/bookmark")
    public ResponseEntity<ApiResponse<List<AnonymousBoardMyPageDTO>>> getBookMarkedBoards() {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<AnonymousBoardMyPageDTO> anonymousBoardDTOList = anonymousBoardService.getBookMarkedBoardList(userSeq);

        return ResponseEntity.ok(ApiResponse.ok(anonymousBoardDTOList));
    }

    @Operation(
            summary = "익명 게시판 메인 TOP 3 조회",
            description = "메인 페이지에 들어갈 일주일 내 작성된 게시글 중 조회 수 TOP 3 조회"
    )
    @GetMapping("/top3")
    public ResponseEntity<ApiResponse<List<AnonymousBoardTop3DTO>>> getBoardsTop3() {

        List<AnonymousBoardTop3DTO> top3BoardsList = anonymousBoardService.getTop3Boards();

        return ResponseEntity.ok(ApiResponse.ok(top3BoardsList));
    }

    @Operation(summary = "익명 게시판 댓글 전체 목록 조회", description = "필터링 되지 않은 익명 게시판 전체 댓글 목록 조회")
    @GetMapping(value = "/{anonymousBoardSeq}/comment")
    public ResponseEntity<ApiResponse<List<AnonymousBoardCommentDTO>>> getAllBoardComments(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq
    ) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<AnonymousBoardCommentDTO> anonymousBoardCommentDTOList = anonymousBoardService
                .getBoardCommentList(anonymousBoardSeq, userSeq);

        return ResponseEntity.ok(ApiResponse.ok(anonymousBoardCommentDTOList));
    }

    @Operation(
            summary = "익명 게시판 상세 조회",
            description = "익명 게시판 작성자, 제목, 내용, 조회 수, 작성일, 첨부 사진, 키워드, 북마크, 좋아요 포함한 글 조회"
    )
    @GetMapping(value = "/{anonymousBoardSeq}")
    public ResponseEntity<ApiResponse<AnonymousBoardDetailDTO>> getBoardDetail(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        AnonymousBoardDetailDTO anonymousBoardDetailDTO = anonymousBoardService.getAnonymousBoardDetail(
                anonymousBoardSeq,
                userSeq,
                request,
                response
        );

        return ResponseEntity.ok(ApiResponse.ok(anonymousBoardDetailDTO));
    }

    @Operation(summary = "익명 게시글 등록", description = "익명 게시판 글 작성 및 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createAnonymousBoard(
            @RequestPart("data") AnonymousBoardCreateRequestDTO anonymousBoardCreateRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> pictureList
    ) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long anonymousBoardSeq = anonymousBoardService.createAnonymousBoard(
                anonymousBoardCreateRequestDTO,
                pictureList,
                userSeq
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(anonymousBoardSeq));
    }

    @Operation(summary = "익명 게시글 댓글 등록", description = "익명 게시판 댓글 작성 및 등록")
    @PostMapping(value = "/{anonymousBoardSeq}/comment")
    public ResponseEntity<ApiResponse<Long>> createAnonymousBoardComment(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq,
            @RequestBody AnonymousBoardCommentRequestDTO anonymousBoardCommentRequestDTO
    ) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long commentSeq = anonymousBoardService.createAnonymousBoardComment(
                anonymousBoardSeq,
                anonymousBoardCommentRequestDTO,
                userSeq
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(commentSeq));
    }

    @Operation(summary = "익명 게시판 내용 수정", description = "익명 게시판 글의 제목과 내용, 키워드 등을 수정")
    @PutMapping(value = "/{anonymousBoardSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> updateAnonymousBoard(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq,
            @RequestPart AnonymousBoardUpdateRequestDTO anonymousBoardUpdateRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> newPictureList
    ) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long updatedAnonymousBoardSeq = anonymousBoardService.updateAnonymousBoard(
                anonymousBoardSeq,
                anonymousBoardUpdateRequestDTO,
                newPictureList,
                userSeq
        );

        return ResponseEntity.ok(ApiResponse.ok(updatedAnonymousBoardSeq));
    }

    @Operation(summary = "익명 게시판 댓글 내용 수정", description = "익명 게시판 댓글 글의 내용을 수정")
    @PutMapping(value = "/{anonymousBoardSeq}/comment/{commentSeq}")
    public ResponseEntity<ApiResponse<Long>> updateAnonymousBoardComment(
            @PathVariable("anonymousBoardSeq") Long anonymousBoardSeq,
            @PathVariable("commentSeq") Long commentSeq,
            @RequestBody AnonymousBoardCommentRequestDTO anonymousBoardCommentRequestDTO
    ) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long updatedCommentSeq = anonymousBoardService.updateAnonymousBoardComment(
                anonymousBoardSeq,
                commentSeq,
                anonymousBoardCommentRequestDTO,
                userSeq
        );

        return ResponseEntity.ok(ApiResponse.ok(updatedCommentSeq));
    }

    @Operation(summary = "익명 게시글 삭제", description = "익명 게시글 삭제")
    @DeleteMapping(value = "/{anonymousBoardSeq}")
    public ResponseEntity<ApiResponse<String>> deleteAnonymousBoard(@PathVariable Long anonymousBoardSeq) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        boolean isDeleted = anonymousBoardService.deleteAnonymousBoard(anonymousBoardSeq, userSeq);

        if(isDeleted) {

            return ResponseEntity.noContent().build();
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(
                new CustomException(ErrorCode.NOT_FOUND_ANONYMOUS_BOARD)
        ));
    }

    @Operation(summary = "익명 게시글 댓글 삭제", description = "익명 게시글 댓글 삭제")
    @DeleteMapping(value = "/{anonymousBoardSeq}/comment/{commentSeq}")
    public ResponseEntity<ApiResponse<String>> deleteAnonymousBoardComment(
            @PathVariable Long anonymousBoardSeq,
            @PathVariable Long commentSeq
    ) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        boolean isDeleted = anonymousBoardService.deleteAnonymousBoardComment(anonymousBoardSeq, commentSeq, userSeq);

        if (isDeleted) {

            return ResponseEntity.noContent().build();
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(
                new CustomException(ErrorCode.NOT_FOUND_COMMENT)
        ));
    }

    @Operation(summary = "익명 게시글 북마크", description = "익명 게시글 북마크 등록/해제")
    @PatchMapping("/{anonymousBoardSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> toggleBookmark(@PathVariable Long anonymousBoardSeq) {

//        Long userSeq = SecurityUtil.getCurrentUserSeq();
        String userId = SecurityUtil.getCurrentUserId();
        boolean isBookmarked = anonymousBoardService.toggleBookmark(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "북마크 여부 확인", description = "익명 게시글에 대해 북마크 여부 확인")
    @GetMapping("/{anonymousBoardSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(@PathVariable Long anonymousBoardSeq) {

//        Long userSeq = SecurityUtil.getCurrentUserSeq();
        String userId = SecurityUtil.getCurrentUserId();
        boolean isBookmarked = anonymousBoardService.isBookmarked(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "익명 게시글 좋아요", description = "익명 게시글 좋아요 등록/해제")
    @PatchMapping("/{anonymousBoardSeq}/prefer")
    public ResponseEntity<ApiResponse<Boolean>> togglePrefer(@PathVariable Long anonymousBoardSeq) {

//        Long userSeq = SecurityUtil.getCurrentUserSeq();
        String userId = SecurityUtil.getCurrentUserId();
        boolean isPreferred = anonymousBoardService.togglePrefer(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isPreferred));
    }

    @Operation(summary = "좋아요 여부 확인", description = "익명 게시글에 대해 좋아요 여부 확인")
    @GetMapping("/{anonymousBoardSeq}/prefer")
    public ResponseEntity<ApiResponse<Boolean>> isPreferred(@PathVariable Long anonymousBoardSeq) {

//        Long userSeq = SecurityUtil.getCurrentUserSeq();
        String userId = SecurityUtil.getCurrentUserId();
        boolean isPreferred = anonymousBoardService.isPreferred(anonymousBoardSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isPreferred));
    }
}