package mediHub_be.case_sharing.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.service.KeywordService;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.CaseSharingComment;
import mediHub_be.board.entity.Keyword;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.case_sharing.entity.Version;
import mediHub_be.case_sharing.repository.CaseSharingCommentRepository;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.case_sharing.repository.TemplateRepository;
import mediHub_be.case_sharing.repository.VersionRepository;
import mediHub_be.part.entity.Part;
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
    private final TemplateRepository templateRepository;
    private final KeywordService keywordService;

    // 1. 케이스 공유 전체(목록) 조회
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getCaseList() {
        return caseSharingRepository.findAll().stream()
                .filter(caseSharing -> {
                    Version latestVersion = versionRepository
                            .findFirstByCaseSharingCaseSharingSeqAndVersionIsLatestTrue(caseSharing.getCaseSharingSeq());
                    return latestVersion != null; // 최신 버전인 경우만 필터링
                })
                .map(caseSharing -> {
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

    //2. 케이스 공유 상세 조회
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

    //3. 케이스 공유 등록
    public Long createCaseSharing(CaseSharingCreateRequestDTO requestDTO) {
        // 작성자 검증
        User user = userRepository.findById(requestDTO.getUserSeq())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        if (!"진료과".equals(user.getPart().getDept().getDeptName())) {
            throw new IllegalArgumentException("케이스 공유글은 의사만 작성할 수 있습니다.");
        }

        // 템플릿 조회
        Template template = templateRepository.findById(requestDTO.getTemplateSeq())
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿을 찾을 수 없습니다."));

        //케이스 공유 생성
        CaseSharing caseSharing = CaseSharing.builder()
                .user(user)
                .part(user.getPart())
                .template(template)
                .caseSharingTitle(requestDTO.getTitle())
                .caseSharingContent(requestDTO.getContent())
                .build();
        caseSharing = caseSharingRepository.save(caseSharing); // 저장하여 caseSharingSeq 생성

        // baseSeq 업데이트
        caseSharing = CaseSharing.createNewCaseSharing(
                user,
                user.getPart(),
                template,
                caseSharing.getCaseSharingSeq(),
                requestDTO.getTitle(),
                requestDTO.getContent()
        );
        caseSharingRepository.save(caseSharing); // 업데이트된 baseSeq 저장

        // 키워드 저장
        if (requestDTO.getKeywords() != null && !requestDTO.getKeywords().isEmpty()) {
            keywordService.saveKeywords(
                    requestDTO.getKeywords(), // 키워드 리스트
                    "CASE_SHARING",          // 게시판 플래그
                    caseSharing.getCaseSharingSeq() // 저장된 케이스 공유 ID
            );
        }
        // 버전 저장
        Version version = Version.builder()
                .caseSharing(caseSharing)
                .versionNum(1) // 새 글은 항상 버전 1
                .versionIsLatest(true) // 최신 버전으로 설정
                .build();
        versionRepository.save(version);

        return caseSharing.getCaseSharingSeq();
    }
    //4. 케이스 공유 수정
    @Transactional
    public Long createNewVersion(Long caseSharingSeq, CaseSharingUpdateRequestDTO requestDTO) {
        // 기존 CaseSharing 조회
        CaseSharing existingCaseSharing = caseSharingRepository.findById(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 검증
        User user = userRepository.findById(requestDTO.getUserSeq())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        if (!existingCaseSharing.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 게시글을 수정할 수 있습니다.");
        }

        // 기존 최신 버전 비활성화
        Version latestVersion = versionRepository.findFirstByCaseSharingCaseSharingSeqAndVersionIsLatestTrue(caseSharingSeq);
        if (latestVersion != null) {
            latestVersion.markAsNotLatest();
            versionRepository.save(latestVersion);
        }

        // 새 케이스 공유 생성 (baseSeq 유지)
        CaseSharing newCaseSharing = CaseSharing.createNewCaseSharing(
                user,
                existingCaseSharing.getPart(),
                existingCaseSharing.getTemplate(),
                existingCaseSharing.getCaseSharingBaseSeq(),
                requestDTO.getTitle(),
                requestDTO.getContent()
        );
        caseSharingRepository.save(newCaseSharing);

        // 새 키워드 저장
        if (requestDTO.getKeywords() != null && !requestDTO.getKeywords().isEmpty()) {
            keywordService.saveKeywords(
                    requestDTO.getKeywords(),
                    "CASE_SHARING",
                    newCaseSharing.getCaseSharingSeq() // 새 CaseSharing의 ID 사용
            );
        }

        // 새 버전 저장
        Version newVersion = Version.builder()
                .caseSharing(newCaseSharing)
                .versionNum(latestVersion.getVersionNum() + 1) // 이전 버전 번호 + 1
                .versionIsLatest(true) // 최신 버전으로 설정
                .build();
        versionRepository.save(newVersion);

        return newCaseSharing.getCaseSharingSeq();
    }


}
