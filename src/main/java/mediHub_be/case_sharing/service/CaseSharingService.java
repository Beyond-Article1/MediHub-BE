package mediHub_be.case_sharing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.board.service.PictureService;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.case_sharing.entity.CaseSharingGroup;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.case_sharing.repository.CaseSharingGroupRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import mediHub_be.user.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseSharingService {
    private final CaseSharingRepository caseSharingRepository;
    private final CaseSharingGroupRepository caseSharingGroupRepository;

    private final UserService userService;
    private final PictureService pictureService;
    private final KeywordService keywordService;
    private final ViewCountManager viewCountManager;
    private final BookmarkService bookmarkService;
    private final AmazonS3Service amazonS3Service;
    private final FlagService flagService;
    private final TemplateService templateService;

    private static final String CASE_SHARING_FLAG = "CASE_SHARING";

    // 1. 케이스 공유 전체(목록) 조회
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getCaseList(String userId) {
        userService.findByUserId(userId);
        return caseSharingRepository.findAllLatestVersionsNotDraftAndDeletedAtIsNull()
                .stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }

    //2. 케이스 공유 상세 조회
    @Transactional
    public CaseSharingDetailDTO getCaseSharingDetail(Long caseSharingSeq, String userId, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findByUserId(userId);
        // 게시글 정보 조회
        CaseSharing caseSharing = findCaseSharing(caseSharingSeq);

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
                .createdAt(caseSharing.getCreatedAt())
                .caseSharingGroupSeq(caseSharing.getCaseSharingGroup().getCaseSharingGroupSeq())
                .templateSeq(caseSharing.getTemplate().getTemplateSeq())
                .isLatestVersion(caseSharing.getCaseSharingIsLatest())
                .caseSharingViewCount(caseSharing.getCaseSharingViewCount())
                .caseAuthorUrl(pictureService.getUserProfileUrl(user.getUserSeq()))
                .build();
    }


    //3. 케이스 공유 등록
    @Transactional
    public Long createCaseSharing(CaseSharingCreateRequestDTO requestDTO, List<MultipartFile> images,String userId) {

        User user = userService.findByUserId(userId);
        if(!userService.validateAdmin(user)) {
            validateDoctor(user);
        }

        // 템플릿 조회
        Template template = templateService.getTemplate(requestDTO.getTemplateSeq());
        // 그룹 생성 및 저장
        CaseSharingGroup group = CaseSharingGroup.createNewGroup();
        caseSharingGroupRepository.save(group);

        String content = requestDTO.getContent();

        // 케이스 공유 생성
        CaseSharing caseSharing = CaseSharing.createNewCaseSharing(
                user,
                user.getPart(),
                template,
                group,
                requestDTO.getTitle(),
                null,
                false
        );
        caseSharingRepository.save(caseSharing);
        log.info("호출확인");
        saveKeywordsAndFlag(requestDTO.getKeywords(), caseSharing.getCaseSharingSeq());
        updateContentWithImages(caseSharing,content);
        return caseSharing.getCaseSharingSeq();
    }

    //4. 케이스 공유 수정
    @Transactional
    public Long createNewVersion(Long caseSharingSeq, CaseSharingUpdateRequestDTO requestDTO, List<MultipartFile> images, String userId) {

        User user = userService.findByUserId(userId);
        CaseSharing existingCaseSharing = findCaseSharing(caseSharingSeq);

        if(!userService.validateAdmin(user)) {
            validateAuthor(existingCaseSharing,user);
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
               null,
                false // 임시 저장 여부 기본값 설정 (예: false)
        );

        String content = requestDTO.getContent();

        newCaseSharing.markAsLatest(); // 새 버전을 최신으로 설정
        caseSharingRepository.save(newCaseSharing);

        saveKeywordsAndFlag(requestDTO.getKeywords(), newCaseSharing.getCaseSharingSeq());

        updateContentWithImages(newCaseSharing, content);

        return newCaseSharing.getCaseSharingSeq();
    }

    //5. 케이스 공유 소프트 삭제
    @Transactional
    public void deleteCaseSharing(Long caseSharingSeq, String userId) {
        User user = userService.findByUserId(userId);
        CaseSharing caseSharing = findCaseSharing(caseSharingSeq);

        if(!userService.validateAdmin(user)) {
            validateAuthor(caseSharing,user);
        }

        CaseSharingGroup caseSharingGroup = caseSharing.getCaseSharingGroup();

        // 최신 버전인지 확인 및 처리
        if(caseSharing.getCaseSharingIsLatest()){
            // 최신 버전 해제
            caseSharing.markAsNotLatest();
            caseSharingRepository.save(caseSharing);

            // 바로 이전 버전을 최신으로 설정
            CaseSharing previousVersion = caseSharingRepository
                    .findTopByCaseSharingGroup_CaseSharingGroupSeqAndCaseSharingSeqNotAndCaseSharingIsDraftFalseAndDeletedAtIsNullOrderByCreatedAtDesc(
                            caseSharingGroup.getCaseSharingGroupSeq(),
                            caseSharingSeq
                    ).orElse(null);
            log.info("seq값"+previousVersion.getCaseSharingSeq());
            if (previousVersion != null) {
                previousVersion.markAsLatest();
                caseSharingRepository.save(previousVersion);
            }
        }
        Flag flag = flagService.findFlag(CASE_SHARING_FLAG, caseSharing.getCaseSharingSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        pictureService.deletePictures(flag);
        bookmarkService.deleteBookmarkByFlag(flag);
        caseSharing.markAsDeleted();
        caseSharingRepository.save(caseSharing);
    }


    // 6. 케이스 공유 파트별 조회
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getCasesByPart(Long partSeq,String userId) {
        userService.findByUserId(userId);

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
        userService.findByUserId(userId);
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
        User user = userService.findByUserId(userId);
        Template template = templateService.getTemplate(requestDTO.getTemplateSeq());

        CaseSharingGroup group = CaseSharingGroup.createNewGroup();
        caseSharingGroupRepository.save(group);

        CaseSharing draftCaseSharing = CaseSharing.createNewCaseSharing(
                user,
                user.getPart(),
                template,
                group,
                requestDTO.getTitle(),
                null,
                true // 임시 저장 여부 설정
        );

        String content = requestDTO.getContent();
        caseSharingRepository.save(draftCaseSharing);
        saveKeywordsAndFlag(requestDTO.getKeywords(), draftCaseSharing.getCaseSharingSeq());
        updateContentWithImages(draftCaseSharing, content);
        return draftCaseSharing.getCaseSharingSeq();
    }

    // 9. 특정 유저가 저장한 임시 저장 목록 불러오기
    @Transactional
    public List<CaseSharingDraftListDTO> getDraftsByUser(String userId) {
        User user = userService.findByUserId(userId);
        List<CaseSharing> drafts = caseSharingRepository.findByUserUserSeqAndCaseSharingIsDraftTrueAndDeletedAtIsNull(user.getUserSeq());
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
        User user = userService.findByUserId(userId);
        CaseSharing draft = findDraft(caseSharingSeq);

        validateAuthor(draft,user);

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
        User user = userService.findByUserId(userId);
        CaseSharing draft = findDraft(caseSharingSeq);
        validateAuthor(draft, user);

        // 제목 및 내용 업데이트
        draft.updateContent(requestDTO.getCaseSharingTitle(), null);
        caseSharingRepository.save(draft);
        // 키워드 수정
        Flag flag = flagService.findFlag(CASE_SHARING_FLAG, draft.getCaseSharingSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        pictureService.deletePictures(flag);

        // 4. 새로운 사진 업로드 및 저장
        if (newImages != null && !newImages.isEmpty()) {
            String updatedContent = pictureService.replaceBase64WithUrls(requestDTO.getCaseSharingContent(),flag.getFlagType(), flag.getFlagEntitySeq());
            draft.updateContent(requestDTO.getCaseSharingTitle(), updatedContent);
            caseSharingRepository.save(draft);
        }

        keywordService.updateKeywords(requestDTO.getKeywords(),CASE_SHARING_FLAG, draft.getCaseSharingSeq());
        return draft.getCaseSharingSeq();
    }

    // 12. 임시 저장된 케이스 공유 삭제 (완전 삭제)
    @Transactional
    public void deleteDraft(Long caseSharingSeq, String userId) {
        User user = userService.findByUserId(userId);
        CaseSharing draft = findCaseSharing(caseSharingSeq);
        validateAuthor(draft, user);

        keywordService.deleteKeywords(CASE_SHARING_FLAG, caseSharingSeq);
        List<Picture> existingPictures = pictureService.getPicturesByFlagTypeAndEntitySeqAndIsDeletedIsNotNull(CASE_SHARING_FLAG, draft.getCaseSharingSeq());

        Flag flag = flagService.findFlag(CASE_SHARING_FLAG, caseSharingSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        for (Picture picture : existingPictures) {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl()); // S3에서 삭제
            pictureService.deletePictures(flag);
        }

        caseSharingRepository.delete(draft);
    }

    // 13. 북마크 설정/해제
    @Transactional
    public boolean toggleBookmark(Long caseSharingSeq, String userId) {
        userService.findByUserId(userId);
        return bookmarkService.toggleBookmark(CASE_SHARING_FLAG, caseSharingSeq, userId);
    }

    // 14. 해당 게시글의 북마크 여부 반환
    @Transactional
    public boolean isBookmarked(Long caseSharingSeq, String userId) {
        userService.findByUserId(userId);
        return bookmarkService.isBookmarked(CASE_SHARING_FLAG, caseSharingSeq, userId);
    }

    // 15. 내가 작성한 케이스 공유 리스트 반환
    @Transactional(readOnly = true)
    public List<CaseSharingMyListDTO> getMyCaseList(String userId) {
        User user = userService.findByUserId(userId);
        List<CaseSharing> myCaseSharing =  caseSharingRepository.findByUserUserSeqAndCaseSharingIsDraftFalseAndDeletedAtIsNullAndCaseSharingIsLatestIsTrue(user.getUserSeq());

        return myCaseSharing.stream()
                .map(draft -> new CaseSharingMyListDTO(
                        draft.getCaseSharingSeq(),
                        draft.getCaseSharingTitle(),
                        draft.getCreatedAt(),
                        draft.getCaseSharingViewCount()
                ))
                .toList();

    }

    // 16. 내가 북마크한 케이스 공유 리스트 반환
    @Transactional(readOnly = true)
    public List<CaseSharingListDTO> getBookMarkedCaseList(String userId) {
        User user = userService.findByUserId(userId);
        List<BookmarkDTO> myBookMarks =  bookmarkService.findByUserAndFlagType(user, CASE_SHARING_FLAG);

        List<Long> caseSharingSeqs = myBookMarks.stream()
                .map(bookmarkDTO -> bookmarkDTO.getFlag().getFlagEntitySeq())
                .toList();

        List<CaseSharing> caseSharings = caseSharingRepository.findAllById(caseSharingSeqs);

        return caseSharings.stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());

    }



    private void updateContentWithImages(CaseSharing caseSharing, String content) {
        // Base64 이미지 -> S3 URL 변환
        String updatedContent = pictureService.replaceBase64WithUrls(
                content,
                CASE_SHARING_FLAG,
                caseSharing.getCaseSharingSeq()
        );
        caseSharing.updateContent(caseSharing.getCaseSharingTitle(), updatedContent);
        caseSharingRepository.save(caseSharing);
    }

    private void validateAuthor(CaseSharing caseSharing, User user) {
        if (!caseSharing.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    private void validateDoctor(User user) {
        if (!"진료과".equals(user.getPart().getDept().getDeptName())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    @Transactional
    public CaseSharing findCaseSharing(Long caseSharingSeq) {
        return caseSharingRepository.findById(caseSharingSeq)
                .filter(c -> c.getDeletedAt() == null) // 삭제된 글은 조회 불가
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CASE));

    }

    private CaseSharing findDraft(Long caseSharingSeq) {
        return caseSharingRepository.findByCaseSharingSeqAndCaseSharingIsDraftTrueAndDeletedAtIsNull(caseSharingSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CASE));
    }

    private void saveKeywordsAndFlag(List<String> keywords, Long entitySeq) {
        Flag flag = flagService.createFlag(CASE_SHARING_FLAG, entitySeq);
        log.info("flag정보 " + flag.getFlagSeq() + flag.getFlagEntitySeq());
        if (keywords != null && !keywords.isEmpty()) {
            log.info("키워드"+ keywords.get(0));
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


    @Transactional(readOnly = true)
    public List<CaseSharingMain3DTO> getTop3Cases() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        Pageable top3 = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "caseSharingViewCount"));

        List<CaseSharing> topCases = caseSharingRepository.findTop3ByCreatedAtAfterOrderByCaseSharingViewCountDesc(oneWeekAgo, top3);

        return topCases.stream()
                .map(caseSharing -> {
                    String url = pictureService.getCaseSharingFirstImageUrl(caseSharing.getCaseSharingSeq()); // caseSharingSeq 전달
                    return new CaseSharingMain3DTO(
                            caseSharing.getCaseSharingSeq(),
                            caseSharing.getCaseSharingTitle(),
                            caseSharing.getUser().getUserName(),
                            caseSharing.getPart().getPartName(),
                            caseSharing.getUser().getRanking().getRankingName(),
                            url // 대표 이미지 URL
                    );
                })
                .toList();
    }
}
