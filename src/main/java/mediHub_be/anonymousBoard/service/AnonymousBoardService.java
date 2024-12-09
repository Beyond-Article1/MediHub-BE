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
import mediHub_be.board.service.KeywordService;
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

    // 익명 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<AnonymousBoardListDTO> getAnonymousBoardList(String userId) {

        userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        // 모든 게시판 식별 목록 가져오기
        List<FlagDTO> flagList = flagRepository.findAll().stream()
                .map(flag -> FlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagBoardFlag(flag.getFlagBoardFlag())
                        .flagPostSeq(flag.getFlagPostSeq())
                        .build())
                .collect(Collectors.toList());
        // 모든 사진 목록 가져오기
        List<PictureDTO> pictureList = pictureRepository.findAll().stream()
                .map(picture -> PictureDTO.builder()
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
        List<AnonymousBoardListDTO> anonymousBoardListDTO = anonymousBoardRepository
                .findAllByAnonymousBoardIsDeletedFalse().stream()
                .map(anonymousBoard -> {
                    // 현재 익명 게시글의 SEQ와 일치하는 게시판 식별 번호 목록 필터링
                    List<FlagDTO> flagsForAnonymousBoard = flagList.stream()
                            .filter(flag -> flag.getFlagBoardFlag().equals("ANONYMOUS_BOARD"))
                            .filter(flag -> flag.getFlagPostSeq() == anonymousBoard.getAnonymousBoardSeq())
                            .collect(Collectors.toList());
                    // 현재 익명 게시글의 SEQ와 일치하는 사진 목록 필터링
                    List<PictureDTO> imagesForFlag = pictureList.stream()
                            // == 연산자로 비교
                            .filter(picture -> flagsForAnonymousBoard.stream()
                                    .anyMatch(flag -> flag.getFlagSeq() == picture.getFlagSeq()))
                            .collect(Collectors.toList());

                    // 익명 게시글 DTO를 생성하고 이미지 목록 추가
                    return AnonymousBoardListDTO.builder()
                            .anonymousBoardSeq(anonymousBoard.getAnonymousBoardSeq())
                            .userId(anonymousBoard.getUser().getUserId())
                            .anonymousBoardTitle(anonymousBoard.getAnonymousBoardTitle())
                            .anonymousBoardViewCount(anonymousBoard.getAnonymousBoardViewCount())
                            .createdAt(anonymousBoard.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return anonymousBoardListDTO;
    }

    // 익명 게시글 조회
    @Transactional
    public AnonymousBoardDetailDTO getAnonymousBoardDetail(
            Long anonymousBoardSeq,
            String userId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        AnonymousBoard anonymousBoard = anonymousBoardRepository.findById(anonymousBoardSeq)
                .orElseThrow(() -> new IllegalArgumentException("익명 게시글을 찾을 수 없습니다."));

        // 삭제된 익명 게시글인지 확인
        if(anonymousBoard.getDeletedAt() != null) throw new IllegalArgumentException(
                "삭제된 익명 게시글은 조회할 수 없습니다."
        );

        // 작성자 정보 조회
        User author = userRepository.findById(anonymousBoard.getUser().getUserSeq())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));
        boolean shouldIncrease = viewCountManager.shouldIncreaseViewCount(anonymousBoardSeq, request, response);

        if(shouldIncrease) {
            log.info("오늘 처음 조회한 익명 게시글");

            anonymousBoard.increaseViewCount();
            anonymousBoardRepository.save(anonymousBoard);
        } else log.info("이미 조회한 적 있는 익명 게시글");

        List<FlagDTO> flagDTOList = flagRepository.findAllByFlagPostSeq(anonymousBoardSeq).stream()
                .map(flag -> FlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagBoardFlag(flag.getFlagBoardFlag())
                        .flagPostSeq(flag.getFlagPostSeq())
                        .build())
                .collect(Collectors.toList());
        FlagDTO flagDTO = flagDTOList.stream()
                .filter(flag -> flag.getFlagBoardFlag().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("익명 게시글에 대한 플래그가 존재하지 않습니다."));

        List<PictureDTO> pictureDTOList = pictureRepository.findAllByFlag_FlagSeq(flagDTO.getFlagSeq()).stream()
                .map(picture -> PictureDTO.builder()
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
        List<Keyword> keywords = keywordRepository.findByBoardFlagAndPostSeq(
                "ANONYMOUS_BOARD",
                anonymousBoardSeq);

        List<AnonymousBoardKeywordDTO> keywordDTOs = keywords.stream()
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
                .anonymousBoardPictureList(pictureDTOList)
                .keywords(keywordDTOs)
                .build();
    }

    // 익명 게시글 생성
    @Transactional
    public Long createAnonymousBoard(
            AnonymousBoardCreateRequestDTO requestDTO,
            List<MultipartFile> imageList,
            String userId
    ) throws IOException {

        // 1. DB (AnonymousBoard)에 데이터 저장
        // 여기서 userId를 SecurityUtil.getCurrentUserSeq()로 대체
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        if(imageList != null && !imageList.isEmpty()) requestDTO.setImageList(imageList);

        AnonymousBoard anonymousBoard = AnonymousBoard.createNewAnonymousBoard(
                user,
                requestDTO.getAnonymousBoardTitle(),
                requestDTO.getAnonymousBoardContent()
        );

        anonymousBoardRepository.save(anonymousBoard);

        Flag flag = Flag.builder()
                .flagBoardFlag("ANONYMOUS_BOARD")
                .flagPostSeq(anonymousBoard.getAnonymousBoardSeq())
                .build();

        flagRepository.save(flag);

        if(requestDTO.getImageList() != null && !requestDTO.getImageList().isEmpty()) {
            for (MultipartFile image : requestDTO.getImageList()) {
                // 2. Amazon S3 버킷에 이미지 저장
                AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);

                // 3. DB (Picture)에 데이터 저장
                RequestPicture requestPicture = new RequestPicture();

                requestPicture.setFlagSeq(flag.getFlagSeq());
                requestPicture.setPictureName(metaData.getOriginalFileName());
                requestPicture.setPictureChangedName(metaData.getChangeFileName());
                requestPicture.setPictureUrl(metaData.getUrl());
                requestPicture.setPictureType(metaData.getType());

                Picture picture = new Picture();

                picture.create(flag, requestPicture);

                pictureRepository.save(picture);
            }
        }

        // 키워드 저장
        if(requestDTO.getKeywords() != null && !requestDTO.getKeywords().isEmpty()) {
            keywordService.saveKeywords(
                    requestDTO.getKeywords(),
                    flag.getFlagSeq()
            );
        }

        return anonymousBoard.getAnonymousBoardSeq();
    }

    // 익명 게시글 수정
    @Transactional
    public AnonymousBoard updateAnonymousBoard(
            Long anonymousBoardSeq,
            AnonymousBoardUpdateRequestDTO requestDTO,
            List<MultipartFile> imageList,
            String userId
    ) throws IOException {

        AnonymousBoard existingAnonymousBoard = anonymousBoardRepository.findById(anonymousBoardSeq)
                .orElseThrow(() -> new IllegalArgumentException("익명 게시글을 찾을 수 없습니다."));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        if(!existingAnonymousBoard.getUser().equals(user)) throw new IllegalArgumentException(
                "작성자만 익명 게시글을 수정할 수 있습니다."
        );

        existingAnonymousBoard.update(user, requestDTO.getAnonymousBoardTitle(), requestDTO.getAnonymousBoardContent());

        if(imageList != null && !imageList.isEmpty()) requestDTO.setImageList(imageList);

        AnonymousBoard result = anonymousBoardRepository.save(existingAnonymousBoard);
        List<Flag> optionalFlag = flagRepository.findAllByFlagPostSeq(anonymousBoardSeq);

        Flag flag = optionalFlag.stream()
                .filter(f -> f.getFlagBoardFlag().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElse(null);

        if(requestDTO.getImageList() != null && !requestDTO.getImageList().isEmpty()) {
            for(MultipartFile image : requestDTO.getImageList()) {
                AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);

                log.info("업로드 성공");

                RequestPicture requestPicture = new RequestPicture();

                requestPicture.setFlagSeq(flag.getFlagSeq());
                requestPicture.setPictureName(metaData.getOriginalFileName());
                requestPicture.setPictureChangedName(metaData.getChangeFileName());
                requestPicture.setPictureUrl(metaData.getUrl());
                requestPicture.setPictureType(metaData.getType());

                Picture picture = new Picture();

                picture.create(flag, requestPicture);

                pictureRepository.save(picture);
            }
        }

        if(requestDTO.getKeywords() != null) keywordService.updateKeywords(
                requestDTO.getKeywords(),
                "ANONYMOUS_BOARD",
                anonymousBoardSeq
        );

        return result;
    }

    // 익명 게시글 삭제
    @Transactional
    public boolean deleteAnonymousBoard(Long anonymousBoardSeq, String userId) {

        AnonymousBoard anonymousBoard = anonymousBoardRepository.findById(anonymousBoardSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 익명 게시글 입니다."));
        // 1. 작성자 여부 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        // 작성자가 아닌 경우
        if(!Objects.equals(user.getUserId(), SecurityUtil.getCurrentUserId())) {
            return false;
        }
        // 관리자가 아닌 경우
        else if(SecurityUtil.getCurrentUserAuthorities().equals("USER")) return false;

        if(anonymousBoard.getDeletedAt() != null) throw new IllegalArgumentException("이미 삭제된 익명 게시글입니다.");

        // 2. DB (AnonymousBoard)에 해당 익명 게시글 삭제 상태 등록
        anonymousBoard.setDeleted();
        anonymousBoardRepository.save(anonymousBoard);

        // 3. DB (PICTURE)에 해당 익명 게시글 번호로 된 모든 데이터 삭제 상태 등록
        List<Flag> flagList = flagRepository.findAllByFlagPostSeq(anonymousBoardSeq);

        Flag flag = flagList.stream()
                .filter(f -> f.getFlagBoardFlag().equals("ANONYMOUS_BOARD"))
                .findFirst()
                .orElse(null);

        List<Picture> pictureList = pictureRepository.findAllByFlag_FlagSeq(flag.getFlagSeq());

        // 각 Picture 객체 삭제 상태 등록
        for(Picture picture : pictureList) {
            picture.setDeleted();

            pictureRepository.save(picture);
        }

        // 4. DB (COMMENT)에 해당 댓글 모두 삭제 상태 등록
        List<Comment> commentList = commentRepository.findAllByFlag_FlagSeq(flag.getFlagSeq());

        for(Comment comment : commentList) {
            comment.setDeleted();

            commentRepository.save(comment);
        }

        return true;
    }
}