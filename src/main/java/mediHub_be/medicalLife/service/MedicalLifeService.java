package mediHub_be.medicalLife.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Comment;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.CommentRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.board.service.*;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.dept.entity.Dept;
import mediHub_be.medicalLife.dto.*;
import mediHub_be.medicalLife.entity.MedicalLife;
import mediHub_be.medicalLife.repository.MedicalLifeRepository;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalLifeService {

    private final MedicalLifeRepository medicalLifeRepository;
    private final UserRepository userRepository;
    private final FlagRepository flagRepository;
    private final KeywordRepository keywordRepository;
    private final CommentRepository commentRepository;
    private final ViewCountManager viewCountManager;
    private final FlagService flagService;
    private final KeywordService keywordService;
    private final PictureService pictureService;
    private final PictureRepository pictureRepository;
    private final AmazonS3Service amazonS3Service;
    private final BookmarkService bookmarkService;
    private final PreferService preferService;
    private final RankingRepository rankingRepository;;


    private static final String MEDICAL_LIFE_FLAG = "MEDICAL_LIFE";

    // 전체 게시물 조회
    @Transactional(readOnly = true)
    public List<MedicalLifeListDTO> getMedicalLifeList(Long userSeq) {

        userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 플래그 데이터 가져오기
        List<MedicalLifeFlagDTO> medicalLifeFlagDTOList = flagRepository.findAll().stream()
                .map(flag -> MedicalLifeFlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagType(flag.getFlagType())
                        .flagEntitySeq(flag.getFlagEntitySeq())
                        .build())
                .toList();

        List<MedicalLifeKeywordDTO> medicalLifeKeywordDTOList = keywordRepository.findAll().stream()
                .map(keyword -> MedicalLifeKeywordDTO.builder()
                        .keywordSeq(keyword.getKeywordSeq())
                        .flagSeq(keyword.getFlagSeq())
                        .keywordName(keyword.getKeywordName())
                        .build())
                .toList();


        List<Ranking> rankings = rankingRepository.findAll();

        return medicalLifeRepository.findAllByMedicalLifeIsDeletedFalse().stream()
                .map(medicalLife -> {
                    List<String> keywordsForMedicalLife = medicalLifeKeywordDTOList.stream()
                            .filter(keyword -> medicalLifeFlagDTOList.stream()
                                    .filter(flag -> flag.getFlagType().equals(MEDICAL_LIFE_FLAG))
                                    .filter(flag -> flag.getFlagEntitySeq().equals(medicalLife.getMedicalLifeSeq()))
                                    .anyMatch(flag -> Objects.equals(flag.getFlagSeq(), keyword.getFlagSeq())))
                            .map(MedicalLifeKeywordDTO::getKeywordName)
                            .toList();

                    Dept dept = medicalLife.getUser().getPart().getDept();
                    Part part = medicalLife.getUser().getPart();

                    String rankingName = rankings.stream()
                            .filter(r -> r.getDeptSeq() == dept.getDeptSeq())
                            .map(Ranking::getRankingName)
                            .findFirst()
                            .orElse("N/A");

                    return MedicalLifeListDTO.builder()
                            .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                            .userSeq(medicalLife.getUser().getUserSeq())
                            .userName(medicalLife.getUser().getUserName())
                            .PartSeq(part.getPartName())
                            .DeptSeq(dept.getDeptName())
                            .medicalLifeName(medicalLife.getMedicalLifeTitle())
                            .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                            .medicalLifeContent(medicalLife.getMedicalLifeContent())
                            .medicalLifeIsDeleted(medicalLife.getMedicalLifeIsDeleted())
                            .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                            .createdAt(medicalLife.getCreatedAt())
                            .keywords(keywordsForMedicalLife)
                            .rankingName(rankingName)
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 메디컬 라이프 상세 조회
    @Transactional
    public MedicalLifeDetailDTO getMedicalLifeDetail(
            Long medicalLifeSeq,
            Long userSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 유저 존재 확인
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 메디컬 라이프 게시글 확인
        MedicalLife medicalLife = medicalLifeRepository.findByMedicalLifeSeqAndMedicalLifeIsDeletedFalse(medicalLifeSeq);
        if (medicalLife == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE);
        }

        // 조회수 증가 여부 확인
        boolean shouldIncrease = viewCountManager.shouldIncreaseViewCount(medicalLifeSeq, request, response);
        if (shouldIncrease) {
            log.info("오늘 처음 조회한 메디컬 라이프 게시글");
            medicalLife.increaseViewCount();
            medicalLifeRepository.save(medicalLife);
        } else {
            log.info("이미 조회한 적 있는 메디컬 라이프 게시글");
        }

        List<Keyword> keywordList = keywordRepository.findByFlagTypeAndEntitySeq(
                MEDICAL_LIFE_FLAG,
                medicalLifeSeq
        );

        List<MedicalLifeKeywordDTO> medicalLifeKeywordDTOList = keywordList.stream()
                .map(keyword -> MedicalLifeKeywordDTO.builder()
                        .keywordSeq(keyword.getKeywordSeq())
                        .flagSeq(keyword.getFlagSeq())
                        .keywordName(keyword.getKeywordName())
                        .build())
                .toList();

        String rankingName = medicalLife.getUser().getRanking() != null
                ? medicalLife.getUser().getRanking().getRankingName()
                : "N/A";

        return MedicalLifeDetailDTO.builder()
                .userSeq(medicalLife.getUser().getUserSeq())
                .userName(medicalLife.getUser().getUserName())
                .rankingName(rankingName)
                .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                .medicalLifeContent(medicalLife.getMedicalLifeContent())
                .medicalLifeViewCount(String.valueOf(medicalLife.getMedicalLifeViewCount()))
                .createdAt(medicalLife.getCreatedAt())
                .keywords(medicalLifeKeywordDTOList)
                .build();
    }



    // 댓글 조회
    @Transactional(readOnly = true)
    public List<MedicalLifeCommentListDTO> getMedicalLifeCommentList(Long medicalLifeSeq, Long userSeq) {
        userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        Flag flag = flagService.findFlag(MEDICAL_LIFE_FLAG, medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        return commentRepository.findByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq()).stream()
                .map(comment -> {
                    User commentUser = comment.getUser(); // 댓글 작성자 정보

                    String partName = (commentUser.getPart() != null) ? commentUser.getPart().getPartName() : "정보 없음";
                    String rankingName = (commentUser.getRanking() != null) ? commentUser.getRanking().getRankingName() : "정보 없음";

                    return MedicalLifeCommentListDTO.builder()
                            .userName(commentUser.getUserName())
                            .part(partName)
                            .rankingName(rankingName)
                            .commentContent(comment.getCommentContent())
                            .createdAt(comment.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }



    // 메디컬 라이프 게시글 생성
    @Transactional
    public Long createMedicalLife(
            MedicalLifeCreateRequestDTO medicalLifeCreateRequestDTO,
            List<MultipartFile> pictureList,
            Long userSeq
    ) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        String medicalLifeContent = medicalLifeCreateRequestDTO.getMedicalLifeContent();

        MedicalLife medicalLife = MedicalLife.builder()
                .user(user)
                .medicalLifeTitle(medicalLifeCreateRequestDTO.getMedicalLifeTitle())
                .medicalLifeContent(medicalLifeContent)
                .medicalLifeIsDeleted(false)
                .medicalLifeViewCount(0L)
                .build();

        medicalLifeRepository.save(medicalLife);

        // 키워드와 플래그 저장
        saveKeywordsAndFlag(medicalLifeCreateRequestDTO.getKeywords(), medicalLife.getMedicalLifeSeq());

        // 이미지 처리
        updateMedicalLifeContentWithImages(medicalLife, medicalLifeContent);

        return medicalLife.getMedicalLifeSeq();
    }

    // 댓글 생성
    @Transactional
    public Long createMedicalLifeComment(
            Long medicalLifeSeq,
            MedicalLifeCommentRequestDTO medicalLifeCommentRequestDTO,
            Long userSeq
    ) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        Flag flag = flagService.findFlag(MEDICAL_LIFE_FLAG, medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 댓글 생성
        Comment comment = Comment.createNewComment(
                user,
                flag,
                medicalLifeCommentRequestDTO.getCommentContent()
        );

        // 댓글 저장
        commentRepository.save(comment);

        return comment.getCommentSeq();
    }

    // 게시글 수정
    @Transactional
    public Long updateMedicalLife(
            Long medicalLifeSeq,
            MedicalLifeUpdateRequestDTO medicalLifeUpdateRequestDTO,
            List<MultipartFile> newImageList,
            Long userSeq
    ) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        MedicalLife medicalLife = medicalLifeRepository
                .findById(medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE));

        if (!medicalLife.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 기존 이미지 삭제
        Flag flag = flagService.findFlag(MEDICAL_LIFE_FLAG, medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        List<Picture> pictureList = pictureRepository.findAllByFlag_FlagSeqAndPictureIsDeletedFalse(flag.getFlagSeq());
        for (Picture picture : pictureList) {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
            picture.setDeleted();
            pictureRepository.save(picture);
        }

        // 새 이미지 처리 및 업데이트된 내용 저장
        String updatedContent = null;
        if (newImageList != null && !newImageList.isEmpty()) {
            updatedContent = pictureService.replaceBase64WithUrls(
                    medicalLifeUpdateRequestDTO.getMedicalLifeContent(),
                    flag.getFlagType(),
                    flag.getFlagEntitySeq()
            );
        }

        // 게시글 업데이트
        medicalLife.update(
                medicalLifeUpdateRequestDTO.getMedicalLifeTitle(),
                updatedContent
        );
        medicalLifeRepository.save(medicalLife);

        // 키워드 업데이트
        if (medicalLifeUpdateRequestDTO.getKeywords() != null) {
            keywordService.updateKeywords(
                    medicalLifeUpdateRequestDTO.getKeywords(),
                    MEDICAL_LIFE_FLAG,
                    medicalLifeSeq
            );
        }

        return medicalLife.getMedicalLifeSeq();
    }

    // 댓글 수정
    @Transactional
    public Long updateMedicalLifeComment(
            Long medicalLifeSeq,
            Long commentSeq,
            MedicalLifeCommentRequestDTO medicalLifeCommentRequestDTO,
            Long userSeq
    ) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        Comment comment = commentRepository.findByCommentSeqAndCommentIsDeletedFalse(commentSeq);
        if (comment == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_COMMENT);
        }

        if (!comment.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        Flag flag = flagService.findFlag(MEDICAL_LIFE_FLAG, medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        comment.updateContent(flag, medicalLifeCommentRequestDTO.getCommentContent());
        commentRepository.save(comment);

        return comment.getCommentSeq();
    }

    // 메디컬라이프 게시글 삭제
    @Transactional
    public boolean deleteMedicalLife(Long medicalLifeSeq, Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        MedicalLife medicalLife = medicalLifeRepository
                .findByMedicalLifeSeqAndMedicalLifeIsDeletedFalse(medicalLifeSeq);

        // 권한 확인 (작성자 또는 ADMIN만 삭제 가능)
        if (!Objects.equals(user.getUserSeq(), SecurityUtil.getCurrentUserSeq())) {
            if (!SecurityUtil.getCurrentUserAuthorities().equals("ADMIN")) return false;
        }

        if (medicalLife.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE);
        }

        Flag flag = flagService.findFlag(MEDICAL_LIFE_FLAG, medicalLife.getMedicalLifeSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 관련 이미지 삭제
        List<Picture> pictureList = pictureRepository.findAllByFlag_FlagSeqAndPictureIsDeletedFalse(flag.getFlagSeq());
        for (Picture picture : pictureList) {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
            picture.setDeleted();
            pictureRepository.save(picture);
        }

        // 관련 키워드 삭제
        List<Keyword> keywordList = keywordRepository.findByFlagTypeAndEntitySeq(MEDICAL_LIFE_FLAG, flag.getFlagSeq());
        keywordRepository.deleteAll(keywordList);

        // 관련 북마크 및 선호도 삭제
        bookmarkService.deleteBookmarkByFlag(flag);
        preferService.deletePreferByFlag(flag);

        // 관련 댓글 삭제 처리
        List<Comment> commentList = commentRepository.findAllByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq());
        for (Comment comment : commentList) {
            comment.setDeleted();
            commentRepository.save(comment);
        }

        // 게시글 삭제 처리
        medicalLife.setDeleted();
        medicalLifeRepository.save(medicalLife);

        return true;
    }

    // 댓글 삭제
    @Transactional
    public boolean deleteMedicalLifeComment(Long medicalLifeSeq, Long commentSeq, Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        Comment comment = commentRepository.findByCommentSeqAndCommentIsDeletedFalse(commentSeq);
        if (comment == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_COMMENT);
        }

        if (!Objects.equals(user.getUserSeq(), comment.getUser().getUserSeq())) {
            if (!SecurityUtil.getCurrentUserAuthorities().equals("ADMIN")) {
                return false;
            }
        }

        if (comment.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.CANNOT_DELETE_DATA_COMMENT);
        }

        // 댓글 소프트 삭제
        comment.setDeleted();
        commentRepository.save(comment);

        return true;
    }

    // 북마크 등록 해제
    @Transactional
    public boolean isBookmarked(Long medicalLifeSeq, String userId) {
        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        return bookmarkService.isBookmarked(MEDICAL_LIFE_FLAG, medicalLifeSeq, userId);
    }

    // 북마크 여부 확인
    @Transactional
    public boolean toggleBookmark(Long medicalLifeSeq, String userId) {
        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        return bookmarkService.toggleBookmark(MEDICAL_LIFE_FLAG, medicalLifeSeq, userId);
    }

    // 게시글 좋아요
    @Transactional
    public boolean togglePrefer(Long medicalLifeSeq, String userId) {
        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        return preferService.togglePrefer(MEDICAL_LIFE_FLAG, medicalLifeSeq, userId);
    }

    // 게시글 좋아요 여부 확인
    @Transactional
    public boolean isPreferred(Long medicalLifeSeq, String userId) {
        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        return preferService.isPreferred(MEDICAL_LIFE_FLAG, medicalLifeSeq, userId);
    }

    @Transactional(readOnly = true)
    public List<MedicalLifeMyListDTO> getMyMedicalLifeList(Long userSeq) {

        userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        List<MedicalLife> medicalLifeList = medicalLifeRepository.findByUser_UserSeqAndMedicalLifeIsDeletedFalse(userSeq);

        return medicalLifeList.stream()
                .map(medicalLife -> new MedicalLifeMyListDTO(
                        medicalLife.getMedicalLifeSeq(),
                        medicalLife.getMedicalLifeTitle(),
                        medicalLife.getMedicalLifeViewCount(),
                        medicalLife.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MedicalLifeListDTO> getBookMarkedMedicalLifeList(Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        List<BookmarkDTO> bookmarkDTOList = bookmarkService.findByUserAndFlagType(user, MEDICAL_LIFE_FLAG);

        List<Long> medicalLifeSeqList = bookmarkDTOList.stream()
                .map(bookmarkDTO -> bookmarkDTO.getFlag().getFlagEntitySeq())
                .toList();

        List<MedicalLife> medicalLifeList = medicalLifeRepository.findAllById(medicalLifeSeqList);

        return medicalLifeList.stream()
                .map(medicalLife -> MedicalLifeListDTO.builder()
                        .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                        .userSeq(medicalLife.getUser().getUserSeq())
                        .userName(medicalLife.getUser().getUserName())
                        .PartSeq(medicalLife.getUser().getPart().getPartName())
                        .DeptSeq(medicalLife.getUser().getPart().getDept().getDeptName())
                        .medicalLifeName(medicalLife.getMedicalLifeTitle())
                        .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                        .medicalLifeContent(medicalLife.getMedicalLifeContent())
                        .medicalLifeIsDeleted(medicalLife.getMedicalLifeIsDeleted())
                        .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                        .build())
                .toList();
    }

    // top3 조회
    @Transactional(readOnly = true)
    public List<MedicalLifeListDTO> getTop3MedicalLifeByViewCount(Long userSeq) {
        // 유저 로그인 여부 확인
        userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 조회수가 가장 높은 Top 3 게시물 조회
        List<MedicalLife> top3MedicalLifeList = medicalLifeRepository
                .findTop3ByMedicalLifeIsDeletedFalseOrderByMedicalLifeViewCountDesc();

        // DTO 변환
        return top3MedicalLifeList.stream()
                .map(medicalLife -> MedicalLifeListDTO.builder()
                        .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                        .userSeq(medicalLife.getUser().getUserSeq())
                        .userName(medicalLife.getUser().getUserName())
                        .PartSeq(medicalLife.getUser().getPart().getPartName())
                        .DeptSeq(medicalLife.getUser().getPart().getDept().getDeptName())
                        .medicalLifeName(medicalLife.getMedicalLifeTitle())
                        .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                        .medicalLifeContent(medicalLife.getMedicalLifeContent())
                        .medicalLifeIsDeleted(medicalLife.getMedicalLifeIsDeleted())
                        .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                        .build())
                .toList();
    }



    private void saveKeywordsAndFlag(List<String> keywordList, Long medicalLifeSeq) {
        Flag flag = flagService.createFlag(MEDICAL_LIFE_FLAG, medicalLifeSeq);

        if (keywordList != null && !keywordList.isEmpty()) {
            keywordService.saveKeywords(keywordList, flag.getFlagSeq());
        }
    }

    private void updateMedicalLifeContentWithImages(MedicalLife medicalLife, String medicalLifeContent) {
        String updatedMedicalLifeContent = pictureService.replaceBase64WithUrls(
                medicalLifeContent,
                MEDICAL_LIFE_FLAG,
                medicalLife.getMedicalLifeSeq()
        );

        medicalLife.update(
                medicalLife.getMedicalLifeTitle(),
                updatedMedicalLifeContent
        );
    }

}
