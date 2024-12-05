package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.repository.CpOpinionRepository;
import mediHub_be.cp.repository.CpVersionRepository;
import mediHub_be.security.util.JwtUtil;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpOpinionService {

    // Repository
    private final CpOpinionRepository cpOpinionRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpOpinionService");    // Logger
    private final CpVersionRepository cpVersionRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public List<ResponseCpOpinionDTO> findCpOpinionListByCpVersionSeq(
            long cpVersionSeq,
            long cpOpinionLocationSeq,
            boolean isDeleted) {

        logger.info("CP 버전 번호: {}의 CP 의견 위치 번호: {}로 조회 요청했습니다.", cpVersionSeq, cpOpinionLocationSeq);

        List<ResponseCpOpinionDTO> result;

        // DB 조회
        if (isDeleted) {
            logger.info("삭제된 의견을 조회합니다.");
            result = cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNotNull(cpOpinionLocationSeq);
        } else {
            logger.info("활성 상태의 의견을 조회합니다.");
            result = cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNull(cpOpinionLocationSeq);
        }

        // 결과 확인
        if (result == null || result.isEmpty()) {
            logger.warn("조회된 CP 의견이 없습니다. CP 버전 번호: {}, CP 의견 위치 번호: {}", cpVersionSeq, cpOpinionLocationSeq);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
        } else {
            logger.info("조회된 CP 의견의 수: {}", result.size());
        }

        return result;
    }

    @Transactional(readOnly = true)
    public ResponseCpOpinionDTO findCpOpinionByCpOpinionSeq(long cpOpinionSeq) {

        logger.info("CP 의견 번호: {}로 조회 요청했습니다.", cpOpinionSeq);

        // DB 조회
        ResponseCpOpinionDTO result = cpOpinionRepository.findByCpOpinionSeq(cpOpinionSeq);

        if (result == null) {
            // 해당 의견은 존재하지 않는다.
            logger.warn("조회된 CP 의견이 없습니다. CP 의견 번호: {}", cpOpinionSeq);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
        } else {
            // 삭제 여부 확인
            if (result.getDeletedAt() == null) {
                // 활성 상태의 게시글
                logger.info("CP 의견 번호: {}는 활성 상태입니다.", cpOpinionSeq);
                return result;
            } else {
                // 로그인한 직원의 권한이 ADMIN인지 확인
                logger.info("CP 의견 번호: {}는 삭제된 상태입니다.", cpOpinionSeq);
                if (SecurityUtil.getCurrentUserAuthorities().equals(UserAuth.ADMIN)) {
                    logger.info("관리자 권한으로 CP 의견 번호: {}를 조회합니다.", cpOpinionSeq);
                    return result;
                } else {
                    // 권한이 없으면 못본다.
                    logger.warn("CP 의견 번호: {}에 대한 접근 권한이 없습니다.", cpOpinionSeq);
                    throw new CustomException(ErrorCode.NOT_FOUND_CP_OPINION);
                }
            }
        }
    }
}
