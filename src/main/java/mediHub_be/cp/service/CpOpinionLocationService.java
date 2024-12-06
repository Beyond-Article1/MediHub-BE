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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
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

    /**
     * CP 의견 위치를 생성하고 데이터베이스에 저장하는 메서드입니다.
     *
     * @param cpVersionSeq    CP 버전의 고유 식별자
     * @param requestBody     CP 의견 위치 생성에 필요한 데이터가 포함된 요청 DTO
     * @return CpOpinionLocationDTO    생성된 CP 의견 위치의 DTO
     * @throws CustomException    데이터베이스 접근 오류나 기타 예기치 않은 오류가 발생할 경우
     */
    @Transactional
    public CpOpinionLocationDTO createCpOpinionLocation(long cpVersionSeq, RequestCpOpinionLocationDTO requestBody) {

        // Logger 인스턴스 생성
        Logger logger = LoggerFactory.getLogger(getClass());

        // 메서드 설명
        logger.info("CP 의견 위치 생성 요청. CP 버전 번호: {}, 요청 데이터: {}", cpVersionSeq, requestBody);

        CpOpinionLocation entity;
        CpOpinionLocationDTO dto;

        try {
            // CP 의견 위치 엔티티 생성
            CpOpinionLocation temp = CpOpinionLocation.create(cpVersionSeq, requestBody);
            logger.info("CP 의견 위치 엔티티 생성 완료. 엔티티: {}", temp);

            // 데이터베이스에 저장
            entity = cpOpinionLocationRepository.save(temp);
            logger.info("CP 의견 위치 데이터베이스에 저장 완료. 저장된 엔티티: {}", entity);

            // DTO로 변환
            dto = CpOpinionLocationDTO.toDto(entity);
            logger.info("CP 의견 위치 DTO 변환 완료. DTO: {}", dto);

        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("CP 의견 위치 생성 완료. 반환된 DTO: {}", dto);
        return dto;
    }
}
