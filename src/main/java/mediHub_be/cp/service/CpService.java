package mediHub_be.cp.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.entity.CpVersion;
import mediHub_be.cp.repository.CpRepository;
import mediHub_be.cp.repository.CpVersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CpService {

    private final CpRepository cpRepository;
    private final CpVersionRepository cpVersionRepository;

    public List<ResponseCpDTO> getCpListByCpSearchCategoryAndCpSearchCategoryData(
            Long[] cpSearchCategorySeqArray,
            String[] cpSearchCategoryDataArray) {

        // CpVersionRepository를 사용하여 CP 버전 목록 조회
        List<CpVersion> cpVersions = cpVersionRepository.findByCategorySeqAndCategoryDataNames(
                List.of(cpSearchCategorySeqArray),
                List.of(cpSearchCategoryDataArray)
        );

        // CpVersion을 ResponseCpDTO로 변환
        return cpVersions.stream()
                .map(this::convertToResponseCpDTO)
                .collect(Collectors.toList());
    }

    // CpVersion을 ResponseCpDTO로 변환하는 메서드
    private ResponseCpDTO convertToResponseCpDTO(CpVersion cpVersion) {
        // 변환 로직 구현
        return new ResponseCpDTO(
                cpVersion.getCpVersionSeq(),
                cpVersion.getCpSeq(),
                cpVersion.getCpVersion(),
                cpVersion.getCpVersionDescription(),
                cpVersion.getCpUrl()
        );
    }
}
