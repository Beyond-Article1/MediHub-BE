package mediHub_be.anonymousBoard.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.anonymousBoard.dto.*;
import mediHub_be.anonymousBoard.entity.AnonymousBoard;
import mediHub_be.anonymousBoard.repository.AnonymousBoardRepository;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.entity.Comment;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Keyword;
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
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnonymousBoardService {

    private static final Logger log = LoggerFactory.getLogger(AnonymousBoardService.class);

    private final AnonymousBoardRepository anonymousBoardRepository;
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

    // 익명 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<AnonymousBoardDTO> getAnonymousBoardList(String userId) {

        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 모든 게시판 식별 목록 가져오기
        List<AnonymousBoardFlagDTO> anonymousBoardFlagDTOList = flagRepository.findAll().stream()
                .map(flag -> AnonymousBoardFlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagType(flag.getFlagType())
                        .flagEntitySeq(flag.getFlagEntitySeq())
                        .build())
                .collect(Collectors.toList());
        // 모든 사진 목록 가져오기
        List<AnonymousBoardPictureDTO> anonymousBoardPictureDTOList = pictureRepository.findAll().stream()
                .map(picture -> AnonymousBoardPictureDTO.builder()
                        .pictureSeq(picture.getPictureSeq())
                        .flagSeq(picture.getFlag().getFlagSeq())
                        .pictureName(picture.getPictureName())
                        .pictureChangedName(picture.getPictureChangedName())
                        .pictureUrl(picture.getPictureUrl())
                        .pictureType(picture.getPictureType())
                        .pictureIsDeleted(picture.getPictureIsDeleted())
                        .createdAt(picture.getCreatedAt())
                        .deletedAt(picture.getDeletedAt())
                        .build())
                .collect(Collectors.toList());
        // 익명 게시글 목록 가져오기
        List<AnonymousBoardDTO> anonymousBoardDTOList = anonymousBoardRepository
                .findAllByAnonymousBoardIsDeletedFalse().stream()
                .map(anonymousBoard -> {
                    // 현재 익명 게시글의 SEQ와 일치하는 게시판 식별 번호 목록 필터링
                    List<AnonymousBoardFlagDTO> flagsForAnonymousBoard = anonymousBoardFlagDTOList.stream()
                            .filter(flag -> flag.getFlagType().equals("ANONYMOUS_BOARD"))
                            .filter(flag -> flag.getFlagEntitySeq() == anonymousBoard.getAnonymousBoardSeq())
                            .collect(Collectors.toList());
                    // 현재 익명 게시글의 SEQ와 일치하는 사진 목록 필터링
                    List<AnonymousBoardPictureDTO> imagesForFlag = anonymousBoardPictureDTOList.stream()
                            .filter(picture -> flagsForAnonymousBoard.stream()
                                    .anyMatch(flag -> flag.getFlagSeq() == picture.getFlagSeq()))
                            .collect(Collectors.toList());

                    // 익명 게시글 DTO
                    return AnonymousBoardDTO.builder()
                            .anonymousBoardSeq(anonymousBoard.getAnonymousBoardSeq())
                            .userId(anonymousBoard.getUser().getUserId())
                            .anonymousBoardTitle(anonymousBoard.getAnonymousBoardTitle())
                            .anonymousBoardViewCount(anonymousBoard.getAnonymousBoardViewCount())
                            .createdAt(anonymousBoard.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return anonymousBoardDTOList;
    }

    // 익명 게시글 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<AnonymousBoardCommentDTO> getAnonymousBoardCommentList(Long anonymousBoardSeq, String userId) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        List<AnonymousBoardFlagDTO> anonymousBoardFlagDTOList = flagRepository
                .findAllByFlagEntitySeq(anonymousBoardSeq).stream()
                .map(flag -> AnonymousBoardFlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagType(flag.getFlagType())
                        .flagEntitySeq(flag.getFlagEntitySeq())
                        .build())
                .collect(Collectors.toList());
        AnonymousBoardFlagDTO anonymousBoardFlagDTO = anonymousBoardFlagDTOList.stream()
                .filter(flag -> flag.getFlagType().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));
        List<AnonymousBoardCommentDTO> anonymousBoardCommentDTOList = commentRepository
                .findByFlag_FlagSeqAndCommentIsDeletedFalse(anonymousBoardFlagDTO.getFlagSeq()).stream()
                .map(comment -> AnonymousBoardCommentDTO.builder()
                        .userId(user.getUserId())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return anonymousBoardCommentDTOList;
    }

    // 익명 게시글 조회
    @Transactional
    public AnonymousBoardDetailDTO getAnonymousBoardDetail(
            Long anonymousBoardSeq,
            String userId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        AnonymousBoard anonymousBoard = anonymousBoardRepository.findById(anonymousBoardSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANONYMOUS_BOARD));

        // 삭제된 익명 게시글인지 확인
        if(anonymousBoard.getDeletedAt() != null) throw new CustomException(
                ErrorCode.CANNOT_DELETE_DATA_ANONYMOUS_BOARD
        );

        // 작성자 정보 조회
        User author = userRepository.findById(anonymousBoard.getUser().getUserSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        boolean shouldIncrease = viewCountManager.shouldIncreaseViewCount(anonymousBoardSeq, request, response);

        if(shouldIncrease) {
            log.info("오늘 처음 조회한 익명 게시글");

            anonymousBoard.increaseViewCount();
            anonymousBoardRepository.save(anonymousBoard);
        } else log.info("이미 조회한 적 있는 익명 게시글");

        List<AnonymousBoardFlagDTO> anonymousBoardFlagDTOList = flagRepository
                .findAllByFlagEntitySeq(anonymousBoardSeq).stream()
                .map(flag -> AnonymousBoardFlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagType(flag.getFlagType())
                        .flagEntitySeq(flag.getFlagEntitySeq())
                        .build())
                .collect(Collectors.toList());
        AnonymousBoardFlagDTO anonymousBoardFlagDTO = anonymousBoardFlagDTOList.stream()
                .filter(flag -> flag.getFlagType().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        List<AnonymousBoardPictureDTO> anonymousBoardPictureDTOList = pictureRepository
                .findAllByFlag_FlagSeq(anonymousBoardFlagDTO.getFlagSeq()).stream()
                .map(picture -> AnonymousBoardPictureDTO.builder()
                        .pictureSeq(picture.getPictureSeq())
                        .flagSeq(picture.getFlag().getFlagSeq())
                        .pictureName(picture.getPictureName())
                        .pictureChangedName(picture.getPictureChangedName())
                        .pictureUrl(picture.getPictureUrl())
                        .pictureType(picture.getPictureType())
                        .pictureIsDeleted(picture.getPictureIsDeleted())
                        .createdAt(picture.getCreatedAt())
                        .deletedAt(picture.getDeletedAt())
                        .build())
                .collect(Collectors.toList());

        // 키워드 내역 반환
        List<Keyword> keywords = keywordRepository.findByFlagTypeAndEntitySeq(
                "ANONYMOUS_BOARD",
                anonymousBoardSeq
        );

        List<AnonymousBoardKeywordDTO> anonymousBoardKeywordDTOs = keywords.stream()
                .map(keyword -> new AnonymousBoardKeywordDTO(
                        keyword.getKeywordSeq(),
                        keyword.getKeywordName()
                ))
                .toList();

        return AnonymousBoardDetailDTO.builder()
                .anonymousBoardSeq(anonymousBoard.getAnonymousBoardSeq())
                .userId(author.getUserId())
                .anonymousBoardTitle(anonymousBoard.getAnonymousBoardTitle())
                .anonymousBoardContent(anonymousBoard.getAnonymousBoardContent())
                .anonymousBoardViewCount(anonymousBoard.getAnonymousBoardViewCount())
                .createdAt(anonymousBoard.getCreatedAt())
                .anonymousBoardPictureList(anonymousBoardPictureDTOList)
                .keywords(anonymousBoardKeywordDTOs)
                .build();
    }

    // 익명 게시글 생성
    @Transactional
    public Long createAnonymousBoard(
            AnonymousBoardCreateRequestDTO anonymousBoardCreateRequestDTO,
            List<MultipartFile> imageList,
            String userId
    ) throws IOException {

        // 여기서 userId를 SecurityUtil.getCurrentUserSeq()로 대체
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        if(imageList != null && !imageList.isEmpty()) anonymousBoardCreateRequestDTO.setImageList(imageList);

        AnonymousBoard anonymousBoard = AnonymousBoard.createNewAnonymousBoard(
                user,
                anonymousBoardCreateRequestDTO.getAnonymousBoardTitle(),
                anonymousBoardCreateRequestDTO.getAnonymousBoardContent()
        );

        anonymousBoardRepository.save(anonymousBoard);

        Flag flag = Flag.builder()
                .flagType("ANONYMOUS_BOARD")
                .flagEntitySeq(anonymousBoard.getAnonymousBoardSeq())
                .build();

        flagRepository.save(flag);

        if(anonymousBoardCreateRequestDTO.getImageList() != null &&
                !anonymousBoardCreateRequestDTO.getImageList().isEmpty()
        ) {
            for(MultipartFile image : anonymousBoardCreateRequestDTO.getImageList()) {
                AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);

                AnonymousBoardPictureRequestDTO anonymousBoardPictureRequestDTO = new AnonymousBoardPictureRequestDTO();

                anonymousBoardPictureRequestDTO.setFlagSeq(flag.getFlagSeq());
                anonymousBoardPictureRequestDTO.setPictureName(metaData.getOriginalFileName());
                anonymousBoardPictureRequestDTO.setPictureChangedName(metaData.getChangeFileName());
                anonymousBoardPictureRequestDTO.setPictureUrl(metaData.getUrl());
                anonymousBoardPictureRequestDTO.setPictureType(metaData.getType());

                Picture picture = new Picture();

                picture.create(flag, anonymousBoardPictureRequestDTO);

                pictureRepository.save(picture);
            }
        }

        // 키워드 저장
        if(anonymousBoardCreateRequestDTO.getKeywords() != null &&
                !anonymousBoardCreateRequestDTO.getKeywords().isEmpty()
        ) {
            keywordService.saveKeywords(
                    anonymousBoardCreateRequestDTO.getKeywords(),
                    flag.getFlagSeq()
            );
        }

        return anonymousBoard.getAnonymousBoardSeq();
    }

    // 익명 게시글 댓글 생성
    @Transactional
    public Long createAnonymousBoardComment(
            Long anonymousBoardSeq,
            String commentContent,
            String userId
    ) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        List<AnonymousBoardFlagDTO> anonymousBoardFlagDTOList = flagRepository
                .findAllByFlagEntitySeq(anonymousBoardSeq).stream()
                .map(flag -> AnonymousBoardFlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagType(flag.getFlagType())
                        .flagEntitySeq(flag.getFlagEntitySeq())
                        .build())
                .collect(Collectors.toList());

        AnonymousBoardFlagDTO anonymousBoardFlagDTO = anonymousBoardFlagDTOList.stream()
                .filter(flag -> flag.getFlagType().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        Flag flag = flagRepository.findById(anonymousBoardFlagDTO.getFlagSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        Comment comment = Comment.createNewComment(
                user,
                flag,
                commentContent
        );

        commentRepository.save(comment);

        return comment.getCommentSeq();
    }

    // 익명 게시글 수정
    @Transactional
    public AnonymousBoard updateAnonymousBoard(
            Long anonymousBoardSeq,
            AnonymousBoardUpdateRequestDTO anonymousBoardUpdateRequestDTO,
            List<MultipartFile> imageList,
            String userId
    ) throws IOException {

        AnonymousBoard existingAnonymousBoard = anonymousBoardRepository.findById(anonymousBoardSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANONYMOUS_BOARD));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        if(!existingAnonymousBoard.getUser().equals(user)) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);

        existingAnonymousBoard.update(
                user,
                anonymousBoardUpdateRequestDTO.getAnonymousBoardTitle(),
                anonymousBoardUpdateRequestDTO.getAnonymousBoardContent()
        );

        List<Flag> FlagList = flagRepository.findAllByFlagEntitySeq(anonymousBoardSeq);

        Flag flag = FlagList.stream()
                .filter(f -> f.getFlagType().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElse(null);

        List<AnonymousBoardPictureDTO> anonymousBoardPictureDTOList = pictureRepository
                .findAllByFlag_FlagSeq(flag.getFlagSeq()).stream()
                .map(picture -> AnonymousBoardPictureDTO.builder()
                        .pictureSeq(picture.getPictureSeq())
                        .flagSeq(picture.getFlag().getFlagSeq())
                        .pictureName(picture.getPictureName())
                        .pictureChangedName(picture.getPictureChangedName())
                        .pictureUrl(picture.getPictureUrl())
                        .pictureType(picture.getPictureType())
                        .pictureIsDeleted(picture.getPictureIsDeleted())
                        .createdAt(picture.getCreatedAt())
                        .deletedAt(picture.getDeletedAt())
                        .build())
                .collect(Collectors.toList());

        if(imageList != null && !imageList.isEmpty()) {
            for (AnonymousBoardPictureDTO anonymousBoardPictureDTO : anonymousBoardPictureDTOList) {
                String pictureUrl = anonymousBoardPictureDTO.getPictureUrl();

                amazonS3Service.deleteImageFromS3(pictureUrl);

                Picture picture = pictureRepository.findById(anonymousBoardPictureDTO.getPictureSeq())
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PICTURE));

                picture.setDeleted();

                pictureRepository.save(picture);
            }

            anonymousBoardUpdateRequestDTO.setImageList(imageList);
        }

        AnonymousBoard result = anonymousBoardRepository.save(existingAnonymousBoard);

        if(anonymousBoardUpdateRequestDTO.getImageList() != null &&
                !anonymousBoardUpdateRequestDTO.getImageList().isEmpty()
        ) {
            for(MultipartFile image : anonymousBoardUpdateRequestDTO.getImageList()) {
                AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);

                log.info("업로드 성공");

                AnonymousBoardPictureRequestDTO anonymousBoardPictureRequestDTO = new AnonymousBoardPictureRequestDTO();

                anonymousBoardPictureRequestDTO.setFlagSeq(flag.getFlagSeq());
                anonymousBoardPictureRequestDTO.setPictureName(metaData.getOriginalFileName());
                anonymousBoardPictureRequestDTO.setPictureChangedName(metaData.getChangeFileName());
                anonymousBoardPictureRequestDTO.setPictureUrl(metaData.getUrl());
                anonymousBoardPictureRequestDTO.setPictureType(metaData.getType());

                Picture picture = new Picture();

                picture.create(flag, anonymousBoardPictureRequestDTO);

                pictureRepository.save(picture);
            }
        }

        if(anonymousBoardUpdateRequestDTO.getKeywords() != null) keywordService.updateKeywords(
                anonymousBoardUpdateRequestDTO.getKeywords(),
                "ANONYMOUS_BOARD",
                anonymousBoardSeq
        );

        return result;
    }

    // 익명 게시글 댓글 수정
    @Transactional
    public Comment updateAnonymousBoardComment(
            Long anonymousBoardSeq,
            Long commentSeq,
            String commentContent,
            String userId
    ) {

        Comment existingComment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        if(!existingComment.getUser().equals(user)) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);

        List<Flag> FlagList = flagRepository.findAllByFlagEntitySeq(anonymousBoardSeq);

        Flag flag = FlagList.stream()
                .filter(f -> f.getFlagType().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElse(null);

        existingComment.update(user, flag, commentContent);

        Comment result = commentRepository.save(existingComment);

        return result;
    }

    // 익명 게시글 삭제
    @Transactional
    public boolean deleteAnonymousBoard(Long anonymousBoardSeq, String userId) {

        AnonymousBoard anonymousBoard = anonymousBoardRepository.findById(anonymousBoardSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANONYMOUS_BOARD));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 작성자 여부 확인
        if(!Objects.equals(user.getUserId(), SecurityUtil.getCurrentUserId())) {
            // 관리자가 아닌 경우
            if(!SecurityUtil.getCurrentUserAuthorities().equals("ADMIN")) {

                return false;
            }
        }

        if(anonymousBoard.getDeletedAt() != null) throw new CustomException(
                ErrorCode.CANNOT_DELETE_DATA_ANONYMOUS_BOARD
        );

        anonymousBoard.setDeleted();
        anonymousBoardRepository.save(anonymousBoard);

        List<Flag> flagList = flagRepository.findAllByFlagEntitySeq(anonymousBoardSeq);

        Flag flag = flagList.stream()
                .filter(f -> f.getFlagType().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElse(null);

        List<Picture> pictureList = pictureRepository.findAllByFlag_FlagSeq(flag.getFlagSeq());

        for(Picture picture : pictureList) {
            picture.setDeleted();
            pictureRepository.save(picture);
        }

        List<Comment> commentList = commentRepository.findAllByFlag_FlagSeq(flag.getFlagSeq());

        for(Comment comment : commentList) {
            comment.setDeleted();
            commentRepository.save(comment);
        }

        return true;
    }

    // 익명 게시글 댓글 삭제
    @Transactional
    public boolean deleteAnonymousBoardComment(Long anonymousBoardSeq, Long commentSeq, String userId) {

        Comment comment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        if(!Objects.equals(user.getUserId(), SecurityUtil.getCurrentUserId())) {
            if(!SecurityUtil.getCurrentUserAuthorities().equals("ADMIN")) {

                return false;
            }
        }

        if(comment.getDeletedAt() != null) throw new CustomException(
                ErrorCode.CANNOT_DELETE_DATA_COMMENT
        );

        comment.setDeleted();
        commentRepository.save(comment);

        return true;
    }

    // 북마크 설정/해제
    @Transactional
    public boolean toggleBookmark(Long anonymousBoard, String userId) {

        return bookmarkService.toggleBookmark("ANONYMOUS_BOARD", anonymousBoard, userId);
    }

    // 해당 익명 게시글 북마크 여부 반환
    @Transactional
    public boolean isBookmarked(Long anonymousBoard, String userId) {

        return bookmarkService.isBookmarked("ANONYMOUS_BOARD", anonymousBoard, userId);
    }

    // 좋아요 설정/해제
    @Transactional
    public boolean togglePrefer(Long anonymousBoard, String userId) {

        return preferService.togglePrefer("ANONYMOUS_BOARD", anonymousBoard, userId);
    }

    // 해당 익명 게시글 좋아요 여부 반환
    @Transactional
    public boolean isPreferred(Long anonymousBoard, String userId) {

        return preferService.isPreferred("ANONYMOUS_BOARD", anonymousBoard, userId);
    }
}