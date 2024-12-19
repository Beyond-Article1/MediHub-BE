package mediHub_be.anonymousBoard.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.anonymousBoard.dto.*;
import mediHub_be.anonymousBoard.entity.AnonymousBoard;
import mediHub_be.anonymousBoard.repository.AnonymousBoardRepository;
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
import mediHub_be.notify.service.NotifyServiceImlp;
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
public class AnonymousBoardService {

    private final AnonymousBoardRepository anonymousBoardRepository;
    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final PictureRepository pictureRepository;
    private final KeywordService keywordService;
    private final KeywordRepository keywordRepository;
    private final ViewCountManager viewCountManager;
    private final BookmarkService bookmarkService;
    private final AmazonS3Service amazonS3Service;
    private final FlagService flagService;
    private final FlagRepository flagRepository;
    private final CommentRepository commentRepository;
    private final NotifyServiceImlp notifyServiceImlp;
    private final PreferService preferService;

    private static final String ANONYMOUS_BOARD_FLAG = "ANONYMOUS_BOARD";

    @Transactional(readOnly = true)
    public List<AnonymousBoardListDTO> getBoardList(Long userSeq) {

        userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        List<AnonymousBoardFlagDTO> anonymousBoardFlagDTOList = flagRepository.findAll().stream()
                .map(flag -> AnonymousBoardFlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagType(flag.getFlagType())
                        .flagEntitySeq(flag.getFlagEntitySeq())
                        .build())
                .toList();
        List<AnonymousBoardKeywordDTO> anonymousBoardKeywordDTOList = keywordRepository.findAll().stream()
                .map(keyword -> AnonymousBoardKeywordDTO.builder()
                        .keywordSeq(keyword.getKeywordSeq())
                        .flagSeq(keyword.getFlagSeq())
                        .keywordName(keyword.getKeywordName())
                        .build())
                .toList();
        List<AnonymousBoardListDTO> anonymousBoardListDTOList = anonymousBoardRepository
                .findAllByAnonymousBoardIsDeletedFalse().stream()
                .map(anonymousBoard -> {

                    List<AnonymousBoardFlagDTO> flagsForAnonymousBoard = anonymousBoardFlagDTOList.stream()
                            .filter(flag -> flag.getFlagType().equals(ANONYMOUS_BOARD_FLAG))
                            .filter(flag -> flag.getFlagEntitySeq() == anonymousBoard.getAnonymousBoardSeq())
                            .toList();
                    List<AnonymousBoardKeywordDTO> keywordsForFlag = anonymousBoardKeywordDTOList.stream()
                            .filter(keyword -> flagsForAnonymousBoard.stream()
                                    .anyMatch(flag -> flag.getFlagSeq() == keyword.getFlagSeq()))
                            .toList();

                    return AnonymousBoardListDTO.builder()
                            .anonymousBoardSeq(anonymousBoard.getAnonymousBoardSeq())
                            .userName(anonymousBoard.getUser().getUserName())
                            .anonymousBoardTitle(anonymousBoard.getAnonymousBoardTitle())
                            .anonymousBoardViewCount(anonymousBoard.getAnonymousBoardViewCount())
                            .createdAt(anonymousBoard.getCreatedAt())
                            .keywords(keywordsForFlag.stream()
                                    .map(AnonymousBoardKeywordDTO::getKeywordName)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        return anonymousBoardListDTOList;
    }

    @Transactional(readOnly = true)
    public List<AnonymousBoardCommentListDTO> getBoardCommentList(Long anonymousBoardSeq, Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        Flag flag = flagService.findFlag(ANONYMOUS_BOARD_FLAG, anonymousBoardSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        List<AnonymousBoardCommentListDTO> anonymousBoardCommentListDTOList = commentRepository
                .findByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq()).stream()
                .map(comment -> AnonymousBoardCommentListDTO.builder()
                        .userName(user.getUserName())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return anonymousBoardCommentListDTOList;
    }

    @Transactional
    public AnonymousBoardDetailDTO getAnonymousBoardDetail(
            Long anonymousBoardSeq,
            Long userSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        AnonymousBoard anonymousBoard = anonymousBoardRepository
                .findByAnonymousBoardSeqAndAnonymousBoardIsDeletedFalse(anonymousBoardSeq);

        if(anonymousBoard.getDeletedAt() != null) throw new CustomException(
                ErrorCode.CANNOT_DELETE_DATA_ANONYMOUS_BOARD
        );

        boolean shouldIncrease = viewCountManager.shouldIncreaseViewCount(anonymousBoardSeq, request, response);

        if(shouldIncrease) {

            log.info("오늘 처음 조회한 익명 게시글");

            anonymousBoard.increaseViewCount();
            anonymousBoardRepository.save(anonymousBoard);
        } else log.info("이미 조회한 적 있는 익명 게시글");

        List<Keyword> keywordList = keywordRepository.findByFlagTypeAndEntitySeq(
                ANONYMOUS_BOARD_FLAG,
                anonymousBoardSeq
        );

        List<AnonymousBoardKeywordDTO> anonymousBoardKeywordDTOList = keywordList.stream()
                .map(keyword -> new AnonymousBoardKeywordDTO(
                        keyword.getKeywordSeq(),
                        keyword.getFlagSeq(),
                        keyword.getKeywordName()
                ))
                .toList();

        return AnonymousBoardDetailDTO.builder()
                .userName(user.getUserName())
                .anonymousBoardTitle(anonymousBoard.getAnonymousBoardTitle())
                .anonymousBoardContent(anonymousBoard.getAnonymousBoardContent())
                .anonymousBoardViewCount(anonymousBoard.getAnonymousBoardViewCount())
                .createdAt(anonymousBoard.getCreatedAt())
                .keywords(anonymousBoardKeywordDTOList)
                .build();
    }

    @Transactional
    public Long createAnonymousBoard(
            AnonymousBoardCreateRequestDTO anonymousBoardCreateRequestDTO,
            List<MultipartFile> pictureList,
            Long userSeq
    ) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        String anonymousBoardContent = anonymousBoardCreateRequestDTO.getAnonymousBoardContent();
        AnonymousBoard anonymousBoard = AnonymousBoard.createNewAnonymousBoard(
                user,
                anonymousBoardCreateRequestDTO.getAnonymousBoardTitle(),
                null
        );

        anonymousBoardRepository.save(anonymousBoard);

        saveKeywordsAndFlag(anonymousBoardCreateRequestDTO.getKeywords(), anonymousBoard.getAnonymousBoardSeq());
        updateAnonymousBoardContentWithImages(
                anonymousBoard,
                anonymousBoardContent
        );

        return anonymousBoard.getAnonymousBoardSeq();
    }

    @Transactional
    public Long createAnonymousBoardComment(
            Long anonymousBoardSeq,
            AnonymousBoardCommentRequestDTO anonymousBoardCommentRequestDTO,
            Long userSeq
    ) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        Flag flag = flagService.findFlag(ANONYMOUS_BOARD_FLAG, anonymousBoardSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        Comment comment = Comment.createNewComment(
                user,
                flag,
                anonymousBoardCommentRequestDTO.getCommentContent()
        );

        commentRepository.save(comment);
//        notifyServiceImlp.send(
//                user,
//                anonymousBoard.getUser(),
//                flag,
//                COMMENT,
//                "/anonymous-board/" + anonymousBoardSeq);

        return comment.getCommentSeq();
    }

    @Transactional
    public Long updateAnonymousBoard(
            Long anonymousBoardSeq,
            AnonymousBoardUpdateRequestDTO anonymousBoardUpdateRequestDTO,
            List<MultipartFile> newImageList,
            Long userSeq
    ) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        AnonymousBoard anonymousBoard = anonymousBoardRepository
                .findByAnonymousBoardSeqAndAnonymousBoardIsDeletedFalse(anonymousBoardSeq);

        if(!anonymousBoard.getUser().equals(user)) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);

        anonymousBoard.updateContent(
                anonymousBoardUpdateRequestDTO.getAnonymousBoardTitle(),
                null
        );

        anonymousBoardRepository.save(anonymousBoard);

        Flag flag = flagService.findFlag(ANONYMOUS_BOARD_FLAG, anonymousBoard.getAnonymousBoardSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        List<Picture> pictureList = pictureRepository.findAllByFlag_FlagSeqAndPictureIsDeletedFalse(flag.getFlagSeq());

        for(Picture picture : pictureList) {

            amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
            picture.setDeleted();
            pictureRepository.save(picture);
        }

        if(newImageList != null && !newImageList.isEmpty()) {

            String updatedContent = pictureService.replaceBase64WithUrls(
                    anonymousBoardUpdateRequestDTO.getAnonymousBoardContent(),
                    flag.getFlagType(),
                    flag.getFlagEntitySeq()
            );

            anonymousBoard.updateContent(anonymousBoardUpdateRequestDTO.getAnonymousBoardTitle(), updatedContent);
            anonymousBoardRepository.save(anonymousBoard);
        }

        if(anonymousBoardUpdateRequestDTO.getKeywords() != null) keywordService.updateKeywords(
                anonymousBoardUpdateRequestDTO.getKeywords(),
                ANONYMOUS_BOARD_FLAG,
                anonymousBoardSeq
        );

        return anonymousBoard.getAnonymousBoardSeq();
    }

    @Transactional
    public Long updateAnonymousBoardComment(
            Long anonymousBoardSeq,
            Long commentSeq,
            AnonymousBoardCommentRequestDTO anonymousBoardCommentRequestDTO,
            Long userSeq
    ) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        Comment comment = commentRepository.findByCommentSeqAndCommentIsDeletedFalse(commentSeq);

        if(!comment.getUser().equals(user)) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);

        Flag flag = flagService.findFlag(ANONYMOUS_BOARD_FLAG, anonymousBoardSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        comment.updateContent(flag, anonymousBoardCommentRequestDTO.getCommentContent());
        commentRepository.save(comment);

        return comment.getCommentSeq();
    }

    @Transactional
    public boolean deleteAnonymousBoard(Long anonymousBoardSeq, Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        AnonymousBoard anonymousBoard = anonymousBoardRepository
                .findByAnonymousBoardSeqAndAnonymousBoardIsDeletedFalse(anonymousBoardSeq);

        if(!Objects.equals(user.getUserSeq(), SecurityUtil.getCurrentUserSeq())) {

            if(!SecurityUtil.getCurrentUserAuthorities().equals("ADMIN")) return false;
        }

        if(anonymousBoard.getDeletedAt() != null) throw new CustomException(
                ErrorCode.CANNOT_DELETE_DATA_ANONYMOUS_BOARD
        );

        Flag flag = flagService.findFlag(ANONYMOUS_BOARD_FLAG, anonymousBoard.getAnonymousBoardSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        List<Picture> pictureList = pictureRepository.findAllByFlag_FlagSeqAndPictureIsDeletedFalse(flag.getFlagSeq());

        for(Picture picture : pictureList) {

            amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
            picture.setDeleted();
            pictureRepository.save(picture);
        }

        List<Keyword> keywordList = keywordRepository
                .findByFlagTypeAndEntitySeq(ANONYMOUS_BOARD_FLAG, flag.getFlagSeq()
        );

        keywordRepository.deleteAll(keywordList);
        bookmarkService.deleteBookmarkByFlag(flag);
        preferService.deletePreferByFlag(flag);

        List<Comment> commentList = commentRepository.findAllByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq());

        for(Comment comment : commentList) {

            comment.setDeleted();
            commentRepository.save(comment);
        }

        anonymousBoard.setDeleted();
        anonymousBoardRepository.save(anonymousBoard);

        return true;
    }

    @Transactional
    public boolean deleteAnonymousBoardComment(Long anonymousBoardSeq, Long commentSeq, Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        Comment comment = commentRepository.findByCommentSeqAndCommentIsDeletedFalse(commentSeq);

        if(!Objects.equals(user.getUserSeq(), SecurityUtil.getCurrentUserSeq())) {

            if(!SecurityUtil.getCurrentUserAuthorities().equals("ADMIN")) return false;
        }

        if(comment.getDeletedAt() != null) throw new CustomException(ErrorCode.CANNOT_DELETE_DATA_COMMENT);

        comment.setDeleted();
        commentRepository.save(comment);

        return true;
    }

    @Transactional
    public boolean toggleBookmark(Long anonymousBoard, String userId) {

        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        return bookmarkService.toggleBookmark(ANONYMOUS_BOARD_FLAG, anonymousBoard, userId);
    }

    @Transactional
    public boolean isBookmarked(Long anonymousBoard, String userId) {

        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        return bookmarkService.isBookmarked(ANONYMOUS_BOARD_FLAG, anonymousBoard, userId);
    }

    @Transactional
    public boolean togglePrefer(Long anonymousBoard, String userId) {

        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        return preferService.togglePrefer(ANONYMOUS_BOARD_FLAG, anonymousBoard, userId);
    }

    @Transactional
    public boolean isPreferred(Long anonymousBoard, String userId) {

        userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        return preferService.isPreferred(ANONYMOUS_BOARD_FLAG, anonymousBoard, userId);
    }

    @Transactional(readOnly = true)
    public List<AnonymousBoardMyListDTO> getMyBoardList(Long userSeq) {

        userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        List<AnonymousBoard> anonymousBoardList =  anonymousBoardRepository.
                findByUser_UserSeqAndAnonymousBoardIsDeletedFalse(userSeq);

        return anonymousBoardList.stream()
                .map(anonymousBoard -> new AnonymousBoardMyListDTO(
                        anonymousBoard.getAnonymousBoardSeq(),
                        anonymousBoard.getAnonymousBoardTitle(),
                        anonymousBoard.getAnonymousBoardViewCount(),
                        anonymousBoard.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AnonymousBoardListDTO> getBookMarkedBoardList(Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        List<BookmarkDTO> BookmarkDTOList = bookmarkService.findByUserAndFlagType(user, ANONYMOUS_BOARD_FLAG);

        List<Long> anonymousBoardSeqList = BookmarkDTOList.stream()
                .map(bookmarkDTO -> bookmarkDTO.getFlag().getFlagEntitySeq())
                .toList();

        List<AnonymousBoard> anonymousBoardList = anonymousBoardRepository.findAllById(anonymousBoardSeqList);

        return anonymousBoardList.stream()
                .map(anonymousBoard -> new AnonymousBoardListDTO(
                        anonymousBoard.getAnonymousBoardSeq(),
                        anonymousBoard.getUser().getUserName(),
                        anonymousBoard.getAnonymousBoardTitle(),
                        anonymousBoard.getAnonymousBoardViewCount(),
                        anonymousBoard.getCreatedAt(),
                        null
                ))
                .collect(Collectors.toList());
    }

    private void saveKeywordsAndFlag(List<String> keywordList, Long entitySeq) {

        Flag flag = flagService.createFlag(ANONYMOUS_BOARD_FLAG, entitySeq);

        if(keywordList != null && !keywordList.isEmpty()) keywordService.saveKeywords(keywordList, flag.getFlagSeq());
    }

    private void updateAnonymousBoardContentWithImages(AnonymousBoard anonymousBoard, String anonymousBoardContent) {

        // Base64 이미지 -> S3 URL 변환
        String updatedAnonymousBoardContent = pictureService.replaceBase64WithUrls(
                anonymousBoardContent,
                ANONYMOUS_BOARD_FLAG,
                anonymousBoard.getAnonymousBoardSeq()
        );

        anonymousBoard.updateAnonymousBoardContent(
                anonymousBoard.getAnonymousBoardTitle(),
                updatedAnonymousBoardContent
        );

        anonymousBoardRepository.save(anonymousBoard);
    }
}