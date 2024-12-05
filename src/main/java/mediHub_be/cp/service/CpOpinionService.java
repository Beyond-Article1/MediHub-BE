package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.repository.CpOpinionRepository;
import mediHub_be.cp.repository.CpVersionRepository;
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

    @Transactional(readOnly = true)
    public List<ResponseCpOpinionDTO> findCpOpinionListByCpVersionSeq(
            long cpVersionSeq,
            long cpOpinionLocationSeq) {

        logger.info("CP 버전 번호: {}의 CP 의견 위치 번호: {}로 조회 요청했습니다.", cpVersionSeq, cpOpinionLocationSeq);

        return null;
    }

    @Transactional(readOnly = true)
    public ResponseCpOpinionDTO findCpOpinionByCpOpinionSeq(
            long cpVersionSeq,
            long cpOpinionLocationSeq,
            long cpOpinionSeq,
            boolean isDeleted) {

        logger.info("CP 버전 번호: {}, CP 의견 위치 번호: {}, CP 의견 번호: {}로 조회 요청했습니다.", cpVersionSeq, cpOpinionLocationSeq, cpOpinionSeq);

        // DB 조회
        ResponseCpOpinionDTO cpOpinion;

        if (isDeleted) {
            logger.info("삭제된 의견을 조회합니다.");
            cpOpinion = cpOpinionRepository.findByCpOpinionSeqAndDeletedAtIsNotNull(cpOpinionSeq);
        } else {
            logger.info("활성 상태의 의견을 조회합니다.");
            cpOpinion = cpOpinionRepository.findByCpOpinionSeqAndDeletedAtIsNull(cpOpinionSeq);
        }

        // 조회 결과 확인
        if (cpOpinion == null) {
            logger.warn("조회된 CP 의견이 없습니다. CP 버전 번호: {}, CP 의견 위치 번호: {}, CP 의견 번호: {}", cpVersionSeq, cpOpinionLocationSeq, cpOpinionSeq);
        } else {
            logger.info("조회된 CP 의견: {}", cpOpinion);
        }

        // 반환
        return cpOpinion;
    }
}
