package mediHub_be.src.test.cp.service;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.repository.CpVersionRepository;
import mediHub_be.cp.service.CpService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CpServiceTest {

    @Autowired
    CpService cpService;

    @MockBean
    CpVersionRepository cpVersionRepository;

    private static List<Map<String, Object>> mockCpVersionList;
    private static Map<String, Object> cp1;
    private static Map<String, Object> cp2;

    @BeforeAll
    public static void setUp() {
        // Mock 데이터 설정
        cp1 = new HashMap<>();
        cp1.put("cpName", "백내장");
        cp1.put("cpDescription", "백내장 설명");
        cp1.put("cpViewCount", 523L);
        cp1.put("cpVersion", "1.0.0");
        cp1.put("cpVersionDescription", "최초 업로드");
        cp1.put("createdAt", LocalDateTime.of(2024, 1, 1, 0, 0)); // LocalDateTime으로 설정
        cp1.put("userName", "임광택");
        cp1.put("userId", "19615041");
        cp1.put("partName", "안과");

        cp2 = new HashMap<>();
        cp2.put("cpName", "각막염");
        cp2.put("cpDescription", "각막염 설명");
        cp2.put("cpViewCount", 492L);
        cp2.put("cpVersion", "1.1.0");
        cp2.put("cpVersionDescription", "2일차 처방약 변경");
        cp2.put("createdAt", LocalDateTime.of(2024, 1, 2, 0, 0)); // LocalDateTime으로 설정
        cp2.put("userName", "김철수");
        cp2.put("userId", "19615042");
        cp2.put("partName", "안과");

        mockCpVersionList = Arrays.asList(cp1, cp2);
    }

    @Test
    @DisplayName("getCpListByCpSearchCategoryAndCpSearchCategoryDataTest")
    public void getCpListByCpSearchCategoryAndCpSearchCategoryDataTest() {
        // Given
        Mockito.when(cpVersionRepository.findByCategorySeqAndCategoryData(anyList(), anyList()))
                .thenReturn(mockCpVersionList);

        // When
        List<ResponseCpDTO> result = cpService.getCpListByCpSearchCategoryAndCpSearchCategoryData(
                List.of(1L, 2L, 3L),
                List.of(1L, 2L, 3L)
        );

        // Then
        // 첫 번째 DTO 검증
        ResponseCpDTO dto1 = result.get(0);
        assertThat(dto1.getCpVersion()).isEqualTo("1.0.0");
        assertThat(dto1.getCpName()).isEqualTo("백내장");
        assertThat(dto1.getCpDescription()).isEqualTo("백내장 설명");
        assertThat(dto1.getCpViewCount()).isEqualTo(523L);
        assertThat(dto1.getCpVersionDescription()).isEqualTo("최초 업로드");
        assertThat(dto1.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertThat(dto1.getUserName()).isEqualTo("임광택");
        assertThat(dto1.getUserId()).isEqualTo("19615041");
        assertThat(dto1.getPartName()).isEqualTo("안과");

        // 두 번째 DTO 검증
        ResponseCpDTO dto2 = result.get(1);
        assertThat(dto2.getCpVersion()).isEqualTo("1.1.0");
        assertThat(dto2.getCpName()).isEqualTo("각막염");
        assertThat(dto2.getCpDescription()).isEqualTo("각막염 설명");
        assertThat(dto2.getCpViewCount()).isEqualTo(492L);
        assertThat(dto2.getCpVersionDescription()).isEqualTo("2일차 처방약 변경");
        assertThat(dto2.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 2, 0, 0));
        assertThat(dto2.getUserName()).isEqualTo("김철수");
        assertThat(dto2.getUserId()).isEqualTo("19615042");
        assertThat(dto2.getPartName()).isEqualTo("안과");

        // 메서드 호출 여부 검증
        Mockito.verify(cpVersionRepository).findByCategorySeqAndCategoryData(anyList(), anyList());
    }

    @Test
    @DisplayName("findByCpNameContainingIgnoreCase")
    public void findByCpNameContainingIgnoreCaseTest() {
        // Given
        Mockito.when(cpVersionRepository.findByCpNameContainingIgnoreCase(anyString()))
                .thenReturn(Arrays.asList(
                        Map.of(
                                "cpName", cp1.get("cpName"),
                                "cpDescription", cp1.get("cpDescription"),
                                "cpViewCount", cp1.get("cpViewCount"),
                                "cpVersion", cp1.get("cpVersion"),
                                "cpVersionDescription", cp1.get("cpVersionDescription"),
                                "createdAt", cp1.get("createdAt"),
                                "userName", cp1.get("userName"),
                                "userId", cp1.get("userId"),
                                "partName", cp1.get("partName")
                        ),
                        Map.of(
                                "cpName", cp2.get("cpName"),
                                "cpDescription", cp2.get("cpDescription"),
                                "cpViewCount", cp2.get("cpViewCount"),
                                "cpVersion", cp2.get("cpVersion"),
                                "cpVersionDescription", cp2.get("cpVersionDescription"),
                                "createdAt", cp2.get("createdAt"),
                                "userName", cp2.get("userName"),
                                "userId", cp2.get("userId"),
                                "partName", cp2.get("partName")
                        )
                ));

        // When
        List<Map<String, Object>> result = cpVersionRepository.findByCpNameContainingIgnoreCase("백내장");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        // 첫 번째 결과 검증
        Map<String, Object> firstResult = result.get(0);
        assertThat(firstResult.get("cpName")).isEqualTo("백내장");
        assertThat(firstResult.get("cpDescription")).isEqualTo("백내장 설명");
        assertThat(firstResult.get("cpViewCount")).isEqualTo(523L);
        assertThat(firstResult.get("cpVersion")).isEqualTo("1.0.0");
        assertThat(firstResult.get("cpVersionDescription")).isEqualTo("최초 업로드");
        assertThat(firstResult.get("createdAt")).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertThat(firstResult.get("userName")).isEqualTo("임광택");
        assertThat(firstResult.get("userId")).isEqualTo("19615041");
        assertThat(firstResult.get("partName")).isEqualTo("안과");

        // 두 번째 결과 검증
        Map<String, Object> secondResult = result.get(1);
        assertThat(secondResult.get("cpName")).isEqualTo("각막염");
        assertThat(secondResult.get("cpDescription")).isEqualTo("각막염 설명");
        assertThat(secondResult.get("cpViewCount")).isEqualTo(492L);
        assertThat(secondResult.get("cpVersion")).isEqualTo("1.1.0");
        assertThat(secondResult.get("cpVersionDescription")).isEqualTo("2일차 처방약 변경");
        assertThat(secondResult.get("createdAt")).isEqualTo(LocalDateTime.of(2024, 1, 2, 0, 0));
        assertThat(secondResult.get("userName")).isEqualTo("김철수");
        assertThat(secondResult.get("userId")).isEqualTo("19615042");
        assertThat(secondResult.get("partName")).isEqualTo("안과");
    }

    @Test
    @DisplayName("findByCpVersionSeq")
    public void testFindByCpVersionSeq() {
        // Given
        ResponseCpDTO expectedResponse = ResponseCpDTO.builder()
                .cpName("백내장")
                .cpDescription("백내장 설명")
                .cpViewCount(523L)
                .cpVersion("1.0.0")
                .cpVersionDescription("최초 업로드")
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .userName("임광택")
                .userId("19615041")
                .partName("안과")
                .build();

        Mockito.when(cpVersionRepository.findByCpVersionSeq(anyLong()))
                .thenReturn(Optional.of(expectedResponse));

        // When
        ResponseCpDTO result = cpVersionRepository.findByCpVersionSeq(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CP_VERSION));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCpName()).isEqualTo("백내장");
        assertThat(result.getCpDescription()).isEqualTo("백내장 설명");
        assertThat(result.getCpViewCount()).isEqualTo(523L);
        assertThat(result.getCpVersion()).isEqualTo("1.0.0");
        assertThat(result.getCpVersionDescription()).isEqualTo("최초 업로드");
        assertThat(result.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertThat(result.getUserName()).isEqualTo("임광택");
        assertThat(result.getUserId()).isEqualTo("19615041");
        assertThat(result.getPartName()).isEqualTo("안과");

        // 메서드 호출 여부 검증
        Mockito.verify(cpVersionRepository).findByCpVersionSeq(anyLong());
    }
}
