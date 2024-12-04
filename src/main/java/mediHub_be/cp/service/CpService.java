package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.repository.CpVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CpService {

    // Repository
    private final CpVersionRepository cpVersionRepository;

    @Transactional(readOnly = true)
    public List<ResponseCpDTO> getCpListByCpSearchCategoryAndCpSearchCategoryData(
            Long[] cpSearchCategorySeqArray,
            Long[] cpSearchCategoryDataArray) {

        // 데이터베이스에서 결과를 조회
        List<Map<String, Object>> findCpList = cpVersionRepository.findByCategorySeqAndCategoryData(
                cpSearchCategorySeqArray,
                cpSearchCategoryDataArray
        );

        if (findCpList.isEmpty()) {
            // 조회 결과 없음
            return null;
        } else {
            // Map을 ResponseCpDTO로 변환
            List<ResponseCpDTO> responseCpDTOList = findCpList.stream()
                    .map(ResponseCpDTO::buildResponseCpDTO)
                    .collect(Collectors.toList());

            return responseCpDTOList;
        }
    }

    public ResponseCpDTO getCpByCpName(String cpName) {
        return null;
    }

    public ResponseCpDTO getCpByCpVersionSeq(long cpVersionSeq) {
        return null;
    }
}
