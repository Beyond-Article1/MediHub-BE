package mediHub_be.src.test.cp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.entity.Cp;
import mediHub_be.cp.repository.CpRepository;
import mediHub_be.cp.repository.CpVersionRepository;
import mediHub_be.cp.service.CpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class CpServiceTest {

    @InjectMocks
    CpService cpService;

    @Mock
    BookmarkService bookmarkService;

    @Mock
    CpRepository cpRepository;

    @Mock
    CpVersionRepository cpVersionRepository;

    @Mock
    ViewCountManager viewCountManager;

    private static List<Map<String, Object>> mockCpVersionList;

    @BeforeEach
    void setUpBeforeEach() {
        // Mock 데이터 설정
        Map<String, Object> cp1 = new HashMap<>();
        cp1.put("cpVersionSeq", 1L);
        cp1.put("cpName", "백내장");
        cp1.put("cpDescription", "백내장 설명");
        cp1.put("cpViewCount", 523L);
        cp1.put("cpVersion", "1.0.0");
        cp1.put("cpVersionDescription", "최초 업로드");
        cp1.put("createdAt", LocalDateTime.of(2024, 1, 1, 0, 0));
        cp1.put("cpUrl", "https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
        cp1.put("userName", "임광택");
        cp1.put("userId", "19615041");
        cp1.put("partName", "안과");

        Map<String, Object> cp2 = new HashMap<>();
        cp2.put("cpVersionSeq", 2L);
        cp2.put("cpName", "각막염");
        cp2.put("cpDescription", "각막염 설명");
        cp2.put("cpViewCount", 492L);
        cp2.put("cpVersion", "1.1.0");
        cp2.put("cpVersionDescription", "2일차 처방약 변경");
        cp2.put("createdAt", LocalDateTime.of(2024, 1, 2, 0, 0));
        cp2.put("cpUrl", "https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
        cp2.put("userName", "김철수");
        cp2.put("userId", "19615042");
        cp2.put("partName", "안과");

        mockCpVersionList = Arrays.asList(cp1, cp2);
    }


    @Test
    @DisplayName("CP 명을 입력받아 CP 검색 테스트 (성공)")
    public void findByCpNameContainingIgnoreCaseTest() {
        // Given
        List<Map<String, Object>> answer = Collections.singletonList(mockCpVersionList.get(0));

        Mockito.when(cpVersionRepository.findByCpNameContainingIgnoreCase(anyString()))
                .thenReturn(answer);

        // When
        List<ResponseCpDTO> result = cpService.getCpListByCpName("백내장");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);

        // 첫 번째 결과 검증
        ResponseCpDTO dto1 = result.get(0);
        assertThat(dto1.getCpVersionSeq()).isEqualTo(1L);
        assertThat(dto1.getCpName()).isEqualTo("백내장");
        assertThat(dto1.getCpDescription()).isEqualTo("백내장 설명");
        assertThat(dto1.getCpViewCount()).isEqualTo(523L);
        assertThat(dto1.getCpVersion()).isEqualTo("1.0.0");
        assertThat(dto1.getCpVersionDescription()).isEqualTo("최초 업로드");
        assertThat(dto1.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertThat(dto1.getUserName()).isEqualTo("임광택");
        assertThat(dto1.getUserId()).isEqualTo("19615041");
        assertThat(dto1.getPartName()).isEqualTo("안과");
    }


    @Test
    @DisplayName("CP 버전 번호를 입력받아 CP 검색 테스트 (성공)")
    public void findByCpVersionSeqTest() {
        // Given
        Cp expectedCp = Cp.builder()
                .cpSeq(1L)
                .userSeq(1L)
                .cpName("백내장")
                .cpDescription("백내장 설명")
                .cpViewCount(523L)
                .build();

        ResponseCpDTO expectedResponse = ResponseCpDTO.builder()
                .cpVersionSeq(1L)
                .cpName("백내장")
                .cpDescription("백내장 설명")
                .cpViewCount(523L)
                .cpVersion("1.0.0")
                .cpVersionDescription("최초 업로드")
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .cpUrl("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf")
                .userName("임광택")
                .userId("19615041")
                .partName("안과")
                .build();

        // HttpServletRequest Mock 생성
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        // HttpServletResponse Mock 생성
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(cpRepository.findByCpVersionSeq(anyLong()))
                .thenReturn(Optional.of(expectedCp));

        Mockito.when(cpVersionRepository.findByCpVersionSeq(anyLong()))
                .thenReturn(Optional.of(expectedResponse));

        // ViewCountManager의 shouldIncreaseViewCount 메서드 Mock 설정
        Mockito.when(viewCountManager.shouldIncreaseViewCount(anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(true); // 뷰 카운트를 증가시키도록 설정

        // When
        ResponseCpDTO result = cpService.getCpByCpVersionSeq(1L, request, response);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCpVersionSeq()).isEqualTo(1L);
        assertThat(result.getCpName()).isEqualTo("백내장");
        assertThat(result.getCpDescription()).isEqualTo("백내장 설명");
        assertThat(result.getCpViewCount()).isEqualTo(523L);
        assertThat(result.getCpVersion()).isEqualTo("1.0.0");
        assertThat(result.getCpVersionDescription()).isEqualTo("최초 업로드");
        assertThat(result.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertThat(result.getCpUrl()).isEqualTo("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
        assertThat(result.getUserName()).isEqualTo("임광택");
        assertThat(result.getUserId()).isEqualTo("19615041");
        assertThat(result.getPartName()).isEqualTo("안과");

        // 메서드 호출 여부 검증
        Mockito.verify(cpVersionRepository).findByCpVersionSeq(anyLong());
        Mockito.verify(cpRepository).findByCpVersionSeq(anyLong());
        Mockito.verify(viewCountManager).shouldIncreaseViewCount(anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("전체 CP 조회 테스트 (성공)")
    public void getCpListTest() {
        // Given
        ResponseCpDTO dto1 = ResponseCpDTO.toDto(mockCpVersionList.get(0));
        ResponseCpDTO dto2 = ResponseCpDTO.toDto(mockCpVersionList.get(1));

        List<ResponseCpDTO> mockitoCpList = Arrays.asList(dto1, dto2);

        Mockito.when(cpVersionRepository.findLatestCp())
                .thenReturn(mockitoCpList);

        Mockito.when(bookmarkService.isBookmarked(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(false);

        // When
        List<ResponseCpDTO> result = cpService.getCpList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        // 첫 번째 DTO 검증
        ResponseCpDTO firstDto = result.get(0);
        assertThat(firstDto.getCpVersionSeq()).isEqualTo(1L);
        assertThat(firstDto.getCpName()).isEqualTo("백내장");
        assertThat(firstDto.getCpDescription()).isEqualTo("백내장 설명");
        assertThat(firstDto.getCpViewCount()).isEqualTo(523L);
        assertThat(firstDto.getCpVersion()).isEqualTo("1.0.0");
        assertThat(firstDto.getCpVersionDescription()).isEqualTo("최초 업로드");
        assertThat(firstDto.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertThat(firstDto.getCpUrl()).isEqualTo("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
        assertThat(firstDto.getUserName()).isEqualTo("임광택");
        assertThat(firstDto.getUserId()).isEqualTo("19615041");
        assertThat(firstDto.getPartName()).isEqualTo("안과");

        // 두 번째 DTO 검증
        ResponseCpDTO secondDto = result.get(1);
        assertThat(secondDto.getCpVersionSeq()).isEqualTo(2L);
        assertThat(secondDto.getCpName()).isEqualTo("각막염");
        assertThat(secondDto.getCpDescription()).isEqualTo("각막염 설명");
        assertThat(secondDto.getCpViewCount()).isEqualTo(492L);
        assertThat(secondDto.getCpVersion()).isEqualTo("1.1.0");
        assertThat(secondDto.getCpVersionDescription()).isEqualTo("2일차 처방약 변경");
        assertThat(secondDto.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 2, 0, 0));
        assertThat(secondDto.getCpUrl()).isEqualTo("https://medihub.s3.ap-northeast-2.amazonaws.com/%5BCP%5D%EC%AF%94%EC%AF%94%EA%B0%80%EB%AC%B4%EC%8B%9C.1.0.0.pdf");
        assertThat(secondDto.getUserName()).isEqualTo("김철수");
        assertThat(secondDto.getUserId()).isEqualTo("19615042");
        assertThat(secondDto.getPartName()).isEqualTo("안과");
    }

}

