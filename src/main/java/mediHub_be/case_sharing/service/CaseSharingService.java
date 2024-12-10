package mediHub_be.case_sharing.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.board.service.PictureService;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.case_sharing.entity.CaseSharingGroup;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.case_sharing.repository.TemplateRepository;
import mediHub_be.case_sharing.repository.CaseSharingGroupRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseSharingService {
    private final CaseSharingRepository caseSharingRepository;
    private final CaseSharingGroupRepository caseSharingGroupRepository;
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;
    private final PictureRepository pictureRepository;
    private final PictureService pictureService;
    private final TemplateRepository templateRepository;
    private final KeywordService keywordService;
    private final ViewCountManager viewCountManager;
    private final FlagRepository flagRepository;
    private final BookmarkService bookmarkService;
    private final AmazonS3Service amazonS3Service;
    private final FlagService flagService;

    private static final String CASE_SHARING_FLAG = "case_sharing";

    // 1. 케이스 공유 전체(목록) 조회
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getCaseList(String userId) {
        findUser(userId);
        return caseSharingRepository.findAllLatestVersionsNotDraft()
                .stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }


    //2. 케이스 공유 상세 조회
    @Transactional
    public CaseSharingDetailDTO getCaseSharingDetail(Long caseSharingSeq, String userId, HttpServletRequest request, HttpServletResponse response) {
        findUser(userId);
        // 게시글 정보 조회
        CaseSharing caseSharing = caseSharingRepository.findById(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        // 삭제된 게시글인지 확인
        validateNotDeleted(caseSharing);

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
        List<CaseSharingKeywordDTO> keywordDTOs = keywordService.getKeywords(CASE_SHARING_FLAG, caseSharingSeq);

        // DTO 생성
        return CaseSharingDetailDTO.builder()
                .caseSharingSeq(caseSharing.getCaseSharingSeq())
                .caseSharingTitle(caseSharing.getCaseSharingTitle())
                .caseSharingContent(caseSharing.getCaseSharingContent())
                .caseAuthor(caseSharing.getUser().getUserName())
                .caseAuthorRankName(caseSharing.getUser().getRanking().getRankingName())
                .keywords(keywordDTOs)
                .caseSharingGroupSeq(caseSharing.getCaseSharingGroup().getCaseSharingGroupSeq())
                .isLatestVersion(caseSharing.getCaseSharingIsLatest())
                .caseSharingViewCount(caseSharing.getCaseSharingViewCount())
                .build();
    }

    //3. 케이스 공유 등록
    @Transactional
    public Long createCaseSharing(CaseSharingCreateRequestDTO requestDTO, List<MultipartFile> images,String userId) {

        User user = findUser(userId);
        validateDoctor(user);

        // 템플릿 조회
        Template template = findTemplate(requestDTO.getTemplateSeq());
        // 그룹 생성 및 저장
        CaseSharingGroup group = CaseSharingGroup.createNewGroup();

        caseSharingGroupRepository.save(group);

        // 케이스 공유 생성
        CaseSharing caseSharing = CaseSharing.createNewCaseSharing(
                user,
                user.getPart(),
                template,
                group,
                requestDTO.getTitle(),
                requestDTO.getContent(),
                false
        );
        caseSharingRepository.save(caseSharing);
        saveKeywordsAndFlag(requestDTO.getKeywords(), caseSharing.getCaseSharingSeq());

        // 이미지 업로드 및 본문 변환 처리
        if (images != null && !images.isEmpty()) {
            String updatedContent = pictureService.replacePlaceHolderWithUrls(
                    requestDTO.getContent(),
                    images,
                    CASE_SHARING_FLAG,
                    caseSharing.getCaseSharingSeq()
            );
            caseSharing.updateContent(requestDTO.getTitle(), updatedContent);
            caseSharingRepository.save(caseSharing);
        }

        return caseSharing.getCaseSharingSeq();
    }

    //4. 케이스 공유 수정
    @Transactional
    public Long createNewVersion(Long caseSharingSeq, CaseSharingUpdateRequestDTO requestDTO, List<MultipartFile> images, String userId) {

        User user = findUser(userId);
        CaseSharing existingCaseSharing = findCaseSharing(caseSharingSeq);
        validateNotDeleted(existingCaseSharing);
        validateAuthor(existingCaseSharing,user);

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

        saveKeywordsAndFlag(requestDTO.getKeywords(), newCaseSharing.getCaseSharingSeq());

        // 이미지 업로드 및 본문 변환 처리
        if (images != null && !images.isEmpty()) {
            String updatedContent = pictureService.replacePlaceHolderWithUrls(
                    requestDTO.getContent(),
                    images,
                    CASE_SHARING_FLAG,
                    newCaseSharing.getCaseSharingSeq()
            );
            newCaseSharing.updateContent(requestDTO.getTitle(), updatedContent);
            caseSharingRepository.save(newCaseSharing);
        }

        return newCaseSharing.getCaseSharingSeq();
    }

    //5. 케이스 공유 소프트 삭제
    @Transactional
    public void deleteCaseSharing(Long caseSharingSeq, String userId) {
        User user = findUser(userId);
        CaseSharing caseSharing = findCaseSharing(caseSharingSeq);
        validateNotDeleted(caseSharing);
        validateAuthor(caseSharing,user);

        CaseSharingGroup caseSharingGroup = caseSharing.getCaseSharingGroup();

        // 최신 버전인지 확인 및 처리
        if(caseSharing.getCaseSharingIsLatest()){
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
        keywordService.deleteKeywords(CASE_SHARING_FLAG, caseSharing.getCaseSharingSeq());

        List<Picture> pictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(CASE_SHARING_FLAG, caseSharingSeq);
        for (Picture picture : pictures) {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl()); // S3에서 이미지 삭제
            pictureRepository.delete(picture); // Picture 엔티티 삭제
        }

        caseSharing.markAsDeleted();
        caseSharingRepository.save(caseSharing);
    }


    // 6. 케이스 공유 파트별 조회
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getCasesByPart(Long partSeq,String userId) {
        findUser(userId);

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
        findUser(userId);
        CaseSharing caseSharing = findCaseSharing(caseSharingSeq);
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
    public Long saveDraft(CaseSharingCreateRequestDTO requestDTO, List<MultipartFile> images, String userId) {
        User user = findUser(userId);
        Template template = findTemplate(requestDTO.getTemplateSeq());

        CaseSharingGroup group = CaseSharingGroup.createNewGroup();
        caseSharingGroupRepository.save(group);

        CaseSharing draftCaseSharing = CaseSharing.createNewCaseSharing(
                user,
                user.getPart(),
                template,
                group,
                requestDTO.getTitle(),
                requestDTO.getContent(),
                true // 임시 저장 여부 설정
        );
        caseSharingRepository.save(draftCaseSharing);
        saveKeywordsAndFlag(requestDTO.getKeywords(), draftCaseSharing.getCaseSharingSeq());

        if (images != null && !images.isEmpty()) {
            String updatedContent = pictureService.replacePlaceHolderWithUrls(
                    requestDTO.getContent(),
                    images,
                    CASE_SHARING_FLAG,
                    draftCaseSharing.getCaseSharingSeq()
            );
            draftCaseSharing.updateContent(requestDTO.getTitle(), updatedContent);
            caseSharingRepository.save(draftCaseSharing);
        }

        return draftCaseSharing.getCaseSharingSeq();
    }

    // 9. 특정 유저가 저장한 임시 저장 목록 불러오기
    @Transactional
    public List<CaseSharingDraftListDTO> getDraftsByUser(String userId) {
        User user = findUser(userId);
        List<CaseSharing> drafts = caseSharingRepository.findByUserUserSeqAndCaseSharingIsDraftTrue(user.getUserSeq());
        return drafts.stream()
                .map(draft -> new CaseSharingDraftListDTO(
                        draft.getCaseSharingSeq(),
                        draft.getCaseSharingTitle(),
                        draft.getCreatedAt()
                ))
                .toList();
    }

    // 10. 임시저장 된 케이스공유 상세 조회(불러오기)
    @Transactional
    public CaseSharingDraftDetailDTO getDraftDetail(Long caseSharingSeq, String userId) {
        User user = findUser(userId);
        CaseSharing draft = findDraft(caseSharingSeq);
        validateAuthor(draft,user);
        validateNotDeleted(draft);

        List<CaseSharingKeywordDTO> keywordDTOs = keywordService.getKeywords(CASE_SHARING_FLAG, draft.getCaseSharingSeq());

        return CaseSharingDraftDetailDTO.builder()
                .caseSharingSeq(draft.getCaseSharingSeq()) // 게시글 ID
                .caseSharingTitle(draft.getCaseSharingTitle()) // 제목
                .caseSharingContent(draft.getCaseSharingContent()) // 본문 내용
                .keywords(keywordDTOs) // 키워드 리스트
                .caseSharingGroupSeq(draft.getCaseSharingGroup().getCaseSharingGroupSeq()) // 그룹 ID
                .build();
    }

    // 11. 임시 저장된 케이스 공유 수정.
    @Transactional
    public Long updateDraft(Long caseSharingSeq, String userId, List<MultipartFile> newImages, CaseSharingDraftUpdateDTO requestDTO) {
        User user = findUser(userId);
        CaseSharing draft = findDraft(caseSharingSeq);
        validateAuthor(draft, user);

        // 제목 및 내용 업데이트
        draft.updateContent(requestDTO.getCaseSharingTitle(), requestDTO.getCaseSharingContent());
        caseSharingRepository.save(draft);
        log.info("젬목, 내용 업데이트");
        // 키워드 수정
        Flag flag = flagService.findFlag(CASE_SHARING_FLAG, draft.getCaseSharingSeq())
                .orElseThrow(() -> new IllegalArgumentException("해당 Flag가 존재하지 않습니다."));

        List<Picture> pictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq (CASE_SHARING_FLAG, caseSharingSeq);
        for (Picture picture : pictures) {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl()); // S3에서 이미지 삭제
            pictureRepository.delete(picture); // Picture 엔티티 삭제
        }
        log.info("사진 삭제");

        // 4. 새로운 사진 업로드 및 저장
        if (newImages != null && !newImages.isEmpty()) {
            String updatedContent = pictureService.replacePlaceHolderWithUrls(requestDTO.getCaseSharingContent(), newImages, flag.getFlagType(), flag.getFlagEntitySeq());
            draft.updateContent(requestDTO.getCaseSharingTitle(), updatedContent);
            caseSharingRepository.save(draft);
        }

        keywordService.updateKeywords(requestDTO.getKeywords(),CASE_SHARING_FLAG, draft.getCaseSharingSeq());
        return draft.getCaseSharingSeq();
    }

    // 12. 임시 저장된 케이스 공유 삭제 (완전 삭제)
    @Transactional
    public void deleteDraft(Long caseSharingSeq, String userId) {
        User user = findUser(userId);
        CaseSharing draft = findCaseSharing(caseSharingSeq);
        validateNotDeleted(draft);
        validateAuthor(draft, user);

        keywordService.deleteKeywords(CASE_SHARING_FLAG, caseSharingSeq);

        List<Picture> existingPictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(CASE_SHARING_FLAG, caseSharingSeq);
        for (Picture picture : existingPictures) {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl()); // S3에서 삭제
            pictureRepository.delete(picture); // 엔티티 삭제
        }

        caseSharingRepository.delete(draft);
    }

    // 13. 북마크 설정/해제
    @Transactional
    public boolean toggleBookmark(Long caseSharingSeq, String userId) {
        return bookmarkService.toggleBookmark(CASE_SHARING_FLAG, caseSharingSeq, userId);
    }

    // 14. 해당 게시글의 북마크 여부 반환
    @Transactional
    public boolean isBookmarked(Long caseSharingSeq, String userId) {
        return bookmarkService.isBookmarked(CASE_SHARING_FLAG, caseSharingSeq, userId);
    }

    //예외 메소드

    private User findUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));
    }


    private void validateAuthor(CaseSharing caseSharing, User user) {
        if (!caseSharing.getUser().equals(user)) {
            throw new IllegalArgumentException("작성자만 해당 작업을 수행할 수 있습니다.");
        }
    }

    private void validateNotDeleted(CaseSharing caseSharing) {
        if (caseSharing.getDeletedAt() != null) {
            throw new IllegalArgumentException("삭제된 게시글은 조회할 수 없습니다.");
        }
    }

    private void validateDoctor(User user) {
        if (!"진료과".equals(user.getPart().getDept().getDeptName())) {
            throw new IllegalArgumentException("케이스 공유글은 의사만 작성할 수 있습니다.");
        }
    }

    private CaseSharing findCaseSharing(Long caseSharingSeq) {
        return caseSharingRepository.findById(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("케이스 공유글을 찾을 수 없습니다."));

    }

    private CaseSharing findDraft(Long caseSharingSeq) {
        return caseSharingRepository.findByCaseSharingSeqAndCaseSharingIsDraftTrue(caseSharingSeq)
                .orElseThrow(() -> new IllegalArgumentException("임시 저장 글을 찾을 수 없습니다."));
    }

    private Template findTemplate(Long templateSeq) {
        return templateRepository.findById(templateSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿을 찾을 수 없습니다."));
    }

    private void saveKeywordsAndFlag(List<String> keywords, Long entitySeq) {
        if (keywords != null && !keywords.isEmpty()) {
            Flag flag = Flag.builder()
                    .flagType(CASE_SHARING_FLAG)
                    .flagEntitySeq(entitySeq)
                    .build();
            flagRepository.save(flag);
            keywordService.saveKeywords(keywords, flag.getFlagSeq());
        }
    }

    // list dto 헬퍼
    private CaseSharingListDTO toListDTO(CaseSharing caseSharing) {
        User author = caseSharing.getUser();
        return new CaseSharingListDTO(
                caseSharing.getCaseSharingSeq(),
                caseSharing.getCaseSharingTitle(),
                author.getUserName(),
                author.getRanking().getRankingName(),
                caseSharing.getCreatedAt(),
                caseSharing.getCaseSharingViewCount()
        );
    }
}
