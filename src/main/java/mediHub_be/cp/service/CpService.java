package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.repository.CpVersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpService {

    // Repository
    private final CpVersionRepository cpVersionRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpService");

    @Transactional(readOnly = true)
    public List<ResponseCpDTO> getCpListByCpSearchCategoryAndCpSearchCategoryData(
            Long[] cpSearchCategorySeqArray,
            Long[] cpSearchCategoryDataArray) {

        logger.info("CP 검색 카테고리 시퀀스: {}, 카테고리 데이터: {}", cpSearchCategorySeqArray, cpSearchCategoryDataArray);

        // DB 조회
        List<Map<String, Object>> findCpList = cpVersionRepository.findByCategorySeqAndCategoryData(
                cpSearchCategorySeqArray,
                cpSearchCategoryDataArray
        );

        if (findCpList.isEmpty()) {
            logger.warn("조회 결과 없음: 카테고리 시퀀스와 데이터로 찾은 CP가 없습니다.");
            return null;
        } else {
            logger.info("조회된 CP 리스트 크기: {}, 내용: {}", findCpList.size(), findCpList);
            // Map을 ResponseCpDTO로 변환
            return findCpList.stream()
                    .map(ResponseCpDTO::buildResponseCpDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<ResponseCpDTO> getCpListByCpName(String cpName) {
        logger.info("CP 이름으로 검색: {}", cpName);

        // DB 조회
        List<Map<String, Object>> findCpList = cpVersionRepository.findByCpNameContainingIgnoreCase(cpName);    // Logger

        if (findCpList.isEmpty()) {
            logger.warn("조회 결과 없음: CP 이름 '{}'에 대한 결과가 없습니다.", cpName);
            return null;
        } else {
            logger.info("조회된 CP 리스트 크기: {}, 내용: {}", findCpList.size(), findCpList);
            // Map을 ResponseCpDTO로 변환
            return findCpList.stream()
                    .map(ResponseCpDTO::buildResponseCpDTO)
                    .collect(Collectors.toList());
        }
    }

    public ResponseCpDTO getCpByCpVersionSeq(long cpVersionSeq) {
        logger.info("CP 버전 시퀀스로 검색: {}", cpVersionSeq);

        // DB 조회
        ResponseCpDTO findCp = cpVersionRepository.findByCpVersionSeq(cpVersionSeq);

        if (findCp == null) {
            logger.warn("조회 결과 없음: CP 버전 시퀀스 '{}'에 대한 결과가 없습니다.", cpVersionSeq);
            return null;
        } else {
            logger.info("조회된 CP: {}", findCp);
            // 조회된 결과 반환
            return findCp;
        }
    }
}
