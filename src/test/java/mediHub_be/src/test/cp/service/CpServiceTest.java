//package mediHub_be.src.test.cp.service;
//
//import mediHub_be.common.exception.CustomException;
//import mediHub_be.common.exception.ErrorCode;
//import mediHub_be.cp.dto.ResponseCpDTO;
//import mediHub_be.cp.repository.CpVersionRepository;
//import mediHub_be.cp.repository.JooqCpVersionRepository;
//import mediHub_be.cp.service.CpService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class CpServiceTest {
//
//    @Autowired
//    CpService cpService;
//
//    @MockBean
//    CpVersionRepository cpVersionRepository;
//
//    @MockBean
//    JooqCpVersionRepository jooqCpVersionRepository;
//
//    private static List<Map<String, Object>> mockCpVersionList;
//    private static List<ResponseCpDTO> mockCpVersionDtoList;
//
//    @BeforeAll
//    public static void setUp() {
//        // Mock 데이터 설정
//        Map<String, Object> cp1 = new HashMap<>();
//        cp1.put("cpVersionSeq", 1L);
//        cp1.put("cpName", "백내장");
//        cp1.put("cpDescription", "백내장 설명");
//        cp1.put("cpViewCount", 523L);
//        cp1.put("cpVersion", "1.0.0");
//        cp1.put("cpVersionDescription", "최초 업로드");
//        cp1.put("createdAt", LocalDateTime.of(2024, 1, 1, 0, 0));
//        cp1.put("cpUrl", "https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
//        cp1.put("userName", "임광택");
//        cp1.put("userId", "19615041");
//        cp1.put("partName", "안과");
//
//        Map<String, Object> cp2 = new HashMap<>();
//        cp2.put("cpVersionSeq", 2L);
//        cp2.put("cpName", "각막염");
//        cp2.put("cpDescription", "각막염 설명");
//        cp2.put("cpViewCount", 492L);
//        cp2.put("cpVersion", "1.1.0");
//        cp2.put("cpVersionDescription", "2일차 처방약 변경");
//        cp2.put("createdAt", LocalDateTime.of(2024, 1, 2, 0, 0));
//        cp2.put("cpUrl", "https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
//        cp2.put("userName", "김철수");
//        cp2.put("userId", "19615042");
//        cp2.put("partName", "안과");
//
//        ResponseCpDTO dto3 = ResponseCpDTO.builder()
//                .cpVersionSeq(1L)
//                .cpName("백내장")
//                .cpDescription("백내장 설명")
//                .cpViewCount(523L)
//                .cpVersion("1.0.0")
//                .cpVersionDescription("최초 업로드")
//                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
//                .cpUrl("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf")
//                .userName("임광택")
//                .userId("19615041")
//                .partName("안과")
//                .build();
//
//        ResponseCpDTO dto4 = ResponseCpDTO.builder()
//                .cpVersionSeq(2L)
//                .cpName("각막염")
//                .cpDescription("각막염 설명")
//                .cpViewCount(492L)
//                .cpVersion("1.1.0")
//                .cpVersionDescription("2일차 처방약 변경")
//                .createdAt(LocalDateTime.of(2024, 1, 2, 0, 0))
//                .cpUrl("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf")
//                .userName("김철수")
//                .userId("19615042")
//                .partName("안과")
//                .build();
//
//        mockCpVersionList = Arrays.asList(cp1, cp2);
//    }
//
//    @Test
//    @DisplayName(value = "CP 검색 카테고리 -> CP 찾기")
//    public void getCpListByCpSearchCategoryAndCpSearchCategoryDataTest() {
//        // Given
//        Mockito.when(jooqCpVersionRepository.findCpVersionByCategory(anyList()))
//                .thenReturn(mockCpVersionDtoList);
//
//        // When
//        List<ResponseCpDTO> result = cpService.getCpListByCpSearchCategoryAndCpSearchCategoryData(
//                List.of(1L, 2L, 3L),
//                List.of(1L, 2L, 3L)
//        );
//
//        // Then
//        // 첫 번째 DTO 검증
//        ResponseCpDTO dto1 = result.get(0);
//        assertThat(dto1.getCpVersion()).isEqualTo("1.0.0");
//        assertThat(dto1.getCpName()).isEqualTo("백내장");
//        assertThat(dto1.getCpDescription()).isEqualTo("백내장 설명");
//        assertThat(dto1.getCpViewCount()).isEqualTo(523L);
//        assertThat(dto1.getCpVersionDescription()).isEqualTo("최초 업로드");
//        assertThat(dto1.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
//        assertThat(dto1.getUserName()).isEqualTo("임광택");
//        assertThat(dto1.getUserId()).isEqualTo("19615041");
//        assertThat(dto1.getPartName()).isEqualTo("안과");
//
//        // 두 번째 DTO 검증
//        ResponseCpDTO dto2 = result.get(1);
//        assertThat(dto2.getCpVersion()).isEqualTo("1.1.0");
//        assertThat(dto2.getCpName()).isEqualTo("각막염");
//        assertThat(dto2.getCpDescription()).isEqualTo("각막염 설명");
//        assertThat(dto2.getCpViewCount()).isEqualTo(492L);
//        assertThat(dto2.getCpVersionDescription()).isEqualTo("2일차 처방약 변경");
//        assertThat(dto2.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 2, 0, 0));
//        assertThat(dto2.getUserName()).isEqualTo("김철수");
//        assertThat(dto2.getUserId()).isEqualTo("19615042");
//        assertThat(dto2.getPartName()).isEqualTo("안과");
//
//        // 메서드 호출 여부 검증
//        Mockito.verify(cpVersionRepository).findByCategorySeqAndCategoryData(anyList(), anyList());
//    }
//
//    @Test
//    @DisplayName("CP명 -> CP 찾기")
//    public void findByCpNameContainingIgnoreCaseTest() {
//        // Given
//        Mockito.when(cpVersionRepository.findByCpNameContainingIgnoreCase(anyString()))
//                .thenReturn(mockCpVersionList);
//
//        // When
//        List<Map<String, Object>> result = cpVersionRepository.findByCpNameContainingIgnoreCase("백내장");
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.size()).isEqualTo(2);
//
//        // 첫 번째 결과 검증
//        Map<String, Object> firstResult = result.get(0);
//        assertThat(firstResult.get("cpVersionSeq")).isEqualTo(1L);
//        assertThat(firstResult.get("cpName")).isEqualTo("백내장");
//        assertThat(firstResult.get("cpDescription")).isEqualTo("백내장 설명");
//        assertThat(firstResult.get("cpViewCount")).isEqualTo(523L);
//        assertThat(firstResult.get("cpVersion")).isEqualTo("1.0.0");
//        assertThat(firstResult.get("cpVersionDescription")).isEqualTo("최초 업로드");
//        assertThat(firstResult.get("createdAt")).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
//        assertThat(firstResult.get("cpUrl")).isEqualTo("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
//        assertThat(firstResult.get("userName")).isEqualTo("임광택");
//        assertThat(firstResult.get("userId")).isEqualTo("19615041");
//        assertThat(firstResult.get("partName")).isEqualTo("안과");
//
//        // 두 번째 결과 검증
//        Map<String, Object> secondResult = result.get(1);
//        assertThat(secondResult.get("cpVersionSeq")).isEqualTo(2L);
//        assertThat(secondResult.get("cpName")).isEqualTo("각막염");
//        assertThat(secondResult.get("cpDescription")).isEqualTo("각막염 설명");
//        assertThat(secondResult.get("cpViewCount")).isEqualTo(492L);
//        assertThat(secondResult.get("cpVersion")).isEqualTo("1.1.0");
//        assertThat(secondResult.get("cpVersionDescription")).isEqualTo("2일차 처방약 변경");
//        assertThat(secondResult.get("createdAt")).isEqualTo(LocalDateTime.of(2024, 1, 2, 0, 0));
//        assertThat(secondResult.get("cpUrl")).isEqualTo("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
//        assertThat(secondResult.get("userName")).isEqualTo("김철수");
//        assertThat(secondResult.get("userId")).isEqualTo("19615042");
//        assertThat(secondResult.get("partName")).isEqualTo("안과");
//    }
//
//    @Test
//    @DisplayName("CP 버전 번호 -> CP 찾기")
//    public void findByCpVersionSeqTest() {
//        // Given
//        ResponseCpDTO expectedResponse = ResponseCpDTO.builder()
//                .cpVersionSeq(1L)
//                .cpName("백내장")
//                .cpDescription("백내장 설명")
//                .cpViewCount(523L)
//                .cpVersion("1.0.0")
//                .cpVersionDescription("최초 업로드")
//                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
//                .cpUrl("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf")
//                .userName("임광택")
//                .userId("19615041")
//                .partName("안과")
//                .build();
//
//        Mockito.when(cpVersionRepository.findByCpVersionSeq(anyLong()))
//                .thenReturn(Optional.of(expectedResponse));
//
//        // When
//        ResponseCpDTO result = cpVersionRepository.findByCpVersionSeq(1L)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CP_VERSION));
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getCpVersionSeq()).isEqualTo(1L);
//        assertThat(result.getCpName()).isEqualTo("백내장");
//        assertThat(result.getCpDescription()).isEqualTo("백내장 설명");
//        assertThat(result.getCpViewCount()).isEqualTo(523L);
//        assertThat(result.getCpVersion()).isEqualTo("1.0.0");
//        assertThat(result.getCpVersionDescription()).isEqualTo("최초 업로드");
//        assertThat(result.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
//        assertThat(result.getCpUrl()).isEqualTo("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
//        assertThat(result.getUserName()).isEqualTo("임광택");
//        assertThat(result.getUserId()).isEqualTo("19615041");
//        assertThat(result.getPartName()).isEqualTo("안과");
//
//        // 메서드 호출 여부 검증
//        Mockito.verify(cpVersionRepository).findByCpVersionSeq(anyLong());
//    }
//
//    @Test
//    @DisplayName("전체 CP 조회")
//    public void getCpListTest() {
//        // Given
//        ResponseCpDTO dto1 = ResponseCpDTO.toDto(mockCpVersionList.get(0));
//        ResponseCpDTO dto2 = ResponseCpDTO.toDto(mockCpVersionList.get(1));
//
//        List<ResponseCpDTO> mockitoCpList = Arrays.asList(dto1, dto2);
//
//        Mockito.when(cpVersionRepository.findLatestCp())
//                .thenReturn(mockitoCpList);
//
//        // When
//        List<ResponseCpDTO> result = cpVersionRepository.findLatestCp();
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.size()).isEqualTo(2);
//
//        // 첫 번째 DTO 검증
//        ResponseCpDTO firstDto = result.get(0);
//        assertThat(firstDto.getCpVersionSeq()).isEqualTo(1L);
//        assertThat(firstDto.getCpName()).isEqualTo("백내장");
//        assertThat(firstDto.getCpDescription()).isEqualTo("백내장 설명");
//        assertThat(firstDto.getCpViewCount()).isEqualTo(523L);
//        assertThat(firstDto.getCpVersion()).isEqualTo("1.0.0");
//        assertThat(firstDto.getCpVersionDescription()).isEqualTo("최초 업로드");
//        assertThat(firstDto.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
//        assertThat(firstDto.getCpUrl()).isEqualTo("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
//        assertThat(firstDto.getUserName()).isEqualTo("임광택");
//        assertThat(firstDto.getUserId()).isEqualTo("19615041");
//        assertThat(firstDto.getPartName()).isEqualTo("안과");
//
//        // 두 번째 DTO 검증
//        ResponseCpDTO secondDto = result.get(1);
//        assertThat(secondDto.getCpVersionSeq()).isEqualTo(2L);
//        assertThat(secondDto.getCpName()).isEqualTo("각막염");
//        assertThat(secondDto.getCpDescription()).isEqualTo("각막염 설명");
//        assertThat(secondDto.getCpViewCount()).isEqualTo(492L);
//        assertThat(secondDto.getCpVersion()).isEqualTo("1.1.0");
//        assertThat(secondDto.getCpVersionDescription()).isEqualTo("2일차 처방약 변경");
//        assertThat(secondDto.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 2, 0, 0));
//        assertThat(secondDto.getCpUrl()).isEqualTo("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
//        assertThat(secondDto.getUserName()).isEqualTo("김철수");
//        assertThat(secondDto.getUserId()).isEqualTo("19615042");
//        assertThat(secondDto.getPartName()).isEqualTo("안과");
//    }
//
//}
//
