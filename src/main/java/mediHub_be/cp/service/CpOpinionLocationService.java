package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.CpOpinionLocationDTO;
import mediHub_be.cp.dto.RequestCpOpinionLocationDTO;
import mediHub_be.cp.entity.CpOpinionLocation;
import mediHub_be.cp.repository.CpOpinionLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpOpinionLocationService {

    // Repository
    private final CpOpinionLocationRepository cpOpinionLocationRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpOpinionLocationService");    // Logger

    /**
     * 주어진 CP 버전 시퀀스를 사용하여 CP 의견 위치 목록을 조회하는 메서드입니다.
     *
     * @param cpVersionSeq CP 버전 시퀀스
     * @return CP 의견 위치 DTO 리스트
     * @throws CustomException 데이터베이스 접근 오류 또는 기타 예외 발생 시
     */
    public List<CpOpinionLocationDTO> getCpOpinionLocationListByCpVersionSeq(long cpVersionSeq) {
        List<CpOpinionLocation> entityList;

        try {
            logger.info("CP 버전 시퀀스: {}로 의견 위치 목록 조회 요청을 시작합니다.", cpVersionSeq);

            entityList = cpOpinionLocationRepository.findByCpVersionSeq(cpVersionSeq);

            if (entityList.isEmpty()) {
                logger.info("CP 버전 시퀀스: {}에 대한 의견 위치 목록이 없습니다.", cpVersionSeq);
            } else {
                logger.info("CP 버전 시퀀스: {}에 대한 의견 위치 목록 조회 성공, 결과 크기: {}", cpVersionSeq, entityList.size());
            }
        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류: CP 버전 시퀀스: {}에 대한 의견 위치 목록 조회 중 오류 발생: {}", cpVersionSeq, e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (Exception e) {
            logger.error("CP 버전 시퀀스: {}에 대한 의견 위치 목록 조회 중 예기치 않은 오류 발생: {}", cpVersionSeq, e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return entityList.stream()
                .map(CpOpinionLocationDTO::toDto)
                .toList();
    }

    public CpOpinionLocationDTO createCpOpinionLocation(long cpVersionSeq, RequestCpOpinionLocationDTO requestBody) {

        CpOpinionLocation entity;

//        try{
//            entity = cpOpinionLocationRepository.save()
//        }

        return null;
    }
}
