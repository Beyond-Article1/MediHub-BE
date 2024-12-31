package mediHub_be.src.test.cp.service;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpSearchDataDTO;
import mediHub_be.cp.entity.CpSearchData;
import mediHub_be.cp.repository.CpSearchDataRepository;
import mediHub_be.cp.service.CpSearchDataService;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.UserAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CpSearchDataServiceTest {

    @InjectMocks
    private CpSearchDataService cpSearchDataService;

    @Mock
    private CpSearchDataRepository cpSearchDataRepository;

    private final static long CURRENT_USER_SEQ = 1L;
    private final static String CURRENT_USER_AUTH_ADMIN = UserAuth.ADMIN.name();

    private static List<ResponseCpSearchDataDTO> mockedResponseCpSearchDataDtoList;

    @BeforeEach
    void setUpBeforeEach() {
        ResponseCpSearchDataDTO dto1 = ResponseCpSearchDataDTO.builder()
                .cpSearchDataSeq(1L)
                .cpVersionSeq(1L)
                .cpName("백혈병")
                .cpSearchCategoryDataSeq(1L)
                .cpSearchCategoryName("식이")
                .build();

        ResponseCpSearchDataDTO dto2 = ResponseCpSearchDataDTO.builder()
                .cpSearchDataSeq(2L)
                .cpVersionSeq(1L)
                .cpName("백혈병")
                .cpSearchCategoryDataSeq(2L)
                .cpSearchCategoryName("처방")
                .build();

        mockedResponseCpSearchDataDtoList = new ArrayList<>();
        mockedResponseCpSearchDataDtoList.add(dto1);
        mockedResponseCpSearchDataDtoList.add(dto2);
    }

    @Test
    @DisplayName("CP 검색 데이터 목록 조회 성공 테스트")
    void getCpSearchDataListByCpVersionSeq_Success() {
        // Given
        long cpVersionSeq = 1L;

        Mockito.when(cpSearchDataRepository.findDtoJoinCpSearchCategoryDataOnCpSearchCategorySeqCpVersionOnCpVersionSeq(cpVersionSeq))
                .thenReturn(mockedResponseCpSearchDataDtoList); // Mocking 데이터 반환

        // When
        List<ResponseCpSearchDataDTO> result = cpSearchDataService.getCpSearchDataListByCpVersionSeq(cpVersionSeq);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("식이", result.get(0).getCpSearchCategoryName());
        Mockito.verify(cpSearchDataRepository).findDtoJoinCpSearchCategoryDataOnCpSearchCategorySeqCpVersionOnCpVersionSeq(cpVersionSeq);
    }

    @Test
    @DisplayName("CP 검색 데이터 목록 조회 시 데이터 접근 오류 테스트")
    void getCpSearchDataListByCpVersionSeq_DataAccessException() {
        // Given
        long cpVersionSeq = 1L;
        Mockito.when(cpSearchDataRepository.findDtoJoinCpSearchCategoryDataOnCpSearchCategorySeqCpVersionOnCpVersionSeq(cpVersionSeq))
                .thenThrow(new DataAccessException("Database error") {
                });

        // When & Then
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cpSearchDataService.getCpSearchDataListByCpVersionSeq(cpVersionSeq);
        });

        // Validate the exception
        Assertions.assertEquals(ErrorCode.INTERNAL_DATA_ACCESS_ERROR, exception.getErrorCode());
        Mockito.verify(cpSearchDataRepository).findDtoJoinCpSearchCategoryDataOnCpSearchCategorySeqCpVersionOnCpVersionSeq(cpVersionSeq);
    }

    @Test
    @DisplayName("CP 검색 데이터 삭제 성공 테스트")
    void deleteCpSearchData_Success() {
        // Given
        long cpSearchDataSeq = 1L;
        CpSearchData existingEntity = CpSearchData.builder()
                .cpVersionSeq(1L)
                .cpSearchCategoryDataSeq(1L)
                .build();

        // Mocking: findById 메서드가 호출될 때 existingEntity를 반환하도록 설정
        Mockito.when(cpSearchDataRepository.findById(cpSearchDataSeq)).thenReturn(Optional.of(existingEntity));
        Mockito.when(cpSearchDataRepository.existsById(cpSearchDataSeq)).thenReturn(true);

        // When
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(CURRENT_USER_SEQ); // 현재 사용자 시퀀스 설정
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities).thenReturn(CURRENT_USER_AUTH_ADMIN); // 현재 사용자 권한 설정

            cpSearchDataService.deleteCpSearchData(cpSearchDataSeq);

            // Then
            Mockito.verify(cpSearchDataRepository).deleteById(cpSearchDataSeq); // 삭제 메서드 호출 검증
        }
    }

    @Test
    @DisplayName("CP 검색 데이터 삭제 시 권한 오류 테스트")
    void deleteCpSearchData_Unauthorized() {
        // Given
        long cpSearchDataSeq = 1L;

        // When
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(CURRENT_USER_SEQ);
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities).thenReturn(UserAuth.USER.name()); // ADMIN이 아닌 경우 설정

            // Then
            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
                cpSearchDataService.deleteCpSearchData(cpSearchDataSeq);
            });

            // Validate the exception
            Assertions.assertEquals(ErrorCode.UNAUTHORIZED_USER, exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("CP 검색 데이터 삭제 시 데이터 없음 예외 테스트")
    void deleteCpSearchData_NotFound() {
        // Given
        long cpSearchDataSeq = 100L;
        Mockito.when(cpSearchDataRepository.existsById(cpSearchDataSeq)).thenReturn(false); // 데이터 없음

        // When & Then
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(CURRENT_USER_SEQ);
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities).thenReturn(CURRENT_USER_AUTH_ADMIN);

            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
                cpSearchDataService.deleteCpSearchData(cpSearchDataSeq);
            });

            // Validate the exception
            Assertions.assertEquals(ErrorCode.NOT_FOUND_CP_SEARCH_DATA, exception.getErrorCode());
            Mockito.verify(cpSearchDataRepository).existsById(cpSearchDataSeq); // 메서드 호출 검증
        }
    }
}

