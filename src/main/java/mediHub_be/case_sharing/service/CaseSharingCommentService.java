package mediHub_be.case_sharing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.service.PictureService;
import mediHub_be.case_sharing.dto.CaseSharingCommentDetailDTO;
import mediHub_be.case_sharing.dto.CaseSharingCommentListDTO;
import mediHub_be.case_sharing.dto.CaseSharingCommentRequestDTO;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.CaseSharingComment;
import mediHub_be.case_sharing.repository.CaseSharingCommentRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseSharingCommentService {

    private final CaseSharingService caseSharingService;
    private final UserService userService;
    private final PictureService pictureService;

    private final CaseSharingCommentRepository commentRepository;

    //1. 케이스 공유 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<CaseSharingCommentListDTO> getCommentList(String userId, Long caseSharingSeq) {
        userService.findByUserId(userId);
        List<CaseSharingComment> comments = commentRepository.findByCaseSharing_CaseSharingSeqAndDeletedAtIsNull(caseSharingSeq);
        return comments.stream()
                .map(comment -> CaseSharingCommentListDTO.builder()
                        .blockId(comment.getCaseSharingBlockId()) // 댓글의 블록 ID만 가져옴
                        .build()
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CaseSharingCommentDetailDTO> getCommentsByBlock(String userId, Long caseSharingSeq, String blockId) {
        userService.findByUserId(userId); // 사용자 검증
        List<CaseSharingComment> comments = commentRepository.findByCaseSharing_CaseSharingSeqAndCaseSharingBlockIdAndDeletedAtIsNull(caseSharingSeq, blockId);

        // DTO로 변환
        return comments.stream()
                .map(comment -> CaseSharingCommentDetailDTO.builder()
                        .userName(comment.getUser().getUserName()) // 댓글 작성자명
                        .userRankName(comment.getUser().getRanking().getRankingName()) // 댓글 작성자 직위명
                        .content(comment.getCaseSharingCommentContent()) // 댓글 내용
                        .createdAt(comment.getCreatedAt()) // 댓글 작성일
                        .userProfileURL(pictureService.getUserProfileUrl(comment.getUser().getUserSeq())) // 프로필 이미지 URL
                        .build())
                .toList();
    }

    //3. 케이스 공유 댓글 생성
    @Transactional
    public Long createCaseSharingComment(String userId, Long caseSharingSeq, CaseSharingCommentRequestDTO requestDTO) {
        User user = userService.findByUserId(userId);
        CaseSharing caseSharing = caseSharingService.findCaseSharing(caseSharingSeq);

        CaseSharingComment comment = CaseSharingComment.builder()
                .user(user)
                .caseSharing(caseSharing)
                .caseSharingCommentContent(requestDTO.getContent())
                .caseSharingBlockId(requestDTO.getBlockId())
                .build();

        commentRepository.save(comment);
        return comment.getCaseSharingCommentSeq();
    }

    //4. 케이스 공유 댓글 수정
    @Transactional
    public void updateCaseSharingComment(String userId, Long commentSeq, CaseSharingCommentRequestDTO requestDTO) {
        User user = userService.findByUserId(userId);
        CaseSharingComment comment = findComment(commentSeq);
        validateAuthor(comment, user);

        comment.updateComment(requestDTO.getContent(), requestDTO.getBlockId());
        commentRepository.save(comment);
    }

    //5. 케이스 공유 댓글 삭제
    @Transactional
    public void deleteCaseSharingComment(String userId, Long commentSeq) {
        User user = userService.findByUserId(userId);

        CaseSharingComment comment = findComment(commentSeq);
        validateAuthor(comment, user);

        comment.markAsDeleted();
        commentRepository.save(comment);
    }

    private CaseSharingComment findComment(Long commentSeq) {
        return commentRepository.findById(commentSeq)
                .filter(c -> c.getDeletedAt() == null) // 삭제된 댓글은 조회 불가
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

    }

    private void validateAuthor(CaseSharingComment caseSharingComment, User user) {
        if (!caseSharingComment.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }


}
