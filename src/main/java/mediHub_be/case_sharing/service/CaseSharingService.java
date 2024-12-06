package mediHub_be.case_sharing.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.BookmarkRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.CaseSharingComment;
import mediHub_be.board.entity.Keyword;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.case_sharing.entity.CaseSharingGroup;
import mediHub_be.case_sharing.repository.CaseSharingCommentRepository;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.case_sharing.repository.TemplateRepository;
import mediHub_be.case_sharing.repository.CaseSharingGroupRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseSharingService {
    private final CaseSharingRepository caseSharingRepository;
    private final CaseSharingGroupRepository caseSharingGroupRepository;
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;
    private final TemplateRepository templateRepository;
    private final KeywordService keywordService;
    private final ViewCountManager viewCountManager;
    private final FlagRepository flagRepository;
    private final BookmarkService bookmarkService;

    private static final String caseSharingBoardFlag = "case_sharing";

    // 1. 케이스 공유 전체(목록) 조회
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getCaseList(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        return caseSharingRepository.findAllLatestVersionsNotDraft().stream()
                .map(caseSharing -> {
                    User author = caseSharing.getUser();

                    return new CaseSharingListDTO(
                            caseSharing.getCaseSharingSeq(),
                            caseSharing.getCaseSharingTitle(),
                            author.getUserName(),
                            author.getRanking().getRankingName(),
                            caseSharing.getCreatedAt(),
                            caseSharing.getCaseSharingViewCount()
                    );
                }).collect(Collectors.toList());
    }


    //2. 케이스 공유 상세 조회
    @Transactional
    public CaseSharingDetailDTO getCaseSharingDetail(Long caseSharingSeq, String userId, HttpServletRequest request, HttpServletResponse response) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        // 게시글 정보 조회
        CaseSharing caseSharing = caseSharingRepository.findById(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 삭제된 게시글인지 확인
        if (caseSharing.getDeletedAt() != null) {
            throw new IllegalArgumentException("삭제된 게시글은 조회할 수 없습니다.");
        }

        // 작성자 정보 조회
        User author = userRepository.findById(caseSharing.getUser().getUserSeq())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        boolean shouldIncrease = viewCountManager.shouldIncreaseViewCount(caseSharingSeq, request, response);
        if (shouldIncrease) {
            log.info("오늘 처음 조회한 게시물");
            caseSharing.increaseViewCount(); // 조회수 증가
            caseSharingRepository.save(caseSharing); // 변경 사항 저장
        }
        else{
            log.info("이미 조회한적 있는 게시물");
        }

        // 키워드 내역 반환
        List<Keyword> keywords = keywordRepository.findByBoardFlagAndPostSeq( caseSharingBoardFlag,caseSharingSeq);
        List<CaseSharingKeywordDTO> keywordDTOs = keywords.stream()
                .map(keyword -> new CaseSharingKeywordDTO(
                        keyword.getKeywordSeq(),
                        keyword.getKeywordName()
                ))
                .toList();
        // DTO 생성
        return CaseSharingDetailDTO.builder()
                .caseSharingSeq(caseSharing.getCaseSharingSeq()) // 게시글 ID
                .caseSharingTitle(caseSharing.getCaseSharingTitle()) // 게시글 제목
                .caseSharingContent(caseSharing.getCaseSharingContent()) // 게시글 내용
                .caseAuthor(author.getUserName()) // 작성자 이름
                .caseAuthorRankName(author.getRanking().getRankingName()) // 작성자 직위명
                .keywords(keywordDTOs) // 키워드 리스트
                .caseSharingGroupSeq(caseSharing.getCaseSharingGroup().getCaseSharingGroupSeq()) // 그룹 ID
                .isLatestVersion(caseSharing.getCaseSharingIsLatest()) // 최신 버전 여부
                .caseSharingViewCount(caseSharing.getCaseSharingViewCount())
                .build();
    }

    //3. 케이스 공유 등록
    @Transactional
    public Long createCaseSharing(CaseSharingCreateRequestDTO requestDTO, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        if (!"진료과".equals(user.getPart().getDept().getDeptName())) {
            throw new IllegalArgumentException("케이스 공유글은 의사만 작성할 수 있습니다.");
        }

        // 템플릿 조회
        Template template = templateRepository.findById(requestDTO.getTemplateSeq())
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿을 찾을 수 없습니다."));

        // 그룹 생성 및 저장
        CaseSharingGroup group = CaseSharingGroup.createNewGroup();
        caseSharingGroupRepository.save(group);

        // 케이스 공유 생성
        CaseSharing caseSharing = CaseSharing.createNewCaseSharing(
                user,
                user.getPart(),
                template,
                group, // 그룹 객체를 직접 전달
                requestDTO.getTitle(),
                requestDTO.getContent(),
                false // 임시 저장 여부 기본값 설정 (예: false)
        );
        caseSharingRepository.save(caseSharing);

        Flag flag = Flag.builder()
                .flagBoardFlag(caseSharingBoardFlag) // 게시판 구분
                .flagPostSeq(caseSharing.getCaseSharingSeq()) // 게시글 ID
                .build();

        flagRepository.save(flag); // Flag 저장

        // 키워드 저장
        if (requestDTO.getKeywords() != null && !requestDTO.getKeywords().isEmpty()) {
            keywordService.saveKeywords(
                    requestDTO.getKeywords(), // 키워드 리스트
                    flag.getFlagSeq()
            );
        }

        return caseSharing.getCaseSharingSeq();
    }
    //4. 케이스 공유 수정
    @Transactional
    public Long createNewVersion(Long caseSharingSeq, CaseSharingUpdateRequestDTO requestDTO, String userId) {
        // 기존 CaseSharing 조회
        CaseSharing existingCaseSharing = caseSharingRepository.findById(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        if (!existingCaseSharing.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 게시글을 수정할 수 있습니다.");
        }

        // 기존 최신 버전 비활성화
        existingCaseSharing.markAsNotLatest();
        caseSharingRepository.save(existingCaseSharing);

        // 새 케이스 공유 생성 (기존 그룹 유지)
        CaseSharing newCaseSharing = CaseSharing.createNewCaseSharing(
                user,
                existingCaseSharing.getPart(),
                existingCaseSharing.getTemplate(),
                existingCaseSharing.getCaseSharingGroup(), // 기존 그룹 유지 (그룹 객체 전달)
                requestDTO.getTitle(),
                requestDTO.getContent(),
                false // 임시 저장 여부 기본값 설정 (예: false)
        );

        newCaseSharing.markAsLatest(); // 새 버전을 최신으로 설정
        caseSharingRepository.save(newCaseSharing);

        // 새 키워드 저장
        Flag flag = Flag.builder()
                .flagBoardFlag(caseSharingBoardFlag) // 게시판 구분
                .flagPostSeq(newCaseSharing.getCaseSharingSeq()) // 게시글 ID
                .build();

        flagRepository.save(flag); // Flag 저장

        // 키워드 저장
        if (requestDTO.getKeywords() != null && !requestDTO.getKeywords().isEmpty()) {
            keywordService.saveKeywords(
                    requestDTO.getKeywords(), // 키워드 리스트
                    flag.getFlagSeq()
            );
        }

        return newCaseSharing.getCaseSharingSeq();
    }

    //5. 케이스 공유 소프트 삭제
    @Transactional
    public void deleteCaseSharing(Long caseSharingSeq, String userId) {

        // 1. 작성자 확인
        CaseSharing caseSharing = caseSharingRepository.findById(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        if (!caseSharing.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 게시글을 삭제할 수 있습니다.");
        }
            // 2. 삭제된 상태 확인
            if (caseSharing.getDeletedAt() != null) {
                throw new IllegalArgumentException("이미 삭제된 게시글입니다.");
            }

            // 3. 그룹 정보 가져오기
            CaseSharingGroup caseSharingGroup = caseSharing.getCaseSharingGroup();
            if (caseSharingGroup == null) {
                throw new IllegalArgumentException("케이스 공유 그룹 정보가 없습니다.");
            }

            // 4. 최신 버전인지 확인 및 처리
            if (caseSharing.getCaseSharingIsLatest()) {
                // 최신 버전 해제
                caseSharing.markAsNotLatest();
                caseSharingRepository.save(caseSharing);

                // 바로 이전 버전을 최신으로 설정
                CaseSharing previousVersion = caseSharingRepository
                        .findTopByCaseSharingGroupCaseSharingGroupSeqAndCaseSharingSeqNotAndIsDraftFalseAndDeletedAtIsNullOrderByCreatedAtDesc(
                                caseSharingGroup.getCaseSharingGroupSeq(),
                                caseSharingSeq
                        ).orElse(null);

                if (previousVersion != null) {
                    previousVersion.markAsLatest();
                    caseSharingRepository.save(previousVersion);
                }

            }
            // 5. 삭제 처리
            caseSharing.markAsDeleted();
            caseSharingRepository.save(caseSharing);
        }


    // 6. 케이스 공유 파트별 조회
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getCasesByPart(Long partSeq,String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        List<CaseSharing> caseSharings = caseSharingRepository.findByPartPartSeqAndCaseSharingIsLatestTrueAndIsDraftFalseAndDeletedAtIsNull(partSeq);

        return caseSharings.stream()
                .map(caseSharing -> {
                    User author = caseSharing.getUser(); // 작성자 정보 조회
                    return new CaseSharingListDTO(
                            caseSharing.getCaseSharingSeq(), // seq값
                            caseSharing.getCaseSharingTitle(), // 제목
                            author.getUserName(), // 작성자
                            author.getRanking().getRankingName(), // 작성자 직위명
                            caseSharing.getCreatedAt(), // 작성 일자
                            caseSharing.getCaseSharingViewCount()
                    );
                })
                .toList();
    }
    //7. 케이스 공유 버전 조회
    @Transactional(readOnly = true)
    public List<CaseSharingVersionListDTO> getCaseVersionList(Long caseSharingSeq, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        CaseSharing caseSharing = caseSharingRepository.findById(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 케이스 공유를 찾을 수 없습니다."));

        List<CaseSharing> caseSharings = caseSharingRepository.findByCaseSharingGroupAndIsDraftFalseAndDeletedAtIsNull(
                caseSharing.getCaseSharingGroup().getCaseSharingGroupSeq()
        );

        return caseSharings.stream()
                .map(cs -> new CaseSharingVersionListDTO(
                        cs.getCaseSharingSeq(),
                        cs.getCaseSharingTitle(),
                        cs.getCreatedAt(),
                        cs.getCaseSharingViewCount()
                ))
                .toList();
    }

    //8. 케이스 공유 임시 저장 등록
    @Transactional
    public Long saveDraft(CaseSharingCreateRequestDTO requestDTO, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        Template template = templateRepository.findById(requestDTO.getTemplateSeq())
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿을 찾을 수 없습니다."));

        CaseSharingGroup group = CaseSharingGroup.createNewGroup();
        caseSharingGroupRepository.save(group);

        CaseSharing caseSharing = CaseSharing.createNewCaseSharing(
                user,
                user.getPart(),
                template,
                group,
                requestDTO.getTitle(),
                requestDTO.getContent(),
                true // 임시 저장 여부 설정
        );
        caseSharingRepository.save(caseSharing);

        // 키워드 저장
        Flag flag = Flag.builder()
                .flagBoardFlag(caseSharingBoardFlag) // 게시판 구분
                .flagPostSeq(caseSharing.getCaseSharingSeq()) // 게시글 ID
                .build();

        flagRepository.save(flag); // Flag 저장

        // 키워드 저장
        if (requestDTO.getKeywords() != null && !requestDTO.getKeywords().isEmpty()) {
            keywordService.saveKeywords(
                    requestDTO.getKeywords(), // 키워드 리스트
                    flag.getFlagSeq()
            );
        }

        return caseSharing.getCaseSharingSeq();
    }

    public List<CaseSharingDraftListDTO> getDraftsByUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        List<CaseSharing> drafts = caseSharingRepository.findByUserUserSeqAndCaseSharingIsDraftTrue(user.getUserSeq());
        return drafts.stream()
                .map(draft -> new CaseSharingDraftListDTO(
                        draft.getCaseSharingSeq(),
                        draft.getCaseSharingTitle(),
                        draft.getCreatedAt()
                ))
                .toList();
    }

    // 9. 임시저장 된 케이스공유 상세 조회(불러오기)
    @Transactional
    public CaseSharingDraftDetailDTO getDraftDetail(Long caseSharingSeq, String userId) {
        // 게시글 정보 조회
        CaseSharing caseSharing = caseSharingRepository.findByCaseSharingSeqAndCaseSharingIsDraftTrue(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 임시 저장 글을 찾을 수 없습니다."));

        if (!Objects.equals(caseSharing.getUser().getUserId(), userId)) {
            throw new IllegalArgumentException("본인이 작성한 임시 저장 글만 조회할 수 있습니다.");
        }

        List<Keyword> keywords = keywordRepository.findByBoardFlagAndPostSeq(caseSharingBoardFlag, caseSharingSeq);
        List<CaseSharingKeywordDTO> keywordDTOs = keywords.stream()
                .map(keyword -> new CaseSharingKeywordDTO(
                        keyword.getKeywordSeq(),
                        keyword.getKeywordName()
                ))
                .toList();

        return CaseSharingDraftDetailDTO.builder()
                .caseSharingSeq(caseSharing.getCaseSharingSeq()) // 게시글 ID
                .caseSharingTitle(caseSharing.getCaseSharingTitle()) // 제목
                .caseSharingContent(caseSharing.getCaseSharingContent()) // 본문 내용
                .keywords(keywordDTOs) // 키워드 리스트
                .caseSharingGroupSeq(caseSharing.getCaseSharingGroup().getCaseSharingGroupSeq()) // 그룹 ID
                .build();
    }

    // 10. 임시 저장된 케이스 공유 수정.
    @Transactional
    public Long updateDraft(Long caseSharingSeq, String userId, CaseSharingDraftUpdateDTO requestDTO) {
        // 1. 해당 임시 저장 데이터 조회
        CaseSharing draft = caseSharingRepository.findByCaseSharingSeqAndCaseSharingIsDraftTrue(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 임시 저장 데이터입니다."));

        // 2. 작성자 검증
        if (!Objects.equals(draft.getUser().getUserId(), userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        // 3. 제목 및 내용 업데이트
        draft.updateContent(requestDTO.getCaseSharingTitle(), requestDTO.getCaseSharingContent());
        caseSharingRepository.save(draft);

        // 4. 키워드 수정
        if (requestDTO.getKeywords() != null) {
            keywordService.updateKeywords(requestDTO.getKeywords(),caseSharingBoardFlag, caseSharingSeq);
        }
        return draft.getCaseSharingSeq();
    }

    @Transactional
    public void deleteDraft(Long caseSharingSeq, String userId) {
        // 1. 해당 임시 저장 데이터 조회
        CaseSharing draft = caseSharingRepository.findByCaseSharingSeqAndCaseSharingIsDraftTrue(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 임시 저장 데이터입니다."));

        // 2. 작성자 검증
        if (!Objects.equals(draft.getUser().getUserId(), userId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        // 3. 키워드 삭제
        caseSharingRepository.delete(draft);
        keywordService.deleteKeywords(caseSharingBoardFlag, caseSharingSeq);

        // 4. 임시 저장 데이터 삭제
        caseSharingRepository.delete(draft);
    }

    // 북마크 설정/해제
    @Transactional
    public boolean toggleBookmark(Long caseSharingSeq, String userId) {
        return bookmarkService.toggleBookmark(caseSharingBoardFlag, caseSharingSeq, userId);
    }

    // 해당 게시글의 북마크 여부 반환
    @Transactional
    public boolean isBookmarked(Long caseSharingSeq, String userId) {
        return bookmarkService.isBookmarked(caseSharingBoardFlag, caseSharingSeq, userId);
    }

}
