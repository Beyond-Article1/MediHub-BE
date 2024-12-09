package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpSearchCategoryDTO;
import mediHub_be.cp.entity.CpSearchCategory;
import mediHub_be.cp.repository.CpSearchCategoryRepository;
import mediHub_be.security.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpSearchCategoryService {

    // Repository
    private final CpSearchCategoryRepository cpSearchCategoryRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpSearchCategoryService");     // Logger

    /**
     * 모든 CP 검색 카테고리를 조회하여 리스트로 반환합니다.
     *
     * @return CP 검색 카테고리 DTO 리스트
     * @throws CustomException NOT_FOUND_CP_SEARCH_CATEGORY:
     *                         조회 결과가 없을 경우 발생.
     */
    public List<ResponseCpSearchCategoryDTO> getCpSearchCategoryList() {
        logger.info("CP 검색 카테고리 조회 요청 시작");

        List<ResponseCpSearchCategoryDTO> dtoList;

        // DB 조회
        try {
            dtoList = cpSearchCategoryRepository.findJoinUserOnUserSeq();

            if (dtoList.isEmpty()) {
                logger.warn("CP 검색 카테고리 조회 결과가 비어 있습니다.");
                throw new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
            }

            logger.info("CP 검색 카테고리 조회 성공: {} 카테고리", dtoList.size());
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        if (dtoList.isEmpty()) {
            logger.info("CP 검색 카테고리 조회 결과가 비어 있습니다.");
            throw new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
        }

        logger.info("CP 검색 카테고리 조회 성공: {} 카테고리", dtoList.size());
        return dtoList;
    }

    /**
     * CP 검색 카테고리를 조회하는 서비스 메서드.
     *
     * @param cpSearchCategorySeq 조회할 CP 검색 카테고리의 고유 번호
     * @return ResponseCpSearchCategoryDTO 해당 카테고리의 정보
     * @throws CustomException 내부 데이터 접근 오류 또는 카테고리 미발견 시 예외 발생
     */
    public ResponseCpSearchCategoryDTO getCpSearchCategory(long cpSearchCategorySeq) {
        logger.info("CP 검색 카테고리 조회 시작: cpSearchCategorySeq={}", cpSearchCategorySeq);

        ResponseCpSearchCategoryDTO dto = null;

        try {
            dto = cpSearchCategoryRepository.findByCpSearchCategorySeq(cpSearchCategorySeq);
            logger.info("CP 검색 카테고리 조회 성공: {}", dto);
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 결과가 없을 경우 예외 처리
        if (dto == null) {
            logger.warn("CP 검색 카테고리 조회 결과가 없음: cpSearchCategorySeq={}", cpSearchCategorySeq);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
        }

        return dto;
    }

    /**
     * 새로운 CP 검색 카테고리를 등록합니다.
     *
     * @param cpSearchCategoryName 생성할 CP 검색 카테고리의 이름
     * @return 생성된 CP 검색 카테고리의 DTO
     * @throws CustomException 유효성 검사 실패, 중복 카테고리 또는 데이터베이스 오류가 발생할 경우
     */
    public ResponseCpSearchCategoryDTO createCpSearchCategory(String cpSearchCategoryName) {
        // 1. 유효성 검사 및 중복 검사
        validateAndCheckDuplicate(cpSearchCategoryName);

        // 2. 데이터 저장
        CpSearchCategory entity = CpSearchCategory.builder()
                .userSeq(SecurityUtil.getCurrentUserSeq())
                .cpSearchCategoryName(cpSearchCategoryName)
                .build();

        try {
            entity = cpSearchCategoryRepository.save(entity); // 저장 후 반환된 엔티티를 업데이트
        } catch (DataAccessException e) {
            logger.error("CP 검색 카테고리 저장 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        }

        // 3. 데이터 반환
        return cpSearchCategoryRepository.findByCpSearchCategorySeq(entity.getCpSearchCategorySeq());
    }

    /**
     * CP 검색 카테고리 이름의 유효성을 검사하고 중복 여부를 확인합니다.
     *
     * @param cpSearchCategoryName 확인할 검색 카테고리 이름
     * @throws CustomException 유효성 검사 실패 또는 중복 카테고리인 경우 발생
     */
    public void validateAndCheckDuplicate(String cpSearchCategoryName) {
        // 유효성 검사
        if (cpSearchCategoryName == null || cpSearchCategoryName.trim().isEmpty()) {
            throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
        }

        // 중복 검사
        if (existsByName(cpSearchCategoryName)) {
            throw new CustomException(ErrorCode.DUPLICATE_CP_SEARCH_CATEGORY_NAME);
        }
    }

    /**
     * 주어진 이름의 CP 검색 카테고리가 존재하는지 확인합니다.
     *
     * @param cpSearchCategoryName 확인할 검색 카테고리 이름
     * @return 존재 여부
     */
    public boolean existsByName(String cpSearchCategoryName) {
        return cpSearchCategoryRepository.findByCpSearchCategoryName(cpSearchCategoryName).isPresent();
    }


    /**
     * CP 검색 카테고리를 업데이트합니다.
     *
     * @param cpSearchCategorySeq 수정할 CP 검색 카테고리의 ID
     * @param cpSearchCategoryName 새로운 CP 검색 카테고리 이름
     * @return 업데이트된 CP 검색 카테고리의 DTO
     * @throws CustomException 유효성 검사 실패, 카테고리 없음, 또는 데이터베이스 오류가 발생할 경우
     */
    public ResponseCpSearchCategoryDTO updateCpSearchCategory(long cpSearchCategorySeq, String cpSearchCategoryName) {
        // 1. 유효성 검사 및 중복 확인
        validateAndCheckDuplicate(cpSearchCategoryName);
        logger.info("CP 검색 카테고리 업데이트 요청: ID={} 이름={}", cpSearchCategorySeq, cpSearchCategoryName);

        // 2. 수정
        CpSearchCategory entity = cpSearchCategoryRepository.findById(cpSearchCategorySeq)
                .orElseThrow(() -> {
                    logger.warn("CP 검색 카테고리 찾기 실패: ID={}", cpSearchCategorySeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
                });

        // 카테고리 데이터 업데이트
        updateCpSearchCategoryData(entity, cpSearchCategoryName);

        // 3. 데이터 저장
        try {
            cpSearchCategoryRepository.save(entity); // 수정된 엔티티를 데이터베이스에 저장
            logger.info("CP 검색 카테고리 업데이트 성공: ID={}", cpSearchCategorySeq);
        } catch (DataAccessException e) {
            logger.error("CP 검색 카테고리 업데이트 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        }

        // 4. 반환
        return cpSearchCategoryRepository.findByCpSearchCategorySeq(cpSearchCategorySeq);
    }

    /**
     * CP 검색 카테고리 엔티티의 데이터를 업데이트합니다.
     *
     * @param entity 수정할 CP 검색 카테고리 엔티티
     * @param cpSearchCategoryName 새로운 CP 검색 카테고리 이름
     */
    public void updateCpSearchCategoryData(CpSearchCategory entity, String cpSearchCategoryName) {
        entity.updateUserSeq(SecurityUtil.getCurrentUserSeq()); // 현재 사용자 ID 업데이트
        entity.updateCpSearchCategoryName(cpSearchCategoryName); // 카테고리 이름 업데이트
        logger.info("CP 검색 카테고리 데이터 업데이트: ID={} 이름={}", entity.getCpSearchCategorySeq(), cpSearchCategoryName);
    }
}
