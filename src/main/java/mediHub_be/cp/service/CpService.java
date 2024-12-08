package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
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

    /**
     * 주어진 카테고리 시퀀스와 카테고리 데이터를 기준으로 CP 리스트를 조회하는 메서드입니다.
     * 이 메서드는 입력된 카테고리 시퀀스 및 데이터를 사용하여 데이터베이스에서 CP 정보를 검색합니다.
     *
     * @param cpSearchCategorySeqArray  검색할 카테고리 시퀀스의 리스트
     * @param cpSearchCategoryDataArray 검색할 카테고리 데이터의 리스트
     * @return 조회된 CP 정보를 포함하는 ResponseCpDTO의 리스트
     * @throws CustomException 조회 결과가 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public List<ResponseCpDTO> getCpListByCpSearchCategoryAndCpSearchCategoryData(
            List<Long> cpSearchCategorySeqArray,
            List<Long> cpSearchCategoryDataArray) {

        logger.info("CP 검색 카테고리 시퀀스: {}, 카테고리 데이터: {}", cpSearchCategorySeqArray, cpSearchCategoryDataArray);

        // DB 조회
        List<Map<String, Object>> findCpList = cpVersionRepository.findByCategorySeqAndCategoryData(
                cpSearchCategorySeqArray,
                cpSearchCategoryDataArray
        );

        if (findCpList.isEmpty()) {
            logger.info("조회 결과 없음: 카테고리 시퀀스와 데이터로 찾은 CP가 없습니다.");
            throw new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);
        } else {
            logger.info("조회된 CP 리스트 크기: {}", findCpList.size());
            // Map을 ResponseCpDTO로 변환
            return findCpList.stream()
                    .map(ResponseCpDTO::toDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 주어진 CP 이름을 기준으로 데이터베이스에서 CP 리스트를 조회하는 메서드입니다.
     * 이 메서드는 대소문자를 구분하지 않고 부분 일치를 통해 CP 이름을 검색합니다.
     *
     * @param cpName 검색할 CP 이름
     * @return CP 리스트가 포함된 ResponseCpDTO의 리스트
     * @throws CustomException 조회 결과가 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public List<ResponseCpDTO> getCpListByCpName(String cpName) {
        logger.info("CP 이름으로 검색: {}", cpName);

        // DB 조회
        List<Map<String, Object>> findCpList = cpVersionRepository.findByCpNameContainingIgnoreCase(cpName);    // Logger

        if (findCpList.isEmpty()) {
            logger.info("조회 결과 없음: CP 이름 '{}'에 대한 결과가 없습니다.", cpName);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);
        } else {
            logger.info("조회된 CP 리스트 크기: {}", findCpList.size());
            // Map을 ResponseCpDTO로 변환
            return findCpList.stream()
                    .map(ResponseCpDTO::toDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 주어진 CP 버전 시퀀스를 기준으로 데이터베이스에서 CP 정보를 조회하는 메서드입니다.
     * 이 메서드는 특정 CP 버전 시퀀스에 해당하는 CP 정보를 검색하여 반환합니다.
     *
     * @param cpVersionSeq 조회할 CP 버전 시퀀스
     * @return 조회된 CP 정보를 포함하는 ResponseCpDTO 객체
     * @throws CustomException 조회 결과가 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public ResponseCpDTO getCpByCpVersionSeq(long cpVersionSeq) {
        logger.info("CP 버전 시퀀스로 검색: {}", cpVersionSeq);

        // DB 조회
        ResponseCpDTO findCp = cpVersionRepository.findByCpVersionSeq(cpVersionSeq);

        if (findCp == null) {
            // 조회 결과 없음
            logger.info("조회 결과 없음: CP 버전 시퀀스 '{}'에 대한 결과가 없습니다.", cpVersionSeq);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);

        } else {
            logger.info("조회된 CP: {}", findCp);
            // 조회된 결과 반환
            return findCp;
        }
    }

    /**
     * CP 리스트를 조회하는 메서드입니다.
     * 이 메서드는 데이터베이스에서 CP 버전 정보를 조회하고,
     * 결과를 ResponseCpDTO 리스트로 반환합니다.
     *
     * @return CP 리스트가 포함된 ResponseCpDTO의 리스트
     * @throws CustomException 조회 결과가 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public List<ResponseCpDTO> getCpList() {
        logger.info("전체 CP 리스트 조회 요청을 받았습니다.");

        List<ResponseCpDTO> dtoList = cpVersionRepository.findCp();

        if (dtoList == null || dtoList.isEmpty()) {
            logger.warn("조회 결과 없음: CP 리스트가 비어있습니다.");
            throw new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);
        }

        logger.info("조회된 CP 리스트 크기: {}", dtoList.size());
        return dtoList;
    }
}
