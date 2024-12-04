package mediHub_be.case_sharing.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.CaseSharingComment;
import mediHub_be.board.entity.Keyword;
import mediHub_be.case_sharing.entity.Version;
import mediHub_be.case_sharing.repository.CaseSharingCommentRepository;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.case_sharing.repository.VersionRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaseSharingService {
    private final CaseSharingRepository caseSharingRepository;
    private final CaseSharingCommentRepository commentRepository;
    private final VersionRepository versionRepository;
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;
    // 케이스 공유 전체(목록) 조회
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getCaseList() {
        return caseSharingRepository.findAll().stream().map(caseSharing -> {
            User author = userRepository.findByUserId(caseSharing.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

            return new CaseSharingListDTO(
                    caseSharing.getCaseSharingSeq(),
                    caseSharing.getCaseSharingTitle(),
                    author.getUserName(),
                    author.getRanking().getRankingName(),
                    caseSharing.getCreatedAt()
            );
        }).collect(Collectors.toList());
    }
    //케이스 공유 상세 조회
    @Transactional(readOnly = true)
    public CaseSharingDetailDTO getCaseSharingDetail(Long caseSharingSeq) {
        // 게시글 정보 조회
        CaseSharing caseSharing = caseSharingRepository.findById(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 정보 조회
        User author = userRepository.findById(caseSharing.getUser().getUserSeq())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        // 댓글 내역 반환
        List<CaseSharingComment> comments = commentRepository.findByCaseSharing_CaseSharingSeqAndDeletedAtIsNull(caseSharingSeq);
        List<CaseSharingCommentDTO> commentDTOs = comments.stream()
                .map(comment -> {
                    // 댓글 작성자 정보 조회
                    User commentAuthor = userRepository.findById(comment.getUser().getUserSeq())
                            .orElseThrow(() -> new IllegalArgumentException("댓글 작성자 정보를 찾을 수 없습니다."));
                    return CaseSharingCommentDTO.builder()
                            .commentSeq(comment.getCaseSharingCommentSeq())
                            .userName(commentAuthor.getUserName()) // 댓글 작성자 이름
                            .userRankName(commentAuthor.getRanking().getRankingName()) // 댓글 작성자 직위명
                            .content(comment.getCaseSharingCommentContent())
                            .startOffset(comment.getCaseSharingCommentStartOffset())
                            .endOffset(comment.getCaseSharingCommentEndOffset())
                            .createdAt(comment.getCreatedAt())
                            .build();
                }).toList();

        // 키워드 내역 반환
        List<Keyword> keywords = keywordRepository.findByBoardFlagAndPostSeq( "CASE_SHARING",caseSharingSeq);
        List<CaseSharingKeywordDTO> keywordDTOs = keywords.stream()
                .map(keyword -> new CaseSharingKeywordDTO(
                        keyword.getKeywordSeq(),
                        keyword.getKeywordName()
                ))
                .toList();

        // 버전 내역 반환
        List<Version> versions = versionRepository.findByCaseSharingCaseSharingSeq(caseSharingSeq);
        List<VersionDTO> versionDTOs = versions.stream()
                .map(version -> new VersionDTO(
                        version.getVersionSeq(),
                        version.getVersionNum(),
                        version.isVersionIsLatest()
                ))
                .toList();

        // DTO 생성
        return CaseSharingDetailDTO.builder()
                .caseSharingSeq(caseSharing.getCaseSharingSeq()) // 게시글 ID
                .caseSharingTitle(caseSharing.getCaseSharingTitle()) // 게시글 제목
                .caseSharingContent(caseSharing.getCaseSharingContent()) // 게시글 내용
                .caseAuthor(author.getUserName()) // 작성자 이름
                .caseAuthorRankName(author.getRanking().getRankingName()) // 작성자 직위명
                .comments(commentDTOs) // 댓글 리스트
                .keywords(keywordDTOs) // 키워드 리스트
                .versions(versionDTOs) // 버전 리스트
                .build();
    }
}
