package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpSearchCategoryDataDTO;
import mediHub_be.cp.dto.ResponseSimpleCpSearchCategoryDataDTO;
import mediHub_be.cp.entity.CpSearchCategoryData;
import mediHub_be.cp.repository.CpSearchCategoryDataRepository;
import mediHub_be.security.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpSearchCategoryDataService {

    // Service
    private final CpSearchCategoryDataRepository cpSearchCategoryDataRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpSearchCategoryService");     // Logger

    /**
     * 주어진 CP 검색 카테고리 ID에 대한 데이터 목록을 조회합니다.
     *
     * <p>이 메서드는 CP 검색 카테고리의 ID를 기반으로 해당 카테고리의 데이터를 데이터베이스에서 조회합니다.
     * 데이터가 존재하지 않는 경우 예외를 발생시킵니다.</p>
     *
     * @param cpSearchCategorySeq 조회할 CP 검색 카테고리의 ID
     * @return List<ResponseCpSearchCategoryDataDTO> 해당 카테고리의 데이터 목록
     * @throws CustomException 데이터가 없거나 데이터 접근 오류 발생 시 예외
     */
    public List<ResponseCpSearchCategoryDataDTO> getCpSearchCategoryDataListByCpSearchCategorySeq(long cpSearchCategorySeq) {
        logger.info("CP 검색 카테고리 데이터 조회 요청 시작: ID={}", cpSearchCategorySeq);

        List<ResponseCpSearchCategoryDataDTO> dtoList;

        // DB 조회
        try {
            dtoList = cpSearchCategoryDataRepository.findByCpSearchCategorySeq(cpSearchCategorySeq); // 데이터 조회

//            if (dtoList.isEmpty()) {
//                logger.info("CP 검색 카테고리 데이터 조회 결과가 비어 있습니다: ID={}", cpSearchCategorySeq);
//                throw new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA);
//            }

            logger.info("CP 검색 카테고리 데이터 조회 성공: ID={} 데이터 수={}", cpSearchCategorySeq, dtoList.size());

        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("CP 검색 카테고리 데이터 조회 중 예상치 못한 에러가 발생했습니다.", e);
        }

        return dtoList; // 정상적으로 조회된 데이터 반환
    }


    /**
     * 주어진 CP 검색 카테고리 ID와 데이터 ID에 대한 카테고리 데이터를 조회합니다.
     *
     * <p>이 메서드는 CP 검색 카테고리의 데이터 ID를 기반으로 해당 데이터를 데이터베이스에서 조회합니다.
     * 데이터가 존재하지 않는 경우 예외를 발생시킵니다.</p>
     *
     * @param cpSearchCategorySeq     조회할 CP 검색 카테고리의 ID
     * @param cpSearchCategoryDataSeq 조회할 CP 검색 카테고리 데이터의 ID
     * @return ResponseCpSearchCategoryDataDTO 해당 카테고리 데이터의 정보
     * @throws CustomException 데이터가 없거나 데이터 접근 오류 발생 시 예외
     */
    public ResponseCpSearchCategoryDataDTO getCpSearchCategoryData(long cpSearchCategorySeq, long cpSearchCategoryDataSeq) {
        logger.info("CP 검색 카테고리 데이터 조회 시작: cpSearchCategorySeq={}, cpSearchCategoryDataSeq={}", cpSearchCategorySeq, cpSearchCategoryDataSeq);

        try {
            // 데이터 조회
            ResponseCpSearchCategoryDataDTO dto = cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq)
                    .orElseThrow(() -> {
                        logger.warn("CP 검색 카테고리 데이터 조회 결과가 비어 있습니다: ID={}", cpSearchCategoryDataSeq);
                        return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA);
                    });

            logger.info("CP 검색 카테고리 데이터 조회 성공: {}", dto);

            return dto;
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("CP 검색 카테고리 조회 시 예기치 못한 에러가 발생했습니다.", e);
        }
    }

    /**
     * 새로운 CP 검색 카테고리 데이터를 등록합니다.
     *
     * @param cpSearchCategorySeq      생성할 CP 검색 카테고리의 시퀀스
     * @param cpSearchCategoryDataName 생성할 CP 검색 카테고리 데이터의 이름
     * @return 생성된 CP 검색 카테고리 데이터의 DTO
     * @throws CustomException 유효성 검사 실패, 중복 데이터 또는 데이터베이스 오류가 발생할 경우
     */
    public ResponseCpSearchCategoryDataDTO createCpSearchCategoryData(long cpSearchCategorySeq, String cpSearchCategoryDataName) {
        // 1. 유효성 검사 및 중복 검사
        cpSearchCategoryDataName = validateAndCheckDuplicateData(cpSearchCategoryDataName);
        logger.info("유효성 검사 및 중복 검사 통과");

        // 2. 데이터 저장
        CpSearchCategoryData entity = CpSearchCategoryData.builder()
                .userSeq(SecurityUtil.getCurrentUserSeq())
                .cpSearchCategorySeq(cpSearchCategorySeq)
                .cpSearchCategoryDataName(cpSearchCategoryDataName)
                .build();
        logger.info("생성할 정보 = {}", entity);

        try {
            entity = cpSearchCategoryDataRepository.save(entity); // 저장 후 반환된 엔티티를 업데이트
            logger.info("생성 완료");
        } catch (DataAccessException e) {
            logger.error("CP 검색 카테고리 데이터 저장 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        }

        // 3. 데이터 반환
        return cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeq(entity.getCpSearchCategoryDataSeq())
                .orElseThrow(() -> {
                    logger.warn("CP 검색 카테고리 데이터 생성 시 에러가 발생했습니다. cpSearchCategorySeq={}", cpSearchCategorySeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA);
                });
    }

    /**
     * CP 검색 카테고리 데이터를 업데이트합니다.
     *
     * <p>주어진 ID에 해당하는 CP 검색 카테고리 데이터를 찾아 이름을 업데이트합니다.
     * 유효성 검사 및 중복 확인을 수행하며, 업데이트된 데이터의 정보를 반환합니다.</p>
     *
     * @param cpSearchCategoryDataSeq 수정할 CP 검색 카테고리 데이터의 ID
     * @param requestBody             새로운 CP 검색 카테고리 데이터 이름
     * @return ResponseCpSearchCategoryDataDTO 업데이트된 CP 검색 카테고리 데이터의 정보
     * @throws CustomException 유효성 검사 실패, 카테고리 데이터 없음, 또는 데이터베이스 오류 발생 시 예외
     */
    public ResponseCpSearchCategoryDataDTO updateCpSearchCategoryDataData(long cpSearchCategoryDataSeq, String requestBody) {
        logger.info("CP 검색 카테고리 데이터 업데이트 요청: Seq={} 이름={}", cpSearchCategoryDataSeq, requestBody);

        // 1. 유효성 검사
        requestBody = validateAndCheckDuplicateData(requestBody);
        logger.info("유효성 검사 완료");

        // 2. 수정할 데이터 찾기
        CpSearchCategoryData entity = cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq)
                .orElseThrow(() -> {
                    logger.warn("CP 검색 카테고리 데이터 찾기 실패: ID={}", cpSearchCategoryDataSeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA);
                });

        // 카테고리 데이터 업데이트
        updateCpSearchCategoryData(entity, requestBody);

        // 3. 데이터 저장
        try {
            cpSearchCategoryDataRepository.save(entity); // 수정된 엔티티를 데이터베이스에 저장
            logger.info("CP 검색 카테고리 데이터 업데이트 성공: ID={}", cpSearchCategoryDataSeq);
        } catch (DataAccessException e) {
            logger.error("CP 검색 카테고리 데이터 업데이트 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (Exception e) {
            logger.error("CP 검색 카테고리 데이터 업데이트 중 예기치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("CP 검색 카테고리 데이터 업데이트 중 예기치 못한 오류 발생했습니다.", e);
        }

        // 4. 반환
        return cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq)
                .orElseThrow(() -> {
                    logger.warn("CP 검색 카테고리 데이터 업데이트 과정에서 업데이트 후, 조회 과정에서 에러가 발생했습니다.");
                    return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA);
                });
    }

    /**
     * CP 검색 카테고리 데이터를 삭제합니다.
     *
     * @param cpSearchCategoryDataSeq 삭제할 CP 검색 카테고리 데이터의 ID
     * @throws CustomException 데이터를 찾지 못한 경우 발생
     */
    public void deleteCpSearchCategoryData(long cpSearchCategoryDataSeq) {
        // 1. 조회
        CpSearchCategoryData entity = cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq)
                .orElseThrow(() -> {
                    logger.warn("CP 검색 카테고리 데이터 찾기 실패: ID={}", cpSearchCategoryDataSeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA);
                });

        // 2. CP 검색 카테고리 데이터 삭제 요청
        logger.info("CP 검색 카테고리 데이터 삭제 요청: ID={}", cpSearchCategoryDataSeq);

        // 3. 삭제(삭제일 추가)
        entity.updateUserSeq(SecurityUtil.getCurrentUserSeq()); // 현재 사용자 ID 업데이트
        entity.delete(); // 삭제 처리

        // 4. 저장
        try {
            cpSearchCategoryDataRepository.save(entity); // 수정된 엔티티를 데이터베이스에 저장
            logger.info("CP 검색 카테고리 데이터 삭제 성공: ID={}", cpSearchCategoryDataSeq);
        } catch (DataAccessException e) {
            logger.error("CP 검색 카테고리 데이터 삭제 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (Exception e) {
            logger.error("CP 검색 카테고리 데이터 삭제 중 에상치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("CP 검색 카테고리 데이터 삭제 중 에상치 못한 오류 발생했습니다.", e);
        }
    }

    /**
     * CP 검색 카테고리 데이터의 유효성을 검사하고 중복 여부를 확인합니다.
     *
     * @param cpSearchCategoryDataName 생성할 CP 검색 카테고리 데이터의 이름
     * @throws CustomException 유효성 검사 실패 시 REQUIRED_FIELD_MISSING 예외 발생,
     *                         중복 데이터가 발견될 경우 DUPLICATE_CP_SEARCH_CATEGORY_DATA_NAME 예외 발생
     */
    private String validateAndCheckDuplicateData(String cpSearchCategoryDataName) {

        if (cpSearchCategoryDataName == null || cpSearchCategoryDataName.trim().isEmpty()) {
            throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
        }

        // 중복 검사 로직
        boolean exists = cpSearchCategoryDataRepository.existsByCpSearchCategoryDataName(cpSearchCategoryDataName);
        if (exists) {
            throw new CustomException(ErrorCode.DUPLICATE_CP_SEARCH_CATEGORY_DATA_NAME);
        }

        return cpSearchCategoryDataName.trim();
    }

    /**
     * CP 검색 카테고리 데이터 엔티티의 데이터를 업데이트합니다.
     *
     * @param entity                   수정할 CP 검색 카테고리 데이터 엔티티
     * @param cpSearchCategoryDataName 새로운 CP 검색 카테고리 데이터 이름
     */
    public void updateCpSearchCategoryData(CpSearchCategoryData entity, String cpSearchCategoryDataName) {
        entity.updateUserSeq(SecurityUtil.getCurrentUserSeq()); // 현재 사용자 ID 업데이트
        entity.updateCpSearchCategoryDataName(cpSearchCategoryDataName); // 데이터 이름 업데이트
        logger.info("CP 검색 카테고리 데이터 업데이트: ID={} 이름={}", entity.getCpSearchCategoryDataSeq(), cpSearchCategoryDataName);
    }

    /**
     * 주어진 검색 카테고리 시퀀스에 해당하는 CP 검색 카테고리 데이터를 조회합니다.
     * <p>
     * 이 메서드는 삭제되지 않은(`deletedAt`이 null인) 카테고리 데이터를 반환합니다.
     *
     * @param cpSearchCategorySeq 검색 카테고리 시퀀스
     * @return 삭제되지 않은 CP 검색 카테고리 데이터 목록
     */
    public List<ResponseSimpleCpSearchCategoryDataDTO> getCpSearchCategoryDataListByCpSearchCategorySeqAndDeletedAtIsNull(long cpSearchCategorySeq) {
        logger.info("CP 검색 카테고리 시퀀스 {}에 대한 데이터 목록을 조회합니다.", cpSearchCategorySeq);

        List<ResponseSimpleCpSearchCategoryDataDTO> result = cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeqAndDeletedAtIsNull(cpSearchCategorySeq);

        logger.info("CP 검색 카테고리 시퀀스 {}에 대한 데이터 목록이 {}개 조회되었습니다.", cpSearchCategorySeq, result.size());

        return result;
    }
}
