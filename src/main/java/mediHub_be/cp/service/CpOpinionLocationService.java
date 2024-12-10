package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.CpOpinionLocationDTO;
import mediHub_be.cp.dto.RequestCpOpinionLocationDTO;
import mediHub_be.cp.dto.ResponseCpOpinionLocationDTO;
import mediHub_be.cp.entity.CpOpinionLocation;
import mediHub_be.cp.repository.CpOpinionLocationRepository;
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
public class CpOpinionLocationService {

    // Repository
    private final CpOpinionLocationRepository cpOpinionLocationRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpOpinionLocationService");    // Logger

    /**
     * 주어진 CP 버전 시퀀스를 사용하여 CP 의견 위치 목록을 조회하는 메서드입니다.
     *
     * <p>이 메서드는 주어진 CP 버전 시퀀스를 기반으로 데이터베이스에서 관련된 CP 의견 위치를 조회합니다.
     * 조회된 위치가 없을 경우, 해당 정보를 로그에 기록합니다.</p>
     *
     * @param cpVersionSeq CP 버전 시퀀스
     * @return CP 의견 위치 DTO 리스트. 조회된 의견 위치가 없을 경우 빈 리스트를 반환합니다.
     * @throws CustomException  데이터베이스 접근 오류 발생 시
     * @throws RuntimeException 기타 예기치 않은 오류 발생 시
     */
    @Transactional(readOnly = true)
    public List<CpOpinionLocationDTO> findCpOpinionLocationListByCpVersionSeq(long cpVersionSeq) {

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
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("CP 버전 시퀀스: {}에 대한 의견 위치 목록 조회 중 예기치 않은 오류 발생: {}", cpVersionSeq, e.getMessage());
            throw new RuntimeException("예기치 않은 오류가 발생했습니다.", e);
        }

        return entityList.stream()
                .map(CpOpinionLocationDTO::toDto)
                .toList();
    }

    /**
     * 주어진 CP 버전 시퀀스를 사용하여 새로운 CP 의견 위치를 생성하는 메서드입니다.
     *
     * <p>이 메서드는 요청 본문을 기반으로 CP 의견 위치 엔티티를 생성하고,
     * 이를 데이터베이스에 저장한 후, 저장된 엔티티를 DTO로 변환하여 반환합니다.</p>
     *
     * @param cpVersionSeq CP 버전 시퀀스
     * @param requestBody  CP 의견 위치 생성에 필요한 요청 데이터
     * @return 생성된 CP 의견 위치 DTO
     * @throws CustomException  데이터베이스 접근 오류 발생 시
     * @throws RuntimeException 기타 예기치 않은 오류 발생 시
     */
    @Transactional
    public CpOpinionLocationDTO createCpOpinionLocation(long cpVersionSeq, RequestCpOpinionLocationDTO requestBody) {
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
            logger.error("데이터베이스 접근 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("예기치 않은 오류가 발생했습니다.", e);
        }

        logger.info("CP 의견 위치 생성 완료. 반환된 DTO: {}", dto);
        return dto;
    }

    /**
     * CP 의견 위치를 삭제하는 메서드입니다.
     * <p>
     * 이 메서드는 주어진 CP 의견 위치 번호에 대해 작성자를 검증하고,
     * 해당 의견 위치가 존재하는지 확인한 후, 논리적으로 삭제합니다.
     *
     * @param cpVersionSeq         CP 버전의 고유 식별자
     * @param cpOpinionLocationSeq 삭제할 CP 의견 위치의 고유 식별자
     * @throws CustomException 데이터베이스 접근 오류, 사용자 정의 예외 또는 기타 예외가 발생할 경우
     */
    @Transactional
    public void deleteCpOpinionLocation(long cpVersionSeq, long cpOpinionLocationSeq) {
        try {
            // 작성자 검증
            checkUnauthorizedAccess(cpOpinionLocationSeq);

            // DB에서 CP 의견 위치 조회
            CpOpinionLocation entity = cpOpinionLocationRepository.findById(cpOpinionLocationSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CP_OPINION_LOCATION));

            // 삭제 처리
            entity.delete(); // delete() 메서드가 논리 삭제를 처리한다고 가정

            // 엔티티 저장 (변경된 상태 반영)
            cpOpinionLocationRepository.save(entity);

            // 성공 로그 추가
            logger.info("CP 의견 위치 삭제 성공. CP 버전 번호: {}, CP 의견 위치 번호: {}", cpVersionSeq, cpOpinionLocationSeq);

        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR);
        } catch (CustomException e) {
            logger.error("사용자 정의 예외 발생: {}", e.getMessage());
            throw e; // 이미 CustomException이 발생했으므로 다시 던집니다.
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 지정된 CP 의견 위치 시퀀스를 사용하여 해당 의견 위치에 대한 접근 권한을 검증하는 메서드입니다.
     *
     * <p>이 메서드는 주어진 CP 의견 위치 시퀀스를 기반으로 데이터베이스에서 의견 위치를 조회하고,
     * 현재 사용자가 해당 의견 위치에 접근할 수 있는 권한이 있는지를 확인합니다.
     * 사용자가 권한이 없거나 의견 위치가 존재하지 않을 경우 예외를 발생시킵니다.</p>
     *
     * @param cpOpinionLocationSeq CP 의견 위치의 고유 식별자
     * @throws CustomException 데이터베이스 접근 오류 발생 시,
     *                         의견 위치가 존재하지 않거나 권한이 없는 경우
     */
    @Transactional(readOnly = true)
    public void checkUnauthorizedAccess(long cpOpinionLocationSeq) {

        ResponseCpOpinionLocationDTO dto;

        // 로그인 유저 정보
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();
        String currentUserAuth = SecurityUtil.getCurrentUserAuthorities();

        try {
            // DB에서 데이터 조회
            dto = cpOpinionLocationRepository.findByCpOpinionLocation_CpOpinion_CpOpinionLocationSeq(cpOpinionLocationSeq)
                    .orElseThrow(() -> {
                        logger.warn("CP 의견 위치 ID: {}에 대한 데이터가 존재하지 않습니다.", cpOpinionLocationSeq);
                        return new CustomException(ErrorCode.NOT_FOUND_CP_OPINION_LOCATION);
                    });

            logger.info("CP 의견 위치 조회 성공: {}", dto);

        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("예기치 않은 오류가 발생했습니다.", e);
        }

        // 권한 확인
        if (dto.getUserSeq() != currentUserSeq && !currentUserAuth.equals(UserAuth.ADMIN)) {
            logger.warn("사용자 ID: {}가 CP 의견 위치 ID: {}에 대한 접근 권한이 없습니다.", currentUserSeq, cpOpinionLocationSeq);
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }
}
