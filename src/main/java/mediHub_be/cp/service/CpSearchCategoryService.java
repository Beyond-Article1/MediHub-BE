package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpSearchCategoryAndCpSearchCategoryDataDTO;
import mediHub_be.cp.dto.ResponseCpSearchCategoryDTO;
import mediHub_be.cp.dto.ResponseSimpleCpSearchCategoryDataDTO;
import mediHub_be.cp.entity.CpSearchCategory;
import mediHub_be.cp.repository.CpSearchCategoryDataRepository;
import mediHub_be.cp.repository.CpSearchCategoryRepository;
import mediHub_be.security.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpSearchCategoryService {

    // Service
    private final CpSearchCategoryDataService cpSearchCategoryDataService;

    // Repository
    private final CpSearchCategoryRepository cpSearchCategoryRepository;
    private final CpSearchCategoryDataRepository cpSearchCategoryDataRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpSearchCategoryService");     // Logger

    /**
     * CP 검색 카테고리 목록을 조회하는 서비스 메서드입니다.
     *
     * <p>이 메서드는 데이터베이스에서 모든 CP 검색 카테고리를 조회하여
     * 해당 카테고리의 정보를 리스트로 반환합니다.</p>
     *
     * @return ResponseCpSearchCategoryDTO 리스트, 카테고리가 존재하지 않을 경우 예외 발생
     * @throws CustomException 데이터베이스 접근 오류 또는 카테고리가 발견되지 않은 경우 예외 발생
     */
    public List<ResponseCpSearchCategoryDTO> getCpSearchCategoryList() {
        logger.info("CP 검색 카테고리 조회 요청 시작");

        List<ResponseCpSearchCategoryDTO> dtoList = null;

        // DB 조회
        try {
            dtoList = cpSearchCategoryRepository.findJoinUserOnUserSeq();
//            logger.info("조회된 결과 = {}", dtoList);
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("CP 검색 카테고리 리스트 조회 과정에서 예상치 못한 에러가 발생했습니다.", e);
        }

//        if (dtoList.isEmpty()) {
//            logger.info("CP 검색 카테고리 조회 결과가 비어 있습니다.");
//            throw new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
//        }

        logger.info("CP 검색 카테고리 조회 성공! 크기: {} ", dtoList.size());
        return dtoList;
    }

    /**
     * 주어진 CP 검색 카테고리의 고유 번호를 사용하여 해당 카테고리를 조회하는 서비스 메서드입니다.
     *
     * <p>이 메서드는 CP 검색 카테고리 번호를 기반으로 데이터베이스에서 카테고리 정보를 조회하며,
     * 카테고리가 존재하지 않거나 데이터 접근 중 오류가 발생하면 예외를 던집니다.</p>
     *
     * @param cpSearchCategorySeq 조회할 CP 검색 카테고리의 고유 번호
     * @return ResponseCpSearchCategoryDTO 해당 카테고리의 정보
     * @throws CustomException 내부 데이터 접근 오류 또는 카테고리가 발견되지 않은 경우 예외 발생
     */
    public ResponseCpSearchCategoryDTO getCpSearchCategoryByCpSearchCategorySeq(long cpSearchCategorySeq) {
        logger.info("CP 검색 카테고리 조회 시작: cpSearchCategorySeq={}", cpSearchCategorySeq);

        try {
            ResponseCpSearchCategoryDTO dto = cpSearchCategoryRepository.findByCpSearchCategorySeq(cpSearchCategorySeq)
                    .orElseThrow(() -> {
                        logger.warn("CP 검색 카테고리를 찾을 수 없습니다: cpSearchCategorySeq={}", cpSearchCategorySeq);
                        return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
                    });

            logger.info("CP 검색 카테고리 조회 성공: {}", dto);
            return dto;

        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("CP 검색 카테고리 상세 조회 과정에서 예상치 못한 에러가 발생했습니다.", e);
        }
    }

    /**
     * 새로운 CP 검색 카테고리를 생성하는 메서드입니다.
     *
     * <p>주어진 카테고리 이름의 유효성을 검사하고 중복 여부를 확인한 후,
     * 새로운 CP 검색 카테고리를 데이터베이스에 저장합니다.</p>
     *
     * @param cpSearchCategoryName 생성할 CP 검색 카테고리의 이름
     * @return ResponseCpSearchCategoryDTO 생성된 CP 검색 카테고리의 정보
     * @throws CustomException 유효성 검사 실패, 중복 카테고리, 또는 데이터 저장 오류 발생 시 예외
     */
    public ResponseCpSearchCategoryDTO createCpSearchCategory(String cpSearchCategoryName) {
        // 1. 유효성 검사 및 중복 검사
        cpSearchCategoryName = validateAndCheckDuplicate(cpSearchCategoryName);
        logger.info("유효성 검사 및 중복 검사 성공");

        // 2. 데이터 생성
        CpSearchCategory entity = CpSearchCategory.builder()
                .userSeq(SecurityUtil.getCurrentUserSeq())
                .cpSearchCategoryName(cpSearchCategoryName)
                .build();
        logger.info("생성된 데이터 = {}", entity);

        try {
            // 3. 데이터 저장
            entity = cpSearchCategoryRepository.save(entity); // 저장 후 반환된 엔티티를 업데이트
            logger.info("저장 성공");
        } catch (DataAccessException e) {
            logger.error("CP 검색 카테고리 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        }

        // 4. 저장된 데이터 반환
        CpSearchCategory finalEntity = entity;
        return cpSearchCategoryRepository.findByCpSearchCategorySeq(entity.getCpSearchCategorySeq())
                .orElseThrow(() -> {
                    logger.warn("저장된 CP 검색 카테고리를 찾을 수 없습니다: cpSearchCategorySeq={}", finalEntity.getCpSearchCategorySeq());
                    return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
                });
    }

    /**
     * 주어진 CP 검색 카테고리 이름의 유효성을 검사하고 중복 여부를 확인합니다.
     *
     * <p>이 메서드는 카테고리 이름이 null이거나 비어 있는지 검사하고,
     * 중복된 이름이 존재할 경우 예외를 발생시킵니다.</p>
     *
     * @param cpSearchCategoryName 확인할 검색 카테고리 이름
     * @return 유효성 검사를 통과한 검색 카테고리 이름 (공백 제거)
     * @throws CustomException 유효성 검사 실패 또는 중복 카테고리인 경우 발생
     */
    public String validateAndCheckDuplicate(String cpSearchCategoryName) {

        // 유효성 검사
        if (cpSearchCategoryName == null || cpSearchCategoryName.trim().isEmpty()) {
            throw new CustomException(ErrorCode.REQUIRED_FIELD_MISSING);
        }

        // 중복 검사
        if (existsByName(cpSearchCategoryName)) {
            throw new CustomException(ErrorCode.DUPLICATE_CP_SEARCH_CATEGORY_NAME);
        }

        return cpSearchCategoryName.trim();
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
     * <p>주어진 ID에 해당하는 CP 검색 카테고리를 찾아 이름을 업데이트합니다.
     * 유효성 검사와 중복 확인을 수행하며, 업데이트된 카테고리의 정보를 반환합니다.</p>
     *
     * @param cpSearchCategorySeq  수정할 CP 검색 카테고리의 ID
     * @param cpSearchCategoryName 새로운 CP 검색 카테고리 이름
     * @return ResponseCpSearchCategoryDTO 업데이트된 CP 검색 카테고리의 DTO
     * @throws CustomException 유효성 검사 실패, 카테고리 없음, 또는 데이터베이스 오류가 발생할 경우
     */
    public ResponseCpSearchCategoryDTO updateCpSearchCategory(long cpSearchCategorySeq, String cpSearchCategoryName) {
        // 1. 유효성 검사 및 중복 확인
        logger.info("CP 검색 카테고리 업데이트 요청: ID={} 이름={}", cpSearchCategorySeq, cpSearchCategoryName);
        cpSearchCategoryName = validateAndCheckDuplicate(cpSearchCategoryName);

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
            logger.error("CP 검색 카테고리 업데이트 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (Exception e) {
            logger.error("CP 검색 카테고리 업데이트 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("CP 검색 카테고리 업데이트 중 예상치 못한 오류 발생", e);
        }

        // 4. 반환
        return cpSearchCategoryRepository.findByCpSearchCategorySeq(cpSearchCategorySeq)
                .orElseThrow(() -> {
                    logger.warn("업데이트된 CP 검색 카테고리를 찾을 수 없습니다: ID={}", cpSearchCategorySeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
                });
    }


    /**
     * CP 검색 카테고리 엔티티의 데이터를 업데이트합니다.
     *
     * @param entity               수정할 CP 검색 카테고리 엔티티
     * @param cpSearchCategoryName 새로운 CP 검색 카테고리 이름
     */
    public void updateCpSearchCategoryData(CpSearchCategory entity, String cpSearchCategoryName) {
        entity.updateUserSeq(SecurityUtil.getCurrentUserSeq()); // 현재 사용자 ID 업데이트
        entity.updateCpSearchCategoryName(cpSearchCategoryName); // 카테고리 이름 업데이트
        logger.info("CP 검색 카테고리 데이터 업데이트: ID={} 이름={}", entity.getCpSearchCategorySeq(), cpSearchCategoryName);
    }

    /**
     * CP 검색 카테고리를 삭제합니다.
     *
     * @param cpSearchCategorySeq 삭제할 CP 검색 카테고리의 ID
     * @throws CustomException 카테고리를 찾지 못한 경우 발생
     * @throws CustomException 해당 카테고리에 관련된 데이터가 존재하는 경우 발생
     */
    public void deleteCpSearchCategory(long cpSearchCategorySeq) {
        // 1. 조회
        CpSearchCategory entity = cpSearchCategoryRepository.findById(cpSearchCategorySeq)
                .orElseThrow(() -> {
                    logger.warn("CP 검색 카테고리 찾기 실패: ID={}", cpSearchCategorySeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY);
                });

        // 2. CP 검색 카테고리 데이터 테이블에서 로우 값에서 사용하는지 확인
        boolean hasRelatedData = cpSearchCategoryDataRepository.existsByCpSearchCategorySeq(cpSearchCategorySeq);
        if (hasRelatedData) {
            logger.warn("CP 검색 카테고리를 삭제할 수 없습니다. 관련 데이터가 존재합니다: ID={}", cpSearchCategorySeq);
            throw new CustomException(ErrorCode.CANNOT_DELETE_DATA); // 관련 데이터가 있을 경우 예외 발생
        }

        logger.info("CP 검색 카테고리 삭제 요청: ID={}", cpSearchCategorySeq);

        // 3. 삭제(삭제일 추가)
        entity.updateUserSeq(SecurityUtil.getCurrentUserSeq()); // 현재 사용자 ID 업데이트
        entity.delete(); // 삭제 처리

        // 4. 저장
        try {
            cpSearchCategoryRepository.save(entity); // 수정된 엔티티를 데이터베이스에 저장
            logger.info("CP 검색 카테고리 삭제 성공: ID={}", cpSearchCategorySeq);
        } catch (DataAccessException e) {
            logger.error("CP 검색 카테고리 삭제 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        }
    }

    /**
     * CP 검색 카테고리와 해당 카테고리에 대한 데이터를 조회합니다.
     * <p>
     * 이 메서드는 모든 CP 검색 카테고리를 조회하고,
     * 각 카테고리에 대해 삭제되지 않은 데이터 목록을 설정합니다.
     *
     * @return CP 검색 카테고리와 해당 데이터 목록의 DTO 리스트
     */
    public List<ResponseCpSearchCategoryAndCpSearchCategoryDataDTO> getCpSearchCategoryAndCpSearchCategoryData() {

        logger.info("CP 검색 카테고리와 카테고리별 데이터 조회 요청이 시작되었습니다.");

        List<ResponseCpSearchCategoryAndCpSearchCategoryDataDTO> dtoList = cpSearchCategoryRepository.findSimpleCpSearchCategory();

        if (dtoList != null && !dtoList.isEmpty()) {
            dtoList.forEach(dto -> {
                List<ResponseSimpleCpSearchCategoryDataDTO> categoryData =
                        cpSearchCategoryDataService.getCpSearchCategoryDataListByCpSearchCategorySeqAndDeletedAtIsNull(dto.getCpSearchCategorySeq());
                dto.setCpSearchCategoryDataDtoList(categoryData);
                logger.info("CP 검색 카테고리 시퀀스 {}에 대한 데이터가 {}개 설정되었습니다.", dto.getCpSearchCategorySeq(), categoryData.size());
            });
        } else {
            logger.warn("CP 검색 카테고리와 카테고리별 데이터 요청 과정에서 에러가 발생했습니다. 데이터가 비어있습니다.");
            return null;
        }

        logger.info("CP 검색 카테고리와 카테고리별 데이터 조회가 완료되었습니다.");
        return dtoList;
    }

}
