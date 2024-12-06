package mediHub_be.case_sharing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.case_sharing.dto.CaseSharingCommentDetailDTO;
import mediHub_be.case_sharing.dto.CaseSharingCommentListDTO;
import mediHub_be.case_sharing.dto.CaseSharingCommentRequestDTO;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.CaseSharingComment;
import mediHub_be.case_sharing.repository.CaseSharingCommentRepository;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseSharingCommentService {

    private final UserRepository userRepository;
    private final CaseSharingCommentRepository commentRepository;
    private final CaseSharingService caseSharingService;
    private final CaseSharingRepository caseSharingRepository;

    public List<CaseSharingCommentListDTO> getCommentList(String userId, Long caseSharingSeq) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        List<CaseSharingComment> comments = commentRepository.findByCaseSharing_CaseSharingSeqAndDeletedAtIsNull(caseSharingSeq);
        return comments.stream()
                .map(comment -> CaseSharingCommentListDTO.builder()
                        .caseSharingCommentStartOffset(comment.getCaseSharingCommentStartOffset())
                        .caseSharingCommentEndOffset(comment.getCaseSharingCommentEndOffset())
                        .build())
                .toList();
    }

    public CaseSharingCommentDetailDTO getCommentDetail(String userId, Long commentSeq) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        CaseSharingComment comment = commentRepository.findById(commentSeq)
                .filter(c -> c.getDeletedAt() == null) // 삭제된 댓글은 조회 불가
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 삭제된 댓글입니다."));

        // 댓글 작성자 정보 조회
        User commentAuthor = userRepository.findById(comment.getUser().getUserSeq())
                .orElseThrow(() -> new IllegalArgumentException("댓글 작성자 정보를 찾을 수 없습니다."));

        // DTO 생성 및 반환
        return CaseSharingCommentDetailDTO.builder()
                .userName(commentAuthor.getUserName()) // 댓글 작성자명
                .userRankName(commentAuthor.getRanking().getRankingName()) // 댓글 작성자 직위명
                .content(comment.getCaseSharingCommentContent()) // 댓글 내용
                .startOffset(comment.getCaseSharingCommentStartOffset()) // 본문 시작 위치
                .endOffset(comment.getCaseSharingCommentEndOffset()) // 본문 끝 위치
                .createdAt(comment.getCreatedAt()) // 댓글 작성일
                .build();
    }

    @Transactional
    public Long createCaseSharingComment(String userId, CaseSharingCommentRequestDTO requestDTO) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        CaseSharing caseSharing = caseSharingRepository.findById(requestDTO.getCaseSharingSeq())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        CaseSharingComment comment = CaseSharingComment.builder()
                .user(user)
                .caseSharing(caseSharing)
                .caseSharingCommentContent(requestDTO.getContent())
                .caseSharingCommentStartOffset(requestDTO.getStartOffset())
                .caseSharingCommentEndOffset(requestDTO.getEndOffset())
                .build();

        commentRepository.save(comment);
        return comment.getCaseSharingCommentSeq();
    }

    // 댓글 수정
    @Transactional
    public void updateCaseSharingComment(String userId, Long commentSeq, CaseSharingCommentRequestDTO requestDTO) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        CaseSharingComment comment = commentRepository.findById(commentSeq)
                .filter(c -> c.getDeletedAt() == null) // 삭제된 댓글은 수정 불가
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 삭제된 댓글입니다."));

        if (!comment.getUser().equals(user)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }

        comment.updateComment(requestDTO.getContent(), requestDTO.getStartOffset(), requestDTO.getEndOffset());
        commentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteCaseSharingComment(String userId, Long commentSeq) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        CaseSharingComment comment = commentRepository.findById(commentSeq)
                .filter(c -> c.getDeletedAt() == null) // 이미 삭제된 댓글은 다시 삭제 불가
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 이미 삭제된 댓글입니다."));

        if (!comment.getUser().equals(user)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        comment.markAsDeleted();
        commentRepository.save(comment);
    }
}
