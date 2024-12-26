package mediHub_be.src.test.cp.service;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpSearchCategoryDataDTO;
import mediHub_be.cp.entity.CpSearchCategoryData;
import mediHub_be.cp.repository.CpSearchCategoryDataRepository;
import mediHub_be.cp.service.CpSearchCategoryDataService;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.UserAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                .thenThrow(new DataAccessException("Database error") {
                });

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

    @Test
    @DisplayName("CP 검색 카테고리 데이터 단일 조회 성공 테스트")
    void getCpSearchCategoryData_Success() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        ResponseCpSearchCategoryDataDTO expectedDto = ResponseCpSearchCategoryDataDTO.builder()
                .cpSearchCategoryDataSeq(cpSearchCategoryDataSeq)
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

        Mockito.when(cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(expectedDto)); // Mocking 데이터 반환

        // When
        ResponseCpSearchCategoryDataDTO result = cpSearchCategoryDataService.getCpSearchCategoryData(1L, cpSearchCategoryDataSeq);

        // Then
        Assertions.assertNotNull(result); // 결과가 null이 아님을 확인
        Assertions.assertEquals(expectedDto.getCpSearchCategoryDataSeq(), result.getCpSearchCategoryDataSeq()); // 시퀀스 검증
        Assertions.assertEquals(expectedDto.getCpSearchCategoryDataName(), result.getCpSearchCategoryDataName()); // 데이터 이름 검증

        Mockito.verify(cpSearchCategoryDataRepository).findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 조회 시 데이터 없음 예외 테스트")
    void getCpSearchCategoryData_NotFound() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        Mockito.when(cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq))
                .thenReturn(Optional.empty()); // 데이터 없음

        // When & Then
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cpSearchCategoryDataService.getCpSearchCategoryData(1L, cpSearchCategoryDataSeq);
        });

        // Validate the exception
        Assertions.assertEquals(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA, exception.getErrorCode());
        Mockito.verify(cpSearchCategoryDataRepository).findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 조회 시 데이터 접근 오류 테스트")
    void getCpSearchCategoryData_DataAccessException() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        Mockito.when(cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq))
                .thenThrow(new DataAccessException("Database error") {
                });

        // When & Then
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cpSearchCategoryDataService.getCpSearchCategoryData(1L, cpSearchCategoryDataSeq);
        });

        // Validate the exception
        Assertions.assertEquals(ErrorCode.INTERNAL_DATA_ACCESS_ERROR, exception.getErrorCode());
        Mockito.verify(cpSearchCategoryDataRepository).findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 조회 시 예기치 않은 오류 테스트")
    void getCpSearchCategoryData_UnexpectedException() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        Mockito.when(cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            cpSearchCategoryDataService.getCpSearchCategoryData(1L, cpSearchCategoryDataSeq);
        });

        // Validate the exception
        Assertions.assertEquals("CP 검색 카테고리 조회 시 예기치 못한 에러가 발생했습니다.", exception.getMessage());
        Mockito.verify(cpSearchCategoryDataRepository).findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 업데이트 성공 테스트")
    void updateCpSearchCategoryData_Success() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        String newDataName = "Updated Data Name";

        // Mocking 기존 카테고리 데이터 엔티티
        CpSearchCategoryData existingEntity = CpSearchCategoryData.builder()
                .cpSearchCategorySeq(1L)
                .cpSearchCategoryDataName("Old Data Name")
                .build();

        // Mocking 업데이트 후 반환될 엔티티
        CpSearchCategoryData updatedEntity = CpSearchCategoryData.builder()
                .cpSearchCategorySeq(1L)
                .cpSearchCategoryDataName(newDataName)
                .build();

        // Mocking DTO 변환
        ResponseCpSearchCategoryDataDTO updatedDto = ResponseCpSearchCategoryDataDTO.builder()
                .cpSearchCategoryDataSeq(cpSearchCategoryDataSeq)
                .cpSearchCategorySeq(1L)
                .cpSearchCategoryDataName(newDataName)
                .userSeq(CURRENT_USER_SEQ)
                .userName("임광택")
                .userId("user01")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deletedAt(null)
                .build();

        // Mocking repository 메서드
        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(existingEntity)); // 기존 데이터 반환
        Mockito.when(cpSearchCategoryDataRepository.save(existingEntity))
                .thenReturn(updatedEntity); // 업데이트된 엔티티 반환
        Mockito.when(cpSearchCategoryDataRepository.findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(updatedDto)); // 업데이트 후 DTO 반환

        // When
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(CURRENT_USER_SEQ); // 현재 사용자 시퀀스 설정
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities).thenReturn(CURRENT_USER_AUTH_ADMIN); // 현재 사용자 권한 설정

            ResponseCpSearchCategoryDataDTO result = cpSearchCategoryDataService.updateCpSearchCategoryDataData(cpSearchCategoryDataSeq, newDataName);

            // Then
            Assertions.assertNotNull(result); // 결과가 null이 아님을 확인
            Assertions.assertEquals(newDataName, result.getCpSearchCategoryDataName()); // 데이터 이름 검증
            Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
            Mockito.verify(cpSearchCategoryDataRepository).save(existingEntity); // 저장 메서드 호출 검증
            Mockito.verify(cpSearchCategoryDataRepository).findByCpSearchCategoryDataSeq(cpSearchCategoryDataSeq); // 업데이트 후 DTO 조회 검증
        }
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 업데이트 시 데이터 없음 예외 테스트")
    void updateCpSearchCategoryData_NotFound() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        String newDataName = "Updated Data Name";

        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.empty()); // 데이터 없음

        // When & Then
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cpSearchCategoryDataService.updateCpSearchCategoryDataData(cpSearchCategoryDataSeq, newDataName);
        });

        // Validate the exception
        Assertions.assertEquals(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA, exception.getErrorCode());
        Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 업데이트 시 데이터베이스 접근 오류 테스트")
    void updateCpSearchCategoryData_DataAccessException() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        String newDataName = "Updated Data Name";

        CpSearchCategoryData existingEntity = CpSearchCategoryData.builder()
                .cpSearchCategorySeq(1L)
                .cpSearchCategoryDataName("Old Data Name")
                .build();

        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(existingEntity)); // 기존 데이터 반환
        Mockito.doThrow(new DataAccessException("Database error") {
                })
                .when(cpSearchCategoryDataRepository).save(existingEntity); // 저장 시 오류 발생

        // When & Then
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
                    .thenReturn(CURRENT_USER_SEQ);

            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
                cpSearchCategoryDataService.updateCpSearchCategoryDataData(cpSearchCategoryDataSeq, newDataName);
            });

            // Validate the exception
            Assertions.assertEquals(ErrorCode.INTERNAL_DATABASE_ERROR, exception.getErrorCode());
            Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
            Mockito.verify(cpSearchCategoryDataRepository).save(existingEntity); // 저장 메서드 호출 검증
        }
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 업데이트 시 예기치 않은 오류 테스트")
    void updateCpSearchCategoryData_UnexpectedException() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        String newDataName = "Updated Data Name";

        CpSearchCategoryData existingEntity = CpSearchCategoryData.builder()
                .cpSearchCategorySeq(1L)
                .cpSearchCategoryDataName("Old Data Name")
                .build();

        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(existingEntity)); // 기존 데이터 반환
        Mockito.doThrow(new RuntimeException("Unexpected error"))
                .when(cpSearchCategoryDataRepository).save(existingEntity); // 저장 시 예기치 않은 오류 발생

        // When & Then
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
                    .thenReturn(CURRENT_USER_SEQ);

            RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
                cpSearchCategoryDataService.updateCpSearchCategoryDataData(cpSearchCategoryDataSeq, newDataName);
            });

            // Validate the exception
            Assertions.assertEquals("CP 검색 카테고리 데이터 업데이트 중 예기치 못한 오류 발생했습니다.", exception.getMessage());
            Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
            Mockito.verify(cpSearchCategoryDataRepository).save(existingEntity); // 저장 메서드 호출 검증
        }
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 삭제 성공 테스트")
    void deleteCpSearchCategoryData_Success() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        CpSearchCategoryData existingEntity = CpSearchCategoryData.builder()
                .cpSearchCategoryDataName("Test Data")
                .build();

        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(existingEntity)); // 기존 데이터 반환

        // When
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(CURRENT_USER_SEQ); // 현재 사용자 시퀀스 설정

            cpSearchCategoryDataService.deleteCpSearchCategoryData(cpSearchCategoryDataSeq);

            // Then
            Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
            Mockito.verify(cpSearchCategoryDataRepository).save(existingEntity); // 저장 메서드 호출 검증
        }
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 삭제 시 데이터 없음 예외 테스트")
    void deleteCpSearchCategoryData_NotFound() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.empty()); // 데이터 없음

        // When & Then
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cpSearchCategoryDataService.deleteCpSearchCategoryData(cpSearchCategoryDataSeq);
        });

        // Validate the exception
        Assertions.assertEquals(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY_DATA, exception.getErrorCode());
        Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 삭제 시 로그인 필요 예외 테스트")
    void deleteCpSearchCategoryData_NeedLogin() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        CpSearchCategoryData existingEntity = CpSearchCategoryData.builder()
                .cpSearchCategoryDataName("Test Data")
                .build();

        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(existingEntity)); // 기존 데이터 반환

        // When & Then
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
                mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(null); // 로그인하지 않은 경우

                cpSearchCategoryDataService.deleteCpSearchCategoryData(cpSearchCategoryDataSeq);
            }
        });

        // Validate the exception
        Assertions.assertEquals(ErrorCode.NEED_LOGIN, exception.getErrorCode());
        Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 삭제 시 데이터베이스 접근 오류 테스트")
    void deleteCpSearchCategoryData_DataAccessException() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        CpSearchCategoryData existingEntity = CpSearchCategoryData.builder()
                .cpSearchCategoryDataName("Test Data")
                .build();

        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(existingEntity)); // 기존 데이터 반환

        // When
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(CURRENT_USER_SEQ); // 현재 사용자 시퀀스 설정
            Mockito.doThrow(new DataAccessException("Database error") {})
                    .when(cpSearchCategoryDataRepository).save(existingEntity); // 저장 시 오류 발생

            // Then
            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
                cpSearchCategoryDataService.deleteCpSearchCategoryData(cpSearchCategoryDataSeq);
            });

            // Validate the exception
            Assertions.assertEquals(ErrorCode.INTERNAL_DATABASE_ERROR, exception.getErrorCode());
            Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
            Mockito.verify(cpSearchCategoryDataRepository).save(existingEntity); // 저장 메서드 호출 검증
        }
    }

    @Test
    @DisplayName("CP 검색 카테고리 데이터 삭제 시 예기치 않은 오류 테스트")
    void deleteCpSearchCategoryData_UnexpectedException() {
        // Given
        long cpSearchCategoryDataSeq = 1L;
        CpSearchCategoryData existingEntity = CpSearchCategoryData.builder()
                .cpSearchCategoryDataName("Test Data")
                .build();

        Mockito.when(cpSearchCategoryDataRepository.findById(cpSearchCategoryDataSeq))
                .thenReturn(Optional.of(existingEntity)); // 기존 데이터 반환

        // When
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(CURRENT_USER_SEQ); // 현재 사용자 시퀀스 설정
            Mockito.doThrow(new RuntimeException("Unexpected error"))
                    .when(cpSearchCategoryDataRepository).save(existingEntity); // 저장 시 예기치 않은 오류 발생

            // Then
            RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
                cpSearchCategoryDataService.deleteCpSearchCategoryData(cpSearchCategoryDataSeq);
            });

            // Validate the exception
            Assertions.assertEquals("CP 검색 카테고리 데이터 삭제 중 에상치 못한 오류 발생했습니다.", exception.getMessage());
            Mockito.verify(cpSearchCategoryDataRepository).findById(cpSearchCategoryDataSeq); // 메서드 호출 검증
            Mockito.verify(cpSearchCategoryDataRepository).save(existingEntity); // 저장 메서드 호출 검증
        }
    }
}
