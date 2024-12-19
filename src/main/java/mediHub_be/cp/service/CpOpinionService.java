package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.board.service.PictureService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.CpOpinionDTO;
import mediHub_be.cp.dto.CpOpinionVoteDTO;
import mediHub_be.cp.dto.RequestCpOpinionDTO;
import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.entity.CpOpinion;
import mediHub_be.cp.entity.CpOpinionVote;
import mediHub_be.cp.repository.CpOpinionRepository;
import mediHub_be.cp.repository.CpOpinionVoteRepository;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpOpinionService {

    // Service
    private final CpOpinionVoteService cpOpinionVoteService;
    private final FlagService flagService;
    private final KeywordService keywordService;
    private final PictureService pictureService;
    private final BookmarkService bookmarkService;
    private final UserService userService;

    // Repository
    private final CpOpinionRepository cpOpinionRepository;
    private final KeywordRepository keywordRepository;
    private final CpOpinionVoteRepository cpOpinionVoteRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpOpinionService");    // Logger

    // FlagType
    public static final String CP_OPINION_BOARD_FLAG = "CP_OPINION";


    /**
     * 주어진 CP 버전 번호와 CP 의견 위치 번호에 따라 CP 의견 목록을 조회합니다.
     *
     * @param cpVersionSeq         CP 버전 번호
     * @param cpOpinionLocationSeq CP 의견 위치 번호
     * @param isDeleted            삭제된 의견을 조회할지 여부
     * @return ResponseCpOpinionDTO의 리스트
     * @throws CustomException 조회된 의견이 없을 경우
     */
    @Transactional(readOnly = true)
    public List<ResponseCpOpinionDTO> getCpOpinionListByCpVersionSeq(
            long cpVersionSeq,
            long cpOpinionLocationSeq,
            boolean isDeleted) {

        logger.info("CP 버전 번호: {}의 CP 의견 위치 번호: {}로 조회 요청했습니다.", cpVersionSeq, cpOpinionLocationSeq);

        List<ResponseCpOpinionDTO> responseCpOpinionDtoList;

        try {
            // DB 조회
            if (isDeleted) {
                logger.info("삭제된 CP 의견을 조회합니다.");
                responseCpOpinionDtoList = cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNotNull(cpOpinionLocationSeq);
            } else {
                logger.info("활성 상태의 CP 의견을 조회합니다.");
                responseCpOpinionDtoList = cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNull(cpOpinionLocationSeq);
            }

            // 키워드 목록 설정
            List<ResponseCpOpinionDTO> dtoList = setKeywordListForCpOpinions(responseCpOpinionDtoList);

            // 결과 확인
            if (dtoList.isEmpty()) {
                logger.warn("조회된 CP 의견이 없습니다. CP 버전 번호: {}, CP 의견 위치 번호: {}", cpVersionSeq, cpOpinionLocationSeq);
                throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
            } else {
                logger.info("조회된 CP 의견의 수: {}", dtoList.size());
            }

            return dtoList;
        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 못한 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 주어진 CP 의견 DTO 리스트의 각 의견에 대해 키워드 목록을 설정하고,
     * 키워드가 포함된 새로운 DTO 리스트를 반환합니다.
     *
     * @param responseCpOpinionDtoList CP 의견 DTO 리스트
     * @return 키워드 목록이 포함된 CP 의견 DTO 리스트
     * @throws DataAccessException 데이터베이스 접근 중 오류가 발생한 경우
     * @throws CustomException     사용자 정의 예외가 발생한 경우
     * @throws RuntimeException    기타 예기치 않은 오류가 발생한 경우
     */
    @Transactional(readOnly = true)
    public List<ResponseCpOpinionDTO> setKeywordListForCpOpinions(List<ResponseCpOpinionDTO> responseCpOpinionDtoList) {
        List<ResponseCpOpinionDTO> resultList = new ArrayList<>();
        logger.info("CP 의견 DTO 리스트에 대한 키워드 목록 설정 시작. 총 의견 수: {}", responseCpOpinionDtoList.size());

        try {
            for (ResponseCpOpinionDTO dto : responseCpOpinionDtoList) {
                logger.info("CP 의견 번호: {}에 대한 키워드 목록 설정 중...", dto.getCpOpinionSeq());
                ResponseCpOpinionDTO resultDto = setKeywordForCpOpinion(dto);
                resultList.add(resultDto);
                logger.info("CP 의견 번호: {}에 대한 키워드 목록이 설정되었습니다.", dto.getCpOpinionSeq());
            }
        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류: {}", e.getMessage());
            throw e;
        } catch (CustomException e) {
            logger.error("사용자 정의 예외 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("예기치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("예기치 못한 오류가 발생했습니다.", e);
        }

        logger.info("모든 CP 의견에 대한 키워드 목록 설정 완료. 결과 리스트 크기: {}", resultList.size());
        return resultList;
    }

    /**
     * 주어진 CP 의견 DTO에 대해 키워드 목록을 설정하고, 키워드가 포함된 새로운 DTO를 반환합니다.
     *
     * @param dto 설정할 CP 의견 DTO
     * @return 키워드 목록이 포함된 CP 의견 DTO
     * @throws DataAccessException 데이터베이스 접근 중 오류가 발생한 경우
     * @throws CustomException     사용자 정의 예외가 발생한 경우
     * @throws RuntimeException    기타 예기치 않은 오류가 발생한 경우
     */
    public ResponseCpOpinionDTO setKeywordForCpOpinion(ResponseCpOpinionDTO dto) {
        logger.info("CP 의견 번호: {}에 대한 키워드 목록을 설정합니다.", dto.getCpOpinionSeq());

        try {
            // 키워드 리스트 조회
            List<Keyword> keywordList = keywordService.getKeywordList(CP_OPINION_BOARD_FLAG, dto.getCpOpinionSeq());
            List<CpOpinionVoteDTO> voteList = getCpOpinionVoteList(dto.getCpOpinionSeq());
            // 비율 계산 및 DTO 생성
            if (voteList.isEmpty()) {
                return ResponseCpOpinionDTO.create(dto, keywordList);
            } else {
                return ResponseCpOpinionDTO.create(dto, keywordList, voteList);
            }
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 주어진 CP 의견 번호에 해당하는 CP 의견 투표 목록을 조회합니다.
     *
     * @param cpOpinionSeq 조회할 CP 의견의 ID
     * @return CP 의견 투표 목록 (없을 경우 빈 리스트 반환)
     */
    public List<CpOpinionVoteDTO> getCpOpinionVoteList(long cpOpinionSeq) {
        logger.info("CP 의견 번호: {}에 대한 의견 투표 목록을 조회합니다.", cpOpinionSeq);

        List<CpOpinionVote> entityList;

        try {
            // CP 의견 투표 목록 조회
            entityList = cpOpinionVoteRepository.findByCpOpinionSeq(cpOpinionSeq);
        } catch (Exception e) {
            logger.error("데이터베이스 접근 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        }

        // 결과가 없을 경우 로그 남기기
        if (entityList == null || entityList.isEmpty()) {
            logger.warn("CP 의견 번호: {}에 대한 의견 투표가 존재하지 않습니다.", cpOpinionSeq);
        } else {
            logger.info("조회된 CP 의견 투표 목록의 크기: {}", entityList.size());
        }

        // DTO 리스트로 변환
        List<CpOpinionVoteDTO> dtoList = new ArrayList<>();
        for (CpOpinionVote entity : entityList) {
            dtoList.add(CpOpinionVoteDTO.toDto(entity));
        }

        return dtoList; // 빈 리스트를 반환
    }

    /**
     * 주어진 CP 의견 번호에 해당하는 CP 의견을 조회합니다.
     *
     * @param cpOpinionSeq 조회할 CP 의견의 ID
     * @return 조회된 CP 의견의 DTO
     * @throws CustomException 조회된 의견이 없거나 접근 권한이 없는 경우
     */
    @Transactional(readOnly = true)
    public ResponseCpOpinionDTO findCpOpinionByCpOpinionSeq(long cpOpinionSeq) {
        logger.info("CP 의견 번호: {}로 조회 요청했습니다.", cpOpinionSeq);

        // DB 조회
        CpOpinion entity = cpOpinionRepository.findById(cpOpinionSeq)
                .orElseThrow(() -> {
                    logger.warn("조회된 CP 의견이 없습니다. CP 의견 번호: {}", cpOpinionSeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
                });

        if (entity.getDeletedAt() == null) {
            // 활성 상태
            logger.info("CP 의견 번호: {}는 활성 상태입니다.", cpOpinionSeq);
            incrementViewCount(entity, cpOpinionSeq); // 조회수 증가 메서드 호출
            logger.info("CP 의견 번호: {}의 조회수가 증가했습니다.", cpOpinionSeq);

            return retrieveCpOpinionDto(cpOpinionSeq);
        } else {
            // 삭제 상태
            logger.info("CP 의견 번호: {}는 삭제된 상태입니다.", cpOpinionSeq);
            if (isAdminUser()) {
                logger.info("관리자 권한으로 CP 의견 번호: {}를 조회합니다.", cpOpinionSeq);
                return retrieveCpOpinionDto(cpOpinionSeq);
            } else {
                logger.warn("CP 의견 번호: {}에 대한 접근 권한이 없습니다.", cpOpinionSeq);
                throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
            }
        }
    }

    /**
     * CP 의견 번호에 해당하는 CP 의견 DTO를 조회합니다.
     *
     * @param cpOpinionSeq 조회할 CP 의견의 ID
     * @return 조회된 CP 의견의 DTO
     * @throws CustomException 조회된 의견이 없을 경우
     */
    private ResponseCpOpinionDTO retrieveCpOpinionDto(long cpOpinionSeq) {
        ResponseCpOpinionDTO dto = cpOpinionRepository.findByCpOpinionSeq(cpOpinionSeq)
                .orElseThrow(() -> {
                    logger.warn("조회된 CP 의견이 없습니다. CP 의견 번호: {}", cpOpinionSeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
                });

        // 키워드 조회
        logger.info("CP 의견 번호: {}에 대한 키워드 목록을 조회합니다.", cpOpinionSeq);
        setKeywordForCpOpinion(dto);

        return dto;
    }

    /**
     * 현재 사용자가 관리자 권한을 가지고 있는지 확인합니다.
     *
     * @return 관리자 권한 여부
     */
    private boolean isAdminUser() {
        boolean isAdmin = SecurityUtil.getCurrentUserAuthorities().equals(UserAuth.ADMIN);
        logger.info("현재 사용자는 관리자 권한: {}", isAdmin);
        return isAdmin;
    }

    /**
     * 주어진 CP 의견의 조회 수를 증가시킵니다.
     *
     * @param cpOpinionSeq 조회 수를 증가시킬 CP 의견의 ID
     * @throws CustomException 주어진 ID에 해당하는 CP 의견이 존재하지 않을 경우 예외를 발생시킬 수 있습니다.
     */
    @Transactional
    public void incrementViewCount(CpOpinion entity, long cpOpinionSeq) {
        entity.increaseCpOpinionViewCount();
        cpOpinionRepository.save(entity);
    }

    /**
     * 주어진 CP 버전 번호와 CP 의견 위치 번호에 따라 새로운 CP 의견을 생성합니다.
     *
     * @param cpVersionSeq         CP 버전 번호
     * @param cpOpinionLocationSeq CP 의견 위치 번호
     * @param requestBody          CP 의견을 생성하기 위한 요청 본문
     * @return 생성된 CP 의견의 DTO
     * @throws CustomException 입력값이 유효하지 않거나 데이터베이스 오류가 발생할 경우
     *                         (예: requestBody의 필드가 누락되었거나 잘못된 형식일 때)
     */
    @Transactional
    public CpOpinionDTO createCpOpinion(
            long cpVersionSeq,
            long cpOpinionLocationSeq,
            RequestCpOpinionDTO requestBody) {

        // 입력값 유효성 검사
        validateRequestCpOpinion(requestBody);

        // 입력값으로 DTO 생성
        CpOpinionDTO dto = CpOpinionDTO.create(cpOpinionLocationSeq, requestBody);
        logger.info("CP 의견 DTO 생성 완료");

        // DTO -> Entity 변환
        CpOpinion entity = CpOpinion.toEntity(dto);
        logger.info("CP 의견 Entity 변환 완료");

        try {
            // 이미지 업로드 및 본문 변환 처리
            updateCpOpinionContentWithImage(entity, requestBody.getCpOpinionContent());
            logger.info("CP 의견 Entity 이미지 변환 및 저장 완료");

            // DB에 저장하고 해당 값을 다시 받아옴.
            entity = cpOpinionRepository.save(entity);
            logger.info("CP 의견이 DB에 성공적으로 저장되었습니다: {}", entity);

            // 키워드 등록
            if (requestBody.getKeywordList() != null && !requestBody.getKeywordList().isEmpty()) {

                // 1. Flag 생성 및 저장
                Flag flag = flagService.createFlag(CP_OPINION_BOARD_FLAG, entity.getCpOpinionSeq());
                logger.info("플래그 생성 및 저장 완료");

                // 2. Keyword 생성 및 저장
                keywordService.saveKeywords(requestBody.getKeywordList(), flag.getFlagSeq());
                logger.info("키워드 생성 및 저장 완료");
            }
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            logger.error("데이터베이스 저장 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("예기치 않은 오류가 발생했습니다.", e);
        }

        // Entity -> DTO 변환
        logger.info("CP 의견 DTO로 변환 중: {}", entity);
        dto = CpOpinionDTO.toDto(entity);

        return dto;
    }

    /**
     * CP 의견의 콘텐츠에 포함된 이미지를 Base64 형식 URL로 교체합니다.
     *
     * @param entity           업데이트할 CP 의견 엔티티
     * @param cpOpinionContent 기존 CP 의견 콘텐츠
     */
    private void updateCpOpinionContentWithImage(CpOpinion entity, String cpOpinionContent) {
        String updatedContent = pictureService.replaceBase64WithUrls(
                cpOpinionContent,
                CP_OPINION_BOARD_FLAG,
                entity.getCpOpinionSeq()
        );
        logger.info("변환 완료");

        entity.updateCpOpinionContent(updatedContent);
        logger.info("업데이트 완료");

        cpOpinionRepository.save(entity);
        logger.info("저장 완료");
    }

    /**
     * 주어진 cpOpinionSeq에 해당하는 CP 의견을 삭제합니다.
     *
     * <p>이 메서드는 다음과 같은 과정을 수행합니다:</p>
     * <ol>
     *     <li>주어진 ID로 데이터베이스에서 CP 의견을 조회하고 권한을 확인합니다.</li>
     *     <li>존재하지 않는 경우, {@link CustomException}을 던집니다.</li>
     *     <li>이미 삭제된 의견인 경우, {@link CustomException}을 던집니다.</li>
     *     <li>현재 사용자와 작성자가 다르고, 사용자가 관리자 권한이 아닐 경우, {@link CustomException}을 던집니다.</li>
     *     <li>의견을 삭제합니다.</li>
     * </ol>
     *
     * @param cpOpinionSeq 삭제할 CP 의견의 ID
     * @throws CustomException  유효하지 않은 ID일 경우 또는 삭제 중 오류가 발생할 경우
     * @throws RuntimeException 기타 예기치 않은 오류가 발생할 경우
     */
    @Transactional
    public void deleteByCpOpinionSeq(long cpOpinionSeq) {
        logger.info("CP 의견 삭제 요청. CP 의견 ID: {}", cpOpinionSeq);

        // DB 조회 및 권한 확인
        CpOpinion entity = getCpOpinionAndCheckUnauthorizedAccess(cpOpinionSeq);

        try {
            // 1. 키워드 삭제
            keywordService.deleteKeywords(CP_OPINION_BOARD_FLAG, entity.getCpOpinionSeq());
            logger.info("{}번 CP 의견에 해당하는 Flag를 삭제하였습니다.", entity.getCpOpinionSeq());

            // 2. 사진 삭제
            pictureService.deletePictures(CP_OPINION_BOARD_FLAG, entity.getCpOpinionSeq());
            logger.info("{}번 CP 의견에 해당하는 사진을 삭제하였습니다.", entity.getCpOpinionSeq());

            // 3. 북마크 삭제
            deleteBookmark(entity);

            // 4. CP 의견 투표 삭제
            deleteCpOpinionVote(entity);

            // 5. CP 의견 삭제 처리
            entity.delete();
            // 저장
            cpOpinionRepository.save(entity);
            logger.info("CP 의견 ID: {}가 삭제되었습니다.", cpOpinionSeq);
        } catch (DataAccessException e) {
            logger.error("CP 의견 삭제 중 데이터베이스 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_IO_DELETE_ERROR);
        } catch (Exception e) {
            logger.error("CP 의견 삭제 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("CP 의견 삭제 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }

    /**
     * 주어진 CP 의견에 대한 모든 투표를 삭제합니다.
     *
     * @param entity 삭제할 투표가 있는 CP 의견 엔티티
     * @throws CustomException 데이터베이스 접근 중 오류가 발생한 경우
     */
    @Transactional
    public void deleteCpOpinionVote(CpOpinion entity) {
        List<Long> seqList = cpOpinionVoteService.getCpOpinionVoteSeqByCpOpinionSeq(entity.getCpOpinionSeq());

        for (long seq : seqList) {
            cpOpinionVoteService.deleteCpOpinionVote(seq);
        }
    }

    /**
     * 현재 사용자의 권한에 따라 주어진 CP 의견에 대해 북마크를 삭제합니다.
     * <p>
     * 이 메서드는 작성자가 자신의 CP 의견의 북마크를 삭제하거나,
     * 어드민이 다른 사용자의 CP 의견의 북마크를 삭제할 수 있도록 합니다.
     *
     * @param entity 삭제할 북마크가 있는 CP 의견 엔티티
     */
    private void deleteBookmark(CpOpinion entity) {

        String currentUserAuthorities = SecurityUtil.getCurrentUserAuthorities();

        if (currentUserAuthorities.equals(UserAuth.USER)) {
            // 1. 작성자가 삭제하는 경우
            if (bookmarkService.isBookmarked(CP_OPINION_BOARD_FLAG, entity.getCpOpinionSeq(), SecurityUtil.getCurrentUserId())) {
                // 북마크가 된 경우
                bookmarkService.deleteBookmarkByFlag(CP_OPINION_BOARD_FLAG, entity.getCpOpinionSeq());
                logger.info("{}번 CP 의견의 북마크를 삭제했습니다.", entity.getCpOpinionSeq());
            }
        } else {
            // 2. 어드민이 삭제하는 경우
            // 작성자 정보 호출
            String entityUserId = userService.findUser(entity.getUserSeq()).getUserId();

            if (bookmarkService.isBookmarked(CP_OPINION_BOARD_FLAG, entity.getCpOpinionSeq(), entityUserId)) {
                // 북마크가 된 경우
                bookmarkService.toggleBookmark(CP_OPINION_BOARD_FLAG, entity.getCpOpinionSeq(), SecurityUtil.getCurrentUserId());
                logger.info("{}번 CP 의견의 북마크를 삭제했습니다.", entity.getCpOpinionSeq());
            }
        }
    }

    /**
     * CP 의견을 조회하고, 현재 사용자의 접근 권한을 검증하는 메서드입니다.
     *
     * <p>이 메서드는 주어진 CP 의견의 고유 식별자로 해당 의견을 조회합니다.
     * 사용자가 해당 의견을 조회할 권한이 있는지 검증하며, 권한이 없거나
     * 의견이 존재하지 않는 경우 적절한 예외를 발생시킵니다.</p>
     *
     * @param cpOpinionSeq CP 의견의 고유 식별자
     * @return CpOpinion 조회된 CP 의견 객체. 의견이 존재하고 삭제되지 않은 경우.
     * @throws CustomException  데이터베이스 접근 오류가 발생한 경우,
     *                          의견이 존재하지 않는 경우, 또는 사용자가 권한이 없는 경우.
     * @throws RuntimeException 다른 예기치 않은 오류가 발생할 경우
     */
    @Transactional(readOnly = true)
    public CpOpinion getCpOpinionAndCheckUnauthorizedAccess(long cpOpinionSeq) {

        // DB에서 CP 의견 조회
        CpOpinion entity = cpOpinionRepository.findById(cpOpinionSeq)
                .orElseThrow(() -> {
                    logger.warn("조회된 CP 의견이 없습니다. CP 의견 번호: {}", cpOpinionSeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_OPINION); // 의견이 존재하지 않을 경우 예외 발생
                });

        // 삭제 상태 확인
        if (entity.getDeletedAt() != null) {
            logger.warn("CP 의견 ID: {}는 이미 삭제된 의견입니다.", cpOpinionSeq);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
        }

        // 로그인 유저 번호 및 권한 확인
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();
        String currentUserAuthority = SecurityUtil.getCurrentUserAuthorities();
        if (currentUserSeq != entity.getUserSeq() && !currentUserAuthority.equals(UserAuth.ADMIN)) {
            logger.warn("사용자 시퀀스: {}가 CP 의견 ID: {}를 조회할 권한이 없습니다.", currentUserSeq, cpOpinionSeq);
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        return entity; // 권한이 확인된 경우, CP 의견 반환
    }

    /**
     * 주어진 CP 의견 ID에 해당하는 CP 의견을 업데이트합니다.
     *
     * @param cpOpinionSeq 업데이트할 CP 의견의 ID
     * @param requestBody  CP 의견 업데이트에 필요한 요청 본문
     * @return 업데이트된 CP 의견의 DTO
     * @throws CustomException 유효성 검사 실패, 의견이 존재하지 않거나 데이터베이스 오류가 발생할 경우
     */
    @Transactional
    public CpOpinionDTO updateCpOpinionByCpOpinionSeq(long cpOpinionSeq, RequestCpOpinionDTO requestBody, List<MultipartFile> imageList) {

        validateRequestCpOpinion(requestBody); // requestBody 유효성 검사

        // DB에서 현재 CP 의견 조회 및 작성자 확인
        CpOpinion entity = getCpOpinionAndCheckUnauthorizedAccess(cpOpinionSeq);

        // 요청 본문으로부터 CP 의견 내용 업데이트
        if (requestBody.getCpOpinionContent() != null) {
            entity.updateCpOpinionContent(requestBody.getCpOpinionContent()); // 의견 내용을 업데이트
        }

        // 키워드 업데이트
        if (requestBody.getKeywordList() != null) {
            keywordService.updateKeywords(
                    requestBody.getKeywordList(),
                    CP_OPINION_BOARD_FLAG,
                    entity.getCpOpinionSeq());
        }

        // 본문 1차 업데이트
        entity.updateCpOpinionContent(requestBody.getCpOpinionContent());
        cpOpinionRepository.save(entity);
        logger.info("{}번 CP 의견 수정 과정 중, 본문 1차 업데이트 성공", entity.getCpOpinionSeq());

        // 기존 사진 제거
        Flag flag = flagService.findFlag(CP_OPINION_BOARD_FLAG, entity.getCpOpinionSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));
        pictureService.deletePictures(flag);
        logger.info("{}번 CP 의견 수정 과정 중, 기존 사진 정보 제거", entity.getCpOpinionSeq());

        // 사진 업데이트 -> 본문 2차 업데이트
        updateCpOpinionContentWithImage(entity, requestBody.getCpOpinionContent());
        logger.info("{}번 CP 의견 수정 과정 중, 본문 2차 업데이트 성공", entity.getCpOpinionSeq());

        try {
            // 업데이트된 의견을 DB에 저장
            entity = cpOpinionRepository.save(entity);
            logger.info("CP 의견이 성공적으로 업데이트되었습니다. cpOpinionSeq: {}", cpOpinionSeq);
        } catch (DataAccessException e) {
            logger.error("데이터베이스 업데이트 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (Exception e) {
            throw new RuntimeException("예기치 않은 오류가 발생했습니다.", e);
        }

        // Entity -> DTO 변환
        return CpOpinionDTO.toDto(entity); // 업데이트된 CP 의견의 DTO를 반환
    }

    /**
     * CP 의견 요청 본문의 유효성을 검사합니다.
     * <p>
     * 이 메서드는 CP 의견 요청 본문(`requestBody`)의 유효성을 검사하며,
     * 다음 조건을 확인합니다:
     * 1. `requestBody`가 null인지 확인합니다.
     * 2. `cpOpinionContent`가 null인지 확인합니다.
     * 3. `cpOpinionContent`는 비어 있지 않아야 하며,
     * 공백만으로 이루어진 문자열은 허용되지 않습니다.
     * 4. `cpOpinionContent`의 길이는 1자 이상 65,535자 이하이어야 합니다.
     * (MariaDB의 TEXT 타입에 맞춰 설정)
     *
     * @param requestBody 검사할 CP 의견 요청 본문
     * @throws CustomException 유효성 검사 실패 시 발생하며,
     *                         필수 필드가 누락된 경우에 해당합니다.
     */
    private void validateRequestCpOpinion(RequestCpOpinionDTO requestBody) {
        if (requestBody == null || requestBody.getCpOpinionContent() == null ||
                !requestBody.getCpOpinionContent().matches("^(?!\\s*$).{1,65535}$")) {
            logger.warn("입력값 유효성 검사 실패: requestBody = {}, cpOpinionContent = {}", requestBody, requestBody != null ? requestBody.getCpOpinionContent() : "null");
            throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
        }
    }

    /**
     * 주어진 CP 의견 시퀀스에 대해 북마크를 토글합니다.
     * <p>
     * 이 메서드는 주어진 CP 의견 시퀀스에 대해 플래그를 생성한 후,
     * 현재 사용자의 북마크 상태를 변경합니다.
     *
     * @param cpOpinionSeq 북마크를 설정할 CP 의견 시퀀스
     * @return 북마크 상태가 변경된 결과 (true: 북마크됨, false: 북마크 해제됨)
     */
    public boolean cpOpinionBookmark(long cpOpinionSeq) {
        // 플래그 생성
        flagService.createFlag(CP_OPINION_BOARD_FLAG, cpOpinionSeq);
        logger.info("CP 의견 {}의 플래그를 생성했습니다.", cpOpinionSeq);

        // 북마크 토글
        boolean newBookmarkState = bookmarkService.toggleBookmark(CP_OPINION_BOARD_FLAG, cpOpinionSeq, SecurityUtil.getCurrentUserId());
        logger.info("CP 의견 {}의 북마크 상태가 {} 되었습니다.", cpOpinionSeq, newBookmarkState ? "북마크됨" : "북마크 해제됨");

        return newBookmarkState;
    }

    public List<ResponseCpOpinionDTO> getBookmarkedCpOpinion() {
        User user = userService.findUser(SecurityUtil.getCurrentUserSeq());
        logger.info("사용자 {}의 북마크된 CP 버전을 조회합니다.", user.getUserId());

        List<BookmarkDTO> bookmarkDtoList = bookmarkService.findByUserAndFlagType(user, CP_OPINION_BOARD_FLAG);
        logger.info("사용자 {}의 북마크 목록을 {}개 찾았습니다.", user.getUserId(), bookmarkDtoList.size());

        List<ResponseCpOpinionDTO> responseCpOpinionDTOList = bookmarkDtoList.stream()
                .map(bookmarkDto ->
                        cpOpinionRepository.findByCpOpinionSeq(bookmarkDto.getFlag().getFlagEntitySeq())
                                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CP_VERSION))
                )
                .toList();


        logger.info("사용자 {}의 북마크된 CP 의견 조회가 완료되었습니다.", user.getUserId());
        return responseCpOpinionDTOList;
    }
}
