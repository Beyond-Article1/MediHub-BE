package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.CpSearchDataDTO;
import mediHub_be.cp.dto.RequestCpSearchDataDTO;
import mediHub_be.cp.dto.ResponseCpSearchDataDTO;
import mediHub_be.cp.entity.CpSearchData;
import mediHub_be.cp.repository.CpSearchDataRepository;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpSearchDataService {

    // Repository
    private final CpSearchDataRepository cpSearchDataRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpSearchDataController"); // Logger

    /**
     * 주어진 CP 버전 시퀀스에 대한 CP 검색 데이터 목록을 조회합니다.
     *
     * @param cpVersionSeq 조회할 CP 버전 시퀀스
     * @return List<ResponseCpSearchDataDTO> 조회된 CP 검색 데이터 목록
     */
    public List<ResponseCpSearchDataDTO> getCpSearchDataListByCpVersionSeq(long cpVersionSeq) {
        try {
            return cpSearchDataRepository.findDtoJoinCpSearchCategoryDataOnCpSearchCategorySeqCpVersionOnCpVersionSeq(cpVersionSeq);
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            // 예외 발생 시 빈 리스트 반환
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("CP 검색 데이터 조회 과정에서 예상치 못한 에러가 발생했습니다.", e);
        }
    }

    /**
     * 새로운 CP 검색 데이터를 생성하고 저장합니다.
     *
     * @param requestBody 생성할 CP 검색 데이터의 정보
     * @return CpSearchDataDTO 생성된 CP 검색 데이터의 DTO
     * @throws CustomException  데이터 접근 오류 발생 시
     * @throws RuntimeException 예기치 않은 오류 발생 시
     */
    @Transactional
    public CpSearchDataDTO createCpSearchData(RequestCpSearchDataDTO requestBody) {
        logger.info("서비스 클래스에 CP 검색 데이터 생성 요청이 되었습니다.");
        logger.info("전달받은 데이터: {}", requestBody);

        // 엔티티 생성
        CpSearchData entity = CpSearchData.create(requestBody);
        logger.info("생성된 데이터: {}", entity);

        try {
            // 엔티티 저장
            entity = cpSearchDataRepository.save(entity);

            logger.info("새로운 CP 검색 데이터를 저장했습니다. 데이터: {}", entity);

            CpSearchDataDTO dto = CpSearchDataDTO.toDto(entity);
            logger.info("변환된 데이터: {}", dto);

            return CpSearchDataDTO.toDto(entity); // 여기서 entity가 null이 아니므로 안전함
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("CP 검색 데이터 생성 중 예기치 않은 에러가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("CP 검색 데이터 생성 중 예기치 못한 에러가 발생했습니다.", e);
        }
    }

    /**
     * 주어진 CP 검색 데이터 시퀀스를 사용하여 해당 CP 검색 데이터를 삭제합니다.
     *
     * @param cpSearchDataSeq 삭제할 CP 검색 데이터의 시퀀스
     * @throws CustomException  권한이 없는 사용자가 접근할 경우
     * @throws RuntimeException CP 검색 데이터 삭제 중 예기치 않은 오류가 발생할 경우
     */
    public void deleteCpSearchData(long cpSearchDataSeq) {
        // 1. 권한 확인
        validateUserAccess();

        // 2. 삭제할 데이터 존재 여부 확인
        if (!cpSearchDataRepository.existsById(cpSearchDataSeq)) {
            logger.error("{}번 CP 검색 데이터가 존재하지 않습니다.", cpSearchDataSeq);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_SEARCH_DATA); // 데이터가 없는 경우 예외 던지기
        }

        try {
            // 3. 삭제 시도
            logger.info("{}번 CP 검색 데이터를 삭제합니다.", cpSearchDataSeq);
            cpSearchDataRepository.deleteById(cpSearchDataSeq);

            // 4. 삭제 완료
            logger.info("{}번 CP 검색 데이터를 삭제했습니다.", cpSearchDataSeq);
        } catch (DataAccessException e) {
            logger.error("CP 검색 데이터 삭제 중 DB 접근 에러가 발생했습니다. 요청 번호: {}", cpSearchDataSeq, e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("CP 검색 데이터 삭제 중 예기치 않은 에러가 발생했습니다. 요청 번호: {}", cpSearchDataSeq, e);
            throw new RuntimeException("CP 검색 데이터 삭제 중 예기치 못한 에러가 발생했습니다. 요청번호: " + cpSearchDataSeq, e);
        }
    }

    /**
     * 현재 사용자의 권한을 확인합니다.
     * ADMIN 권한이 아닐 경우 예외를 발생시킵니다.
     */
    private void validateUserAccess() {
        if (!SecurityUtil.getCurrentUserAuthorities().equals(UserAuth.ADMIN.name())) {
            logger.warn("권한이 없는 사용자가 API를 호출하였습니다.");
            logger.warn("userSeq: {}, userId: {}, userAuth: {}",
                    SecurityUtil.getCurrentUserSeq(),
                    SecurityUtil.getCurrentUserId(),
                    SecurityUtil.getCurrentUserAuthorities());
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

}
