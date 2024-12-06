package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.CpOpinionDTO;
import mediHub_be.cp.dto.RequestCpOpinionDTO;
import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.dto.ResponseCpOpinionWithKeywordListDTO;
import mediHub_be.cp.entity.CpOpinion;
import mediHub_be.cp.repository.CpOpinionRepository;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpOpinionService {

    // Service
    private final FlagService flagService;
    private final KeywordService keywordService;

    // Repository
    private final CpOpinionRepository cpOpinionRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpOpinionService");    // Logger
    private final FlagRepository flagRepository;
    private final KeywordRepository keywordRepository;

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
    public List<ResponseCpOpinionDTO> findCpOpinionListByCpVersionSeq(
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
            setKeywordListForCpOpinions(responseCpOpinionDtoList);

            // 결과 확인
            if (responseCpOpinionDtoList.isEmpty()) {
                logger.warn("조회된 CP 의견이 없습니다. CP 버전 번호: {}, CP 의견 위치 번호: {}", cpVersionSeq, cpOpinionLocationSeq);
                throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
            } else {
                logger.info("조회된 CP 의견의 수: {}", responseCpOpinionDtoList.size());
            }

            return responseCpOpinionDtoList;
        } catch (CustomException e) {
            logger.error("사용자 정의 예외 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류: {}", e.getMessage());
            throw new RuntimeException("데이터베이스 접근 오류가 발생했습니다.", e);
        } catch (Exception e) {
            logger.error("예기치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("예기치 못한 오류가 발생했습니다.", e);
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
    public List<ResponseCpOpinionWithKeywordListDTO> setKeywordListForCpOpinions(List<ResponseCpOpinionDTO> responseCpOpinionDtoList) {
        List<ResponseCpOpinionWithKeywordListDTO> resultList = new ArrayList<>();
        logger.info("CP 의견 DTO 리스트에 대한 키워드 목록 설정 시작. 총 의견 수: {}", responseCpOpinionDtoList.size());

        try {
            for (ResponseCpOpinionDTO dto : responseCpOpinionDtoList) {
                logger.info("CP 의견 번호: {}에 대한 키워드 목록 설정 중...", dto.getCpOpinionSeq());
                ResponseCpOpinionWithKeywordListDTO resultDto = setKeywordForCpOpinion(dto);
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
     * @throws CustomException 사용자 정의 예외가 발생한 경우
     * @throws RuntimeException 기타 예기치 않은 오류가 발생한 경우
     */
    @Transactional(readOnly = true)
    public ResponseCpOpinionWithKeywordListDTO setKeywordForCpOpinion(ResponseCpOpinionDTO dto) {
        logger.info("CP 의견 번호: {}에 대한 키워드 목록을 설정합니다.", dto.getCpOpinionSeq());

        try {
            // 키워드 리스트 조회
            List<Keyword> keywordList = keywordRepository.findByBoardFlagAndPostSeq(FlagService.CP_OPINION_BOARD_FLAG, dto.getCpOpinionSeq());

            // CP 의견 DTO에 키워드 목록을 설정 및 반환
            return ResponseCpOpinionWithKeywordListDTO.create(dto, keywordList);
        } catch (DataAccessException e) {
            // 데이터 접근 예외 발생 시 로그 남기기
            logger.error("데이터베이스 접근 오류: {}", e.getMessage());
            throw e; // 필요에 따라 다시 던질 수 있음
        } catch (CustomException e) {
            // 사용자 정의 예외 발생 시 로그 남기기
            logger.error("사용자 정의 예외 발생: {}", e.getMessage());
            throw e; // 필요에 따라 다시 던질 수 있음
        } catch (Exception e) {
            // 일반 예외 발생 시 로그 남기기
            logger.error("예기치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("예기치 못한 오류가 발생했습니다.", e); // 새로운 런타임 예외로 감싸서 던짐
        }
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
        ResponseCpOpinionDTO result = cpOpinionRepository.findByCpOpinionSeq(cpOpinionSeq);

        if (result == null) {
            logger.warn("조회된 CP 의견이 없습니다. CP 의견 번호: {}", cpOpinionSeq);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
        }

        // 키워드 조회
        logger.info("CP 의견 번호: {}에 대한 키워드 목록을 조회합니다.", cpOpinionSeq);
        setKeywordForCpOpinion(result);

        if (result.getDeletedAt() == null) {
            // 활성 상태
            logger.info("CP 의견 번호: {}는 활성 상태입니다.", cpOpinionSeq);
            incrementViewCount(cpOpinionSeq); // 조회수 증가 메서드 호출
            logger.info("CP 의견 번호: {}의 조회수가 증가했습니다.", cpOpinionSeq);
            return result;
        } else {
            // 삭제 상태
            logger.info("CP 의견 번호: {}는 삭제된 상태입니다.", cpOpinionSeq);
            if (isAdminUser()) {
                logger.info("관리자 권한으로 CP 의견 번호: {}를 조회합니다.", cpOpinionSeq);
                return result;
            } else {
                logger.warn("CP 의견 번호: {}에 대한 접근 권한이 없습니다.", cpOpinionSeq);
                throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
            }
        }
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
    public void incrementViewCount(long cpOpinionSeq) {
        cpOpinionRepository.incrementViewCount(cpOpinionSeq);
    }

    /**
     * 주어진 CP 버전 번호와 CP 의견 위치 번호에 따라 새로운 CP 의견을 생성합니다.
     *
     * @param cpVersionSeq         CP 버전 번호
     * @param cpOpinionLocationSeq CP 의견 위치 번호
     * @param requestBody          CP 의견을 생성하기 위한 요청 본문
     * @return 생성된 CP 의견의 DTO
     * @throws CustomException 입력값이 유효하지 않거나 데이터베이스 오류가 발생할 경우
     */
    @Transactional
    public CpOpinionDTO createCpOpinion(
            long cpVersionSeq,
            long cpOpinionLocationSeq,
            RequestCpOpinionDTO requestBody) {

        // 입력값 유효성 검사
        validateRequestCpOpinion(requestBody);

        // 입력값으로 DTO 생성
        logger.info("CP 의견 DTO 생성 중: cpOpinionLocationSeq = {}, 요청 본문 = {}", cpOpinionLocationSeq, requestBody);
        CpOpinionDTO cpOpinionDTO = CpOpinionDTO.create(cpOpinionLocationSeq, requestBody);

        // DTO -> Entity 변환
        logger.info("CP 의견 Entity 변환 중: {}", cpOpinionDTO);
        CpOpinion cpOpinion = CpOpinion.toEntity(cpOpinionDTO);

        try {
            // DB에 저장하고 해당 값을 다시 받아옴.
            logger.info("DB에 CP 의견 저장 중: {}", cpOpinion);
            cpOpinion = cpOpinionRepository.save(cpOpinion);
            logger.info("CP 의견이 DB에 성공적으로 저장되었습니다: {}", cpOpinion);
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            logger.error("데이터베이스 저장 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        }

        // Entity -> DTO 변환
        logger.info("CP 의견 DTO로 변환 중: {}", cpOpinion);
        cpOpinionDTO = CpOpinionDTO.toDto(cpOpinion);

        // 키워드 등록
        if (requestBody.getKeywordList() != null && !requestBody.getKeywordList().isEmpty()) {

            try {
                // 1. Flag 생성
                Flag flag = Flag.builder()
                        .flagSeq(null)
                        .flagBoardFlag(FlagService.CP_OPINION_BOARD_FLAG)
                        .flagPostSeq(cpOpinion.getCpOpinionSeq())
                        .build();

                flag = flagRepository.save(flag);

                // 2. Keyword 생성
                keywordService.saveKeywords(
                        requestBody.getKeywordList(),
                        flag.getFlagSeq());
            } catch (DataAccessException e) {
                // 데이터 접근 오류 처리
                logger.error("데이터베이스 접근 오류: {}", e.getMessage());
                throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
            } catch (CustomException e) {
                // 사용자 정의 예외 처리
                logger.error("사용자 정의 예외 발생: {}", e.getMessage());
                throw e; // 사용자 정의 예외는 그대로 던짐
            } catch (Exception e) {
                // 일반 예외 처리
                logger.error("예기치 못한 오류 발생: {}", e.getMessage());
                throw new RuntimeException("예기치 못한 오류가 발생했습니다.", e);
            }

        }

        return cpOpinionDTO;
    }

    /**
     * 주어진 cpOpinionSeq에 해당하는 CP 의견을 삭제합니다.
     *
     * @param cpOpinionSeq 삭제할 CP 의견의 ID
     * @throws CustomException 유효하지 않은 ID일 경우 또는 삭제 중 오류가 발생할 경우
     */
    @Transactional
    public void deleteCpOpinionByCpOpinionSeq(long cpOpinionSeq) {
        // 입력값 유효성 검사
        validateCpOpinionSeq(cpOpinionSeq);

        try {
            // CP 의견 조회 및 작성자 확인
            CpOpinion cpOpinion = getCpOpinionAndCheckUnauthorizedAccess(cpOpinionSeq);

            // CP 의견 삭제
            cpOpinionRepository.deleteById(cpOpinionSeq);
            logger.info("CP 의견이 성공적으로 삭제되었습니다. cpOpinionSeq: {}", cpOpinionSeq);

            // 키워드 삭제
            keywordService.deleteKeywords(FlagService.CP_OPINION_BOARD_FLAG, cpOpinion.getCpOpinionSeq());
        } catch (DataAccessException e) {
            logger.error("데이터베이스 삭제 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        }
    }

    /**
     * 주어진 CP 의견 ID에 대한 접근 권한을 확인하고,
     * 해당 CP 의견을 반환합니다.
     *
     * @param cpOpinionSeq 확인할 CP 의견의 ID
     * @return CP 의견 객체
     * @throws CustomException - NOT_FOUND_CP_OPINION: 주어진 ID에 해당하는 CP 의견이 존재하지 않을 경우
     *                         - UNAUTHORIZED_USER: 현재 사용자 ID가 null이거나,
     *                         요청된 CP 의견의 작성자와 현재 사용자가 일치하지 않을 경우
     */
    @Transactional(readOnly = true)
    public CpOpinion getCpOpinionAndCheckUnauthorizedAccess(long cpOpinionSeq) {

        // DB에서 데이터 조회
        CpOpinion cpOpinion = cpOpinionRepository.findById(cpOpinionSeq)
                .orElseThrow(() -> {
                    logger.warn("조회된 CP 의견이 없습니다. CP 의견 번호: {}", cpOpinionSeq);
                    throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION); // 의견이 존재하지 않을 경우 예외 발생
                });

        // 로그인 유저 번호
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        // 작성자 확인
        if (currentUserSeq == null || !currentUserSeq.equals(cpOpinion.getUserSeq())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        return cpOpinion;
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
    public CpOpinionDTO updateCpOpinionByCpOpinionSeq(long cpOpinionSeq, RequestCpOpinionDTO requestBody) {
        // 입력값 유효성 검사
        validateCpOpinionSeq(cpOpinionSeq); // CP 의견 ID 유효성 검사
        validateRequestCpOpinion(requestBody); // 요청 본문 유효성 검사

        // DB에서 현재 CP 의견 조회 및 작성자 확인
        CpOpinion cpOpinion = getCpOpinionAndCheckUnauthorizedAccess(cpOpinionSeq);

        // 요청 본문으로부터 CP 의견 내용 업데이트
        if (requestBody.getCpOpinionContent() != null) {
            cpOpinion.editCpOpinionContent(requestBody.getCpOpinionContent()); // 의견 내용을 업데이트
        }

        // 키워드 업데이트
        if (requestBody.getKeywordList() != null) {
            keywordService.updateKeywords(
                    requestBody.getKeywordList(),
                    FlagService.CP_OPINION_BOARD_FLAG,
                    cpOpinion.getCpOpinionSeq());
        }

        try {
            // 업데이트된 의견을 DB에 저장
            cpOpinion = cpOpinionRepository.save(cpOpinion);
            logger.info("CP 의견이 성공적으로 업데이트되었습니다. cpOpinionSeq: {}", cpOpinionSeq);
        } catch (DataAccessException e) {
            logger.error("데이터베이스 업데이트 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // Entity -> DTO 변환
        return CpOpinionDTO.toDto(cpOpinion); // 업데이트된 CP 의견의 DTO를 반환
    }

    /**
     * 주어진 CP 의견 ID의 유효성을 검사합니다.
     *
     * @param cpOpinionSeq 검사할 CP 의견 ID
     * @throws CustomException 유효하지 않은 ID인 경우 예외를 발생시킵니다.
     */
    private void validateCpOpinionSeq(long cpOpinionSeq) {
        if (cpOpinionSeq <= 0) {
            logger.warn("유효하지 않은 cpOpinionSeq: {}", cpOpinionSeq);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION); // 예외 발생
        }
    }

    /**
     * CP 의견 요청 본문의 유효성을 검사합니다.
     *
     * @param requestBody 검사할 CP 의견 요청 본문
     * @throws CustomException 유효성 검사 실패 시 예외를 발생시킵니다.
     */
    private void validateRequestCpOpinion(RequestCpOpinionDTO requestBody) {
        // 입력값 유효성 검사
        // 1. requestBody가 null인지 확인합니다.
        // 2. cpOpinionContent가 null인지 확인합니다.
        // 3. cpOpinionContent는 비어 있지 않아야 하며,
        //    공백만으로 이루어진 문자열은 허용되지 않습니다.
        // 4. cpOpinionContent의 길이는 1자 이상 65,535자 이하이어야 합니다.
        //    (MariaDB의 TEXT 타입에 맞춰 설정)
        if (requestBody == null || requestBody.getCpOpinionContent() == null ||
                !requestBody.getCpOpinionContent().matches("^(?!\\s*$).{1,65535}$")) {
            logger.warn("입력값 유효성 검사 실패: requestBody = {}, cpOpinionContent = {}", requestBody, requestBody != null ? requestBody.getCpOpinionContent() : "null");
            throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
        }
    }
}
