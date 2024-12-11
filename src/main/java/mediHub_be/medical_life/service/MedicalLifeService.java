package mediHub_be.medical_life.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.entity.Comment;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.CommentRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.board.service.PreferService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.dept.entity.Dept;
import mediHub_be.medical_life.dto.*;
import mediHub_be.medical_life.entity.MedicalLife;
import mediHub_be.medical_life.repository.MedicalLifeRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalLifeService {

    private final MedicalLifeRepository medicalLifeRepository;
    private final UserRepository userRepository;
    private final FlagRepository flagRepository;
    private final PictureRepository pictureRepository;
    private final ViewCountManager viewCountManager;
    private final KeywordRepository keywordRepository;
    private final KeywordService keywordService;
    private final AmazonS3Service amazonS3Service;
    private final CommentRepository commentRepository;
    private final BookmarkService bookmarkService;
    private final PreferService preferService;

    // 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<MedicalLifeDTO> getMedicalLifeListByUsername(String userId, DeptPartFilterDTO filterDTO) {
        // 유저 확인
        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        return medicalLifeRepository.findAllByDeptAndPart(filterDTO.getDeptSeq(), filterDTO.getPartSeq())
                .stream()
                .map(medicalLife -> {
                    // Flag 조회
                    Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("MEDICAL_LIFE", medicalLife.getMedicalLifeSeq())
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

                    // 사진 목록 조회
                    List<MedicalLifePictureDTO> pictures = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())
                            .stream()
                            .map(picture -> MedicalLifePictureDTO.builder()
                                    .pictureSeq(picture.getPictureSeq())
                                    .pictureName(picture.getPictureName())
                                    .pictureUrl(picture.getPictureUrl())
                                    .pictureType(picture.getPictureType())
                                    .pictureIsDeleted(false)
                                    .build())
                            .collect(Collectors.toList());

                    // DTO 매핑
                    return MedicalLifeDTO.builder()
                            .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                            .userName(medicalLife.getUser().getUserName())
                            .deptName(medicalLife.getDept().getDeptName())
                            .partName(medicalLife.getPart().getPartName())
                            .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                            .medicalLifeContent(medicalLife.getMedicalLifeContent())
                            .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                            .pictures(pictures)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    @Transactional
    public MedicalLifeDetailDTO getMedicalLifeDetail(Long medicalLifeSeq, String userId, HttpServletRequest request, HttpServletResponse response) {
        // 유저 로그인 확인
        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 게시글 조회
        MedicalLife medicalLife = medicalLifeRepository.findById(medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE));

        // 조회수 증가 처리
        if (viewCountManager.shouldIncreaseViewCount(medicalLifeSeq, request, response)) {
            medicalLife.increaseViewCount();
            medicalLifeRepository.save(medicalLife);
        }

        // Flag 조회
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("MEDICAL_LIFE", medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 사진 조회
        List<MedicalLifePictureDTO> pictures = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())
                .stream()
                .map(picture -> MedicalLifePictureDTO.builder()
                        .pictureSeq(picture.getPictureSeq())
                        .pictureName(picture.getPictureName())
                        .pictureUrl(picture.getPictureUrl())
                        .pictureType(picture.getPictureType())
                        .pictureIsDeleted(false)
                        .build())
                .collect(Collectors.toList());

        // 작성자 정보
        User author = medicalLife.getUser();

        // 댓글 조회
        List<MedicalLifeCommentDTO> comments = commentRepository.findByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq())
                .stream()
                .map(comment -> MedicalLifeCommentDTO.builder()
                        .commentSeq(comment.getCommentSeq())
                        .userName(comment.getUser().getUserName())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // 키워드 조회
        List<MedicalLifeKeywordDTO> keywords = keywordRepository.findByFlagTypeAndEntitySeq("MEDICAL_LIFE", medicalLifeSeq)
                .stream()
                .map(keyword -> MedicalLifeKeywordDTO.builder()
                        .keywordSeq(keyword.getKeywordSeq())
                        .keywordName(keyword.getKeywordName())
                        .build())
                .collect(Collectors.toList());

        return MedicalLifeDetailDTO.builder()
                .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                .userName(author.getUserName())
                .deptName(medicalLife.getDept().getDeptName())
                .partName(medicalLife.getPart().getPartName())
                .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                .medicalLifeContent(medicalLife.getMedicalLifeContent())
                .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                .createdAt(medicalLife.getCreatedAt())
                .pictures(pictures)
                .comments(comments)
                .keywords(keywords)
                .build();
    }

    // 게시글 등록
    @Transactional
    public Long createMedicalLife(String title, String content, List<MultipartFile> imageList, String userId) throws IOException {
        // 로그인된 회원 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));


        Dept dept = user.getPart().getDept();
        if (dept == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE);
        }

        // 게시글 생성
        MedicalLife medicalLife = MedicalLife.builder()
                .user(user)
                .dept(dept)
                .part(user.getPart())
                .medicalLifeTitle(title)
                .medicalLifeContent(content)
                .build();

        medicalLifeRepository.save(medicalLife);

        // Flag 생성
        Flag flag = Flag.builder()
                .flagType("MEDICAL_LIFE")
                .flagEntitySeq(medicalLife.getMedicalLifeSeq())
                .build();
        flagRepository.save(flag);

        // 이미지 저장
        if (imageList != null && !imageList.isEmpty()) {
            for (MultipartFile image : imageList) {
                AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);
                Picture picture = Picture.builder()
                        .flag(flag)
                        .pictureName(metaData.getOriginalFileName())
                        .pictureUrl(metaData.getUrl())
                        .pictureType(metaData.getType())
                        .pictureIsDeleted(false)
                        .build();
                pictureRepository.save(picture);
            }
        }

        return medicalLife.getMedicalLifeSeq();
    }

    // 게시글 수정
    @Transactional
    public void updateMedicalLife(Long medicalLifeSeq, String title, String content, List<MultipartFile> imageList, String userId) throws IOException {
        // 게시글 조회
        MedicalLife medicalLife = medicalLifeRepository.findById(medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE));

        // 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 게시글 작성자 확인
        if (!medicalLife.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 게시글 내용 업데이트
        medicalLife.update(title, content);

        // Flag 조회
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("MEDICAL_LIFE", medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 기존 이미지 삭제 및 새로운 이미지 추가
        if (imageList != null && !imageList.isEmpty()) {

            // 기존 이미지 삭제
            List<Picture> existingPictures = pictureRepository.findAllByFlag_FlagSeq(flag.getFlagSeq());
            for (Picture picture : existingPictures) {
                amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
                picture.setDeleted();
                pictureRepository.save(picture);
            }

            // 새로운 이미지 추가
            for (MultipartFile image : imageList) {
                AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);
                Picture newPicture = Picture.builder()
                        .flag(flag)
                        .pictureName(metaData.getOriginalFileName())
                        .pictureUrl(metaData.getUrl())
                        .pictureType(metaData.getType())
                        .pictureIsDeleted(false)
                        .build();
                pictureRepository.save(newPicture);
            }
        }
    }


    // 게시글 삭제
    @Transactional
    public void deleteMedicalLife(Long medicalLifeSeq, String userId) {

        // 게시글 조회
        MedicalLife medicalLife = medicalLifeRepository.findById(medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE));

        // 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 게시글 작성자 확인
        if (!medicalLife.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 게시글 삭제 (소프트 딜리트)
        medicalLife.setDeleted();
        medicalLifeRepository.save(medicalLife);
    }


    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<MedicalLifeCommentDTO> getMedicalLifeComments(Long medicalLifeSeq) {
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("MEDICAL_LIFE", medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        return commentRepository.findByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq())
                .stream()
                .map(comment -> MedicalLifeCommentDTO.builder()
                        .commentSeq(comment.getCommentSeq())
                        .userName(comment.getUser().getUserName())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 댓글 등록
    @Transactional
    public Long createMedicalLifeComment(Long medicalLifeSeq, String commentContent, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("MEDICAL_LIFE", medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        Comment comment = Comment.createNewComment(user, flag, commentContent);
        commentRepository.save(comment);

        return comment.getCommentSeq();
    }

    // 댓글 수정
    @Transactional
    public void updateMedicalLifeComment(Long commentSeq, String commentContent, String userId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        // 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 댓글 작성자 확인
        if (!comment.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 댓글 업데이트
        comment.update(user, comment.getFlag(), commentContent);
        commentRepository.save(comment);
    }


    // 댓글 삭제
    @Transactional
    public void deleteMedicalLifeComment(Long commentSeq, String userId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        // 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 댓글 작성자 확인
        if (!comment.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 댓글 삭제 (소프트 딜리트)
        comment.setDeleted();
        commentRepository.save(comment);
    }


    // 북마크 토글
    @Transactional
    public boolean toggleBookmark(Long medicalLifeSeq, String userId) {
        return bookmarkService.toggleBookmark("MEDICAL_LIFE", medicalLifeSeq, userId);
    }

    // 북마크 여부 확인
    @Transactional(readOnly = true)
    public boolean isBookmarked(Long medicalLifeSeq, String userId) {
        return bookmarkService.isBookmarked("MEDICAL_LIFE", medicalLifeSeq, userId);
    }

    // 좋아요 토글
    @Transactional
    public boolean togglePrefer(Long medicalLifeSeq, String userId) {
        return preferService.togglePrefer("MEDICAL_LIFE", medicalLifeSeq, userId);
    }

    // 좋아요 여부 확인
    @Transactional(readOnly = true)
    public boolean isPreferred(Long medicalLifeSeq, String userId) {
        return preferService.isPreferred("MEDICAL_LIFE", medicalLifeSeq, userId);
    }

}
