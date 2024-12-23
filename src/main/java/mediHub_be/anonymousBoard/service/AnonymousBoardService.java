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
import mediHub_be.board.repository.CommentRepository;
import mediHub_be.board.service.*;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.notify.service.NotifyServiceImlp;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static mediHub_be.notify.entity.NotiType.COMMENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnonymousBoardService {

    private final AnonymousBoardRepository anonymousBoardRepository;
    private final UserService userService;
    private final PictureService pictureService;
    private final KeywordService keywordService;
    private final ViewCountManager viewCountManager;
    private final BookmarkService bookmarkService;
    private final PreferService preferService;
    private final FlagService flagService;
    private final CommentRepository commentRepository;
    private final NotifyServiceImlp notifyServiceImlp;

    private static final String ANONYMOUS_BOARD_FLAG = "ANONYMOUS_BOARD";

    @Transactional(readOnly = true)
    public List<AnonymousBoardDTO> getBoardList(Long userSeq) {

        userService.findUser(userSeq);
        List<Flag> flagList = flagService.findAllFlag();
        List<Keyword> keywordList = keywordService.findAllKeyword();

        List<AnonymousBoardDTO> anonymousBoardDTOList = anonymousBoardRepository
                .findAllByAnonymousBoardIsDeletedFalse().stream()
                .map(anonymousBoard -> {

                    List<Flag> flagsForAnonymousBoard = flagList.stream()
                            .filter(flag -> flag.getFlagType().equals(ANONYMOUS_BOARD_FLAG))
                            .filter(flag -> flag.getFlagEntitySeq() == anonymousBoard.getAnonymousBoardSeq())
                            .toList();
                    List<Keyword> keywordsForAnonymousBoard = keywordList.stream()
                            .filter(keyword -> flagsForAnonymousBoard.stream()
                                    .anyMatch(flag -> flag.getFlagSeq() == keyword.getFlagSeq()))
                            .toList();

                    return new AnonymousBoardDTO(
                            anonymousBoard.getAnonymousBoardSeq(),
                            anonymousBoard.getUser().getUserName(),
                            anonymousBoard.getAnonymousBoardTitle(),
                            anonymousBoard.getAnonymousBoardViewCount(),
                            anonymousBoard.getCreatedAt(),
                            new ArrayList<>(keywordsForAnonymousBoard)
                    );
                })
                .collect(Collectors.toList());

        return anonymousBoardDTOList;
    }

    @Transactional(readOnly = true)
    public List<AnonymousBoardMyPageDTO> getMyPageBoardList(Long userSeq) {

        userService.findUser(userSeq);
        List<AnonymousBoard> anonymousBoardList = anonymousBoardRepository.
                findByUser_UserSeqAndAnonymousBoardIsDeletedFalse(userSeq);

        return anonymousBoardList.stream()
                .map(anonymousBoard -> new AnonymousBoardMyPageDTO(
                        anonymousBoard.getAnonymousBoardSeq(),
                        anonymousBoard.getAnonymousBoardTitle(),
                        anonymousBoard.getAnonymousBoardViewCount(),
                        anonymousBoard.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AnonymousBoardDTO> getBookMarkedBoardList(Long userSeq) {

        User user = userService.findUser(userSeq);
        List<BookmarkDTO> BookmarkDTOList = bookmarkService.findByUserAndFlagType(user, ANONYMOUS_BOARD_FLAG);

        List<Long> anonymousBoardSeqList = BookmarkDTOList.stream()
                .map(bookmarkDTO -> bookmarkDTO.getFlag().getFlagEntitySeq())
                .toList();

        List<AnonymousBoard> anonymousBoardList = anonymousBoardRepository.findAllById(anonymousBoardSeqList);

        return anonymousBoardList.stream()
                .map(anonymousBoard -> new AnonymousBoardDTO(
                        anonymousBoard.getAnonymousBoardSeq(),
                        anonymousBoard.getUser().getUserName(),
                        anonymousBoard.getAnonymousBoardTitle(),
                        anonymousBoard.getAnonymousBoardViewCount(),
                        anonymousBoard.getCreatedAt(),
                        null
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnonymousBoardTop3DTO> getTop3Boards() {

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        Pageable top3 = PageRequest.of(0, 3, Sort.by(
                Sort.Direction.DESC,
                "anonymousBoardViewCount")
        );

        List<AnonymousBoard> topBoards = anonymousBoardRepository
                .findTop3ByCreatedAtAfterOrderByAnonymousBoardViewCountDesc(oneWeekAgo, top3);

        return topBoards.stream()
                .map(anonymousBoard -> new AnonymousBoardTop3DTO(
                        anonymousBoard.getAnonymousBoardSeq(),
                        anonymousBoard.getAnonymousBoardTitle(),
                        anonymousBoard.getUser().getUserName()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AnonymousBoardCommentDTO> getBoardCommentList(Long anonymousBoardSeq, Long userSeq) {

        User user = userService.findUser(userSeq);
        Flag flag = flagService.findFlag(ANONYMOUS_BOARD_FLAG, anonymousBoardSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        List<AnonymousBoardCommentDTO> anonymousBoardCommentDTOList = commentRepository
                .findByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq()).stream()
                .map(comment -> new AnonymousBoardCommentDTO(
                        user.getUserName(),
                        comment.getCommentContent(),
                        comment.getCreatedAt()))
                .collect(Collectors.toList());

        return anonymousBoardCommentDTOList;
    }

    @Transactional
    public AnonymousBoardDetailDTO getAnonymousBoardDetail(
            Long anonymousBoardSeq,
            Long userSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        User user = userService.findUser(userSeq);
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

        List<Keyword> keywordList = keywordService.getKeywordList(ANONYMOUS_BOARD_FLAG, anonymousBoardSeq);

        return AnonymousBoardDetailDTO.builder()
                .userName(user.getUserName())
                .anonymousBoardTitle(anonymousBoard.getAnonymousBoardTitle())
                .anonymousBoardContent(anonymousBoard.getAnonymousBoardContent())
                .anonymousBoardViewCount(anonymousBoard.getAnonymousBoardViewCount())
                .createdAt(anonymousBoard.getCreatedAt())
                .keywordList(keywordList)
                .build();
    }

    @Transactional
    public Long createAnonymousBoard(
            AnonymousBoardCreateRequestDTO anonymousBoardCreateRequestDTO,
            List<MultipartFile> pictureList,
            Long userSeq
    ) {

        User user = userService.findUser(userSeq);

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

        User user = userService.findUser(userSeq);
        AnonymousBoard anonymousBoard = anonymousBoardRepository
                .findByAnonymousBoardSeqAndAnonymousBoardIsDeletedFalse(anonymousBoardSeq);
        Flag flag = flagService.findFlag(ANONYMOUS_BOARD_FLAG, anonymousBoardSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        Comment comment = Comment.createNewComment(
                user,
                flag,
                anonymousBoardCommentRequestDTO.getCommentContent()
        );

        commentRepository.save(comment);
        notifyServiceImlp.send(
                user,
                anonymousBoard.getUser(),
                flag,
                COMMENT,
                "/anonymous-board/" + anonymousBoardSeq);

        return comment.getCommentSeq();
    }

    @Transactional
    public Long updateAnonymousBoard(
            Long anonymousBoardSeq,
            AnonymousBoardUpdateRequestDTO anonymousBoardUpdateRequestDTO,
            List<MultipartFile> newPictureList,
            Long userSeq
    ) {

        User user = userService.findUser(userSeq);
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
        pictureService.deletePictures(flag);

        if(newPictureList != null && !newPictureList.isEmpty()) {

            String updatedContent = pictureService.replaceBase64WithUrls(
                    anonymousBoardUpdateRequestDTO.getAnonymousBoardContent(),
                    flag.getFlagType(),
                    flag.getFlagEntitySeq()
            );

            anonymousBoard.updateContent(anonymousBoardUpdateRequestDTO.getAnonymousBoardTitle(), updatedContent);
            anonymousBoardRepository.save(anonymousBoard);
        }

        keywordService.updateKeywords(
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

        User user = userService.findUser(userSeq);
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

        User user = userService.findUser(userSeq);
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

        pictureService.deletePictures(flag);
        keywordService.deleteKeywords(ANONYMOUS_BOARD_FLAG, flag.getFlagEntitySeq());
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

        User user = userService.findUser(userSeq);
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

//        User user = userService.findUser(userSeq);
        userService.findByUserId(userId);

        return bookmarkService.toggleBookmark(ANONYMOUS_BOARD_FLAG, anonymousBoard, userId);
    }

    @Transactional
    public boolean isBookmarked(Long anonymousBoard, String userId) {

//        User user = userService.findUser(userSeq);
        userService.findByUserId(userId);

        return bookmarkService.isBookmarked(ANONYMOUS_BOARD_FLAG, anonymousBoard, userId);
    }

    @Transactional
    public boolean togglePrefer(Long anonymousBoard, String userId) {

//        User user = userService.findUser(userSeq);
        userService.findByUserId(userId);

        return preferService.togglePrefer(ANONYMOUS_BOARD_FLAG, anonymousBoard, userId);
    }

    @Transactional
    public boolean isPreferred(Long anonymousBoard, String userId) {

//        User user = userService.findUser(userSeq);
        userService.findByUserId(userId);

        return preferService.isPreferred(ANONYMOUS_BOARD_FLAG, anonymousBoard, userId);
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