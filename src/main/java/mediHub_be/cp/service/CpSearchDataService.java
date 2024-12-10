package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpSearchDataDTO;
import mediHub_be.cp.repository.CpSearchDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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


}
