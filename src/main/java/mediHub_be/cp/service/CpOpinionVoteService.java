package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.CpOpinionVoteDTO;
import mediHub_be.cp.entity.CpOpinionVote;
import mediHub_be.cp.repository.CpOpinionVoteRepository;
import mediHub_be.security.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpOpinionVoteService {

    // Repository
    private final CpOpinionVoteRepository cpOpinionVoteRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpOpinionVoteService");    // Logger

    /**
     * 주어진 CP 버전 번호와 투표 정보를 사용하여 새로운 CP 의견 투표를 생성합니다.
     *
     * @param cpVersionSeq  CP 버전 번호
     * @param cpOpinionVote CP 의견 투표 여부 (true: 찬성, false: 반대)
     * @return 생성된 CP 의견 투표의 DTO
     * @throws CustomException 데이터베이스 처리 중 오류가 발생한 경우
     */
    @Transactional
    public CpOpinionVoteDTO createCpOpinionVote(long cpVersionSeq, boolean cpOpinionVote) {
        logger.info("CP 버전 번호: {}로 CP 의견 투표를 생성합니다. 투표 여부: {}", cpVersionSeq, cpOpinionVote);

        CpOpinionVote entity = CpOpinionVote.toEntity(cpVersionSeq, SecurityUtil.getCurrentUserSeq(), cpOpinionVote);
        CpOpinionVoteDTO dto;

        try {
            // 데이터베이스에 저장
            entity = cpOpinionVoteRepository.save(entity);
            dto = CpOpinionVoteDTO.toDto(entity);

            logger.info("CP 의견 투표가 성공적으로 생성되었습니다. 투표 ID: {}", entity.getCpOpinionVoteSeq());
        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("예기치 않은 오류가 발생했습니다.", e);
        }

        return dto;
    }

    /**
     * 주어진 CP 의견 투표 번호에 해당하는 CP 의견 투표를 삭제합니다.
     *
     * @param cpOpinionVoteSeq 삭제할 CP 의견 투표의 ID
     * @throws CustomException 데이터베이스 접근 중 오류가 발생한 경우
     */
    @Transactional
    public void deleteCpOpinionVote(long cpOpinionVoteSeq) {
        logger.info("CP 의견 투표 삭제 요청. 투표 ID: {}", cpOpinionVoteSeq);

        try {
            // 삭제 시도
            cpOpinionVoteRepository.deleteById(cpOpinionVoteSeq);
            logger.info("CP 의견 투표가 성공적으로 삭제되었습니다. 투표 ID: {}", cpOpinionVoteSeq);
        } catch (DataAccessException e) {
            logger.error("데이터베이스 접근 오류: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("예기치 않은 오류가 발생했습니다. 투표 ID: " + cpOpinionVoteSeq, e);
        }
    }

    /**
     * 주어진 CP 의견 시퀀스에 대한 투표 스퀀스 리스트를 가져옵니다.
     *
     * @param cpOpinionSeq CP 의견 시퀀스
     * @return 해당 CP 의견에 대한 투표 시퀀스 리스트
     * @throws CustomException 데이터베이스 접근 중 오류가 발생한 경우
     */
    @Transactional(readOnly = true)
    public List<Long> getCpOpinionVoteSeqByCpOpinionSeq(long cpOpinionSeq) {
        try {
            List<CpOpinionVote> entityList = cpOpinionVoteRepository.findByCpOpinionSeq(cpOpinionSeq);

            return entityList.stream()
                    .map(CpOpinionVote::getCpOpinionSeq)
                    .toList();
        } catch (DataAccessException e) {
            logger.error("CP 의견 투표 정보를 가져오는 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        }
    }
}
