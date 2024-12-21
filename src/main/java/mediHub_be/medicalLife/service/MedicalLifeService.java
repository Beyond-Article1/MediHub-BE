package mediHub_be.medicalLife.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.entity.Comment;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.CommentRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.board.service.PictureService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.dept.entity.Dept;
import mediHub_be.medicalLife.dto.*;
import mediHub_be.medicalLife.entity.MedicalLife;
import mediHub_be.medicalLife.repository.MedicalLifeRepository;
import mediHub_be.part.entity.Part;
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


    private static final String MEDICAL_LIFE_FLAG = "MEDICAL_LIFE";


    // 전체 회원 조회
    @Transactional(readOnly = true)

    public List<MedicalLifeListDTO> getMedicalLifeList(Long userSeq) {
        // 유저 존재 확인
        userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 플래그 데이터 가져오기
        List<MedicalLifeFlagDTO> medicalLifeFlagDTOList = flagRepository.findAll().stream()
                .map(flag -> MedicalLifeFlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagType(flag.getFlagType())
                        .flagEntitySeq(flag.getFlagEntitySeq())
                        .build())
                .toList();

        // 키워드 데이터 가져오기
        List<MedicalLifeKeywordDTO> medicalLifeKeywordDTOList = keywordRepository.findAll().stream()
                .map(keyword -> MedicalLifeKeywordDTO.builder()
                        .keywordSeq(keyword.getKeywordSeq())
                        .flagSeq(keyword.getFlagSeq())
                        .keywordName(keyword.getKeywordName())
                        .build())
                .toList();


        List<MedicalLifeListDTO> medicalLifeListDTOList = medicalLifeRepository.findAllByMedicalLifeIsDeletedFalse().stream()
                .map(medicalLife -> {

                    // 해당 게시물에 연결된 플래그 조회
                    List<MedicalLifeFlagDTO> flagsForMedicalLife = medicalLifeFlagDTOList.stream()
                            .filter(flag -> flag.getFlagType().equals(MEDICAL_LIFE_FLAG))
                            .filter(flag -> flag.getFlagEntitySeq().equals(medicalLife.getMedicalLifeSeq()))
                            .toList();

                    // 플래그를 통해 해당 게시물의 키워드 조회
                    List<MedicalLifeKeywordDTO> keywordsForFlag = medicalLifeKeywordDTOList.stream()
                            .filter(keyword -> flagsForMedicalLife.stream()
                                    .anyMatch(flag -> Objects.equals(flag.getFlagSeq(), keyword.getFlagSeq())))
                            .toList();

                    // 작성자의 부서와 파트 정보 조회
                    Dept dept = medicalLife.getUser().getPart().getDept();
                    Part part = medicalLife.getUser().getPart();

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
                            .build();
                })
                .collect(Collectors.toList());

        return medicalLifeListDTOList;
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
        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

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

        // 키워드 가져오기
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

        // DTO 빌드
        return MedicalLifeDetailDTO.builder()
                .userSeq(medicalLife.getUser().getUserSeq())
                .userName(medicalLife.getUser().getUserName())
                .rankingName(medicalLife.getUser().getRanking().getRankingName())
                .medicalLifeContent(medicalLife.getMedicalLifeContent())
                .createdAt(medicalLife.getCreatedAt())
                .keywords(medicalLifeKeywordDTOList)
                .build();
    }


    // 댓글 조회
    @Transactional(readOnly = true)
    public List<MedicalLifeCommentListDTO> getMedicalLifeCommentList(Long medicalLifeSeq, Long userSeq) {
        // 현재 사용자 확인 (로그인 여부 확인 용도)
        userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 플래그 확인
        Flag flag = flagService.findFlag(MEDICAL_LIFE_FLAG, medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 댓글 목록 조회 및 DTO 변환
        return commentRepository.findByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq()).stream()
                .map(comment -> {
                    User commentUser = comment.getUser(); // 댓글 작성자 정보

                    // 댓글 작성자의 부서 및 랭킹 정보 확인
                    String partName = (commentUser.getPart() != null) ? commentUser.getPart().getPartName() : "정보 없음";
                    String rankingName = (commentUser.getRanking() != null) ? commentUser.getRanking().getRankingName() : "정보 없음";

                    // DTO 빌드
                    return MedicalLifeCommentListDTO.builder()
                            .userName(commentUser.getUserName()) // 댓글 작성자 이름
                            .part(partName) // 댓글 작성자 부서
                            .rankingName(rankingName) // 댓글 작성자 랭킹
                            .commentContent(comment.getCommentContent()) // 댓글 내용
                            .createdAt(comment.getCreatedAt()) // 댓글 생성 시간
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
        // 유저 존재 확인
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // MedicalLife 게시글 내용
        String medicalLifeContent = medicalLifeCreateRequestDTO.getMedicalLifeContent();

        // MedicalLife 엔티티 생성
        MedicalLife medicalLife = MedicalLife.builder()
                .user(user)
                .medicalLifeTitle(medicalLifeCreateRequestDTO.getMedicalLifeTitle())
                .medicalLifeContent(medicalLifeContent)
                .medicalLifeIsDeleted(false)
                .medicalLifeViewCount(0L)
                .build();

        // MedicalLife 저장
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
            Long userSeq) {

        // 유저 존재 확인
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // Flag 조회 (MedicalLifeFlag 확인)
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

    @Transactional
    public Long updateMedicalLife(
            Long medicalLifeSeq,
            MedicalLifeUpdateRequestDTO medicalLifeUpdateRequestDTO,
            List<MultipartFile> newImageList,
            Long userSeq
    ) {
        // 유저 존재 확인
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // MedicalLife 게시글 존재 확인
        MedicalLife medicalLife = medicalLifeRepository
                .findById(medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE));

        // 작성자 권한 확인
        if (!medicalLife.getUser().equals(user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 게시글 업데이트 (제목, 내용)
        medicalLife.update(
                medicalLifeUpdateRequestDTO.getMedicalLifeTitle(),
                null
        );
        medicalLifeRepository.save(medicalLife);

        // 기존 키워드 및 이미지 삭제
        Flag flag = flagService.findFlag(MEDICAL_LIFE_FLAG, medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 기존 이미지 삭제
        List<Picture> pictureList = pictureRepository.findAllByFlag_FlagSeqAndPictureIsDeletedFalse(flag.getFlagSeq());
        for (Picture picture : pictureList) {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
            picture.setDeleted();
            pictureRepository.save(picture);
        }

        // 새 이미지 처리 및 업데이트된 내용 저장
        if (newImageList != null && !newImageList.isEmpty()) {
            String updatedContent = pictureService.replaceBase64WithUrls(
                    medicalLifeUpdateRequestDTO.getMedicalLifeContent(),
                    flag.getFlagType(),
                    flag.getFlagEntitySeq()
            );

            medicalLife.update(
                    medicalLifeUpdateRequestDTO.getMedicalLifeTitle(),
                    updatedContent
            );
            medicalLifeRepository.save(medicalLife);
        }

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
