package mediHub_be.src.test.cp.service;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpSearchCategoryDataDTO;
import mediHub_be.cp.repository.CpSearchCategoryDataRepository;
import mediHub_be.cp.service.CpSearchCategoryDataService;
import mediHub_be.user.entity.UserAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CpSearchCategoryDataServiceTest {

    @Autowired
    private CpSearchCategoryDataService cpSearchCategoryDataService;

    @MockBean
    private CpSearchCategoryDataRepository cpSearchCategoryDataRepository;

    private final static long CURRENT_USER_SEQ = 1L;
    private final static String CURRENT_USER_AUTH_USER = UserAuth.USER.name();
    private final static String CURRENT_USER_AUTH_ADMIN = UserAuth.ADMIN.name();

    private static List<ResponseCpSearchCategoryDataDTO> mockedResponseCpSearchCategoryDataDtoList;

    @BeforeEach
    void setUp() {
        ResponseCpSearchCategoryDataDTO dto1 = ResponseCpSearchCategoryDataDTO.builder()
                .cpSearchCategoryDataSeq(1L)
                .cpSearchCategorySeq(1L)
                .cpSearchCategoryName("식이")
                .cpSearchCategoryDataName("RD")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .userSeq(CURRENT_USER_SEQ)
                .userName("임광택")
                .userId("user01")
                .build();

        ResponseCpSearchCategoryDataDTO dto2 = ResponseCpSearchCategoryDataDTO.builder()
                .cpSearchCategoryDataSeq(2L)
                .cpSearchCategorySeq(1L) // 동일한 cpSearchCategorySeq로 설정
                .cpSearchCategoryName("식이")
                .cpSearchCategoryDataName("ENT")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .userSeq(CURRENT_USER_SEQ)
                .userName("임광택")
                .userId("user01")
                .build();

        mockedResponseCpSearchCategoryDataDtoList = new ArrayList<>();
        mockedResponseCpSearchCategoryDataDtoList.add(dto1);
        mockedResponseCpSearchCategoryDataDtoList.add(dto2);
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 조회 성공 테스트")
    void getCpSearchCategoryDataListByCpSearchCategorySeq_Success() {
        // Given
        long cpSearchCategorySeq = 1L;

        Mockito.when(cpSearchCategoryDataRepository.findByCpSearchCategorySeq(cpSearchCategorySeq))
                .thenReturn(mockedResponseCpSearchCategoryDataDtoList); // Mocking 데이터 반환

        // When
        List<ResponseCpSearchCategoryDataDTO> result = cpSearchCategoryDataService.getCpSearchCategoryDataListByCpSearchCategorySeq(cpSearchCategorySeq);

        // Then
        Assertions.assertNotNull(result); // 결과가 null이 아님을 확인
        Assertions.assertEquals(2, result.size()); // 데이터 수 검증
        Assertions.assertEquals("RD", result.get(0).getCpSearchCategoryDataName()); // 첫 번째 데이터 이름 검증
        Assertions.assertEquals("ENT", result.get(1).getCpSearchCategoryDataName()); // 두 번째 데이터 이름 검증

        Mockito.verify(cpSearchCategoryDataRepository).findByCpSearchCategorySeq(cpSearchCategorySeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 조회 시 데이터 접근 오류 테스트")
    void getCpSearchCategoryDataListByCpSearchCategorySeq_DataAccessException() {
        // Given
        long cpSearchCategorySeq = 1L;
        Mockito.when(cpSearchCategoryDataRepository.findByCpSearchCategorySeq(cpSearchCategorySeq))
                .thenThrow(new DataAccessException("Database error") {});

        // When & Then
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cpSearchCategoryDataService.getCpSearchCategoryDataListByCpSearchCategorySeq(cpSearchCategorySeq);
        });

        // Validate the exception
        Assertions.assertEquals(ErrorCode.INTERNAL_DATA_ACCESS_ERROR, exception.getErrorCode());
        Mockito.verify(cpSearchCategoryDataRepository).findByCpSearchCategorySeq(cpSearchCategorySeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 조회 시 예기치 않은 오류 테스트")
    void getCpSearchCategoryDataListByCpSearchCategorySeq_UnexpectedException() {
        // Given
        long cpSearchCategorySeq = 1L;
        Mockito.when(cpSearchCategoryDataRepository.findByCpSearchCategorySeq(cpSearchCategorySeq))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            cpSearchCategoryDataService.getCpSearchCategoryDataListByCpSearchCategorySeq(cpSearchCategorySeq);
        });

        // Validate the exception
        Assertions.assertEquals("CP 검색 카테고리 데이터 조회 중 예상치 못한 에러가 발생했습니다.", exception.getMessage());
        Mockito.verify(cpSearchCategoryDataRepository).findByCpSearchCategorySeq(cpSearchCategorySeq); // 메서드 호출 검증
    }
}
