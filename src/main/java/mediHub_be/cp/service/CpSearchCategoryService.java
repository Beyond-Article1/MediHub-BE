package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpSearchCategoryDTO;
import mediHub_be.cp.repository.CpSearchCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
