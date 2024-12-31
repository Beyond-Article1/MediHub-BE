//package mediHub_be.src.test.cp.service;
//
//import mediHub_be.common.exception.CustomException;
//import mediHub_be.common.exception.ErrorCode;
//import mediHub_be.cp.dto.ResponseCpSearchCategoryDTO;
//import mediHub_be.cp.entity.CpSearchCategory;
//import mediHub_be.cp.repository.CpSearchCategoryDataRepository;
//import mediHub_be.cp.repository.CpSearchCategoryRepository;
//import mediHub_be.cp.service.CpSearchCategoryService;
//import mediHub_be.security.util.SecurityUtil;
//import mediHub_be.user.entity.UserAuth;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.dao.DataAccessException;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class CpSearchCategoryServiceTest {
//
//    @Autowired
//    private CpSearchCategoryService cpSearchCategoryService;
//
//    @MockBean
//    private CpSearchCategoryRepository cpSearchCategoryRepository;
//
//    @MockBean
//    private CpSearchCategoryDataRepository cpSearchCategoryDataRepository;
//
//    private static List<ResponseCpSearchCategoryDTO> dtoList;
//
//    private static final long CURRENT_USER_SEQ = 1L;
//    private static final String CURRENT_USER_AUTH_USER = UserAuth.USER.name();
//    private static final String CURRENT_USER_AUTH_ADMIN = UserAuth.ADMIN.name();
//
//    @BeforeAll
//    static void setUp() {
//        dtoList = new ArrayList<>();
//
//        ResponseCpSearchCategoryDTO dto1 = ResponseCpSearchCategoryDTO.builder()
//                .cpSearchCategorySeq(1L)
//                .cpSearchCategoryName("식이")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
//                .deletedAt(null)
//                .userSeq(1L)
//                .userName("임광택")
//                .userId("user01")
//                .build();
//
//        ResponseCpSearchCategoryDTO dto2 = ResponseCpSearchCategoryDTO.builder()
//                .cpSearchCategorySeq(2L)
//                .cpSearchCategoryName("투약")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
//                .deletedAt(null)
//                .userSeq(1L)
//                .userName("임광택")
//                .userId("user01")
//                .build();
//
//        dtoList.add(dto1);
//        dtoList.add(dto2);
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 리스트 조회 테스트")
//    void getCpSearchCategoryList() {
//        // Given
//        Mockito.when(cpSearchCategoryRepository.findJoinUserOnUserSeq())
//                .thenReturn(dtoList);
//
//        // When
//        List<ResponseCpSearchCategoryDTO> result = cpSearchCategoryService.getCpSearchCategoryList();
//
//        // Then
//        Assertions.assertNotNull(result);  // 결과가 null이 아님을 확인
//        Assertions.assertEquals(2, result.size());  // 결과의 크기 검증
//        Assertions.assertEquals("식이", result.get(0).getCpSearchCategoryName());  // 첫 번째 카테고리 이름 검증
//        Assertions.assertEquals("투약", result.get(1).getCpSearchCategoryName());  // 두 번째 카테고리 이름 검증
//
//        // verify 메서드가 호출되었는지 확인
//        Mockito.verify(cpSearchCategoryRepository).findJoinUserOnUserSeq();
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 조회 테스트")
//    void getCpSearchCategoryByCpSearchCategorySeq() {
//        // Given
//        long cpSearchCategorySeq = 1L;
//        Mockito.when(cpSearchCategoryRepository.findByCpSearchCategorySeq(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.of(dtoList.get(0))); // 첫 번째 카테고리 반환
//
//        // When
//        ResponseCpSearchCategoryDTO result = cpSearchCategoryService.getCpSearchCategoryByCpSearchCategorySeq(cpSearchCategorySeq);
//
//        // Then
//        Assertions.assertNotNull(result);  // 결과가 null이 아님을 확인
//        Assertions.assertEquals("식이", result.getCpSearchCategoryName());  // 카테고리 이름 검증
//        Assertions.assertEquals(1L, result.getCpSearchCategorySeq());  // 카테고리 시퀀스 검증
//
//        // verify 메서드가 호출되었는지 확인
//        Mockito.verify(cpSearchCategoryRepository).findByCpSearchCategorySeq(cpSearchCategorySeq);
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 조회 시 카테고리 없음 예외 테스트")
//    void getCpSearchCategoryByCpSearchCategorySeq_NotFound() {
//        // Given
//        long cpSearchCategorySeq = 3L; // 존재하지 않는 시퀀스
//        Mockito.when(cpSearchCategoryRepository.findByCpSearchCategorySeq(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.empty()); // 카테고리 없음
//
//        // When & Then
//        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//            cpSearchCategoryService.getCpSearchCategoryByCpSearchCategorySeq(cpSearchCategorySeq);
//        });
//
//        // Validate the exception
//        Assertions.assertEquals(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY, exception.getErrorCode());
//        Mockito.verify(cpSearchCategoryRepository).findByCpSearchCategorySeq(cpSearchCategorySeq); // 메서드 호출 검증
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 조회 시 데이터베이스 접근 오류 테스트")
//    void getCpSearchCategoryByCpSearchCategorySeq_DataAccessException() {
//        // Given
//        long cpSearchCategorySeq = 1L;
//        Mockito.when(cpSearchCategoryRepository.findByCpSearchCategorySeq(cpSearchCategorySeq))
//                .thenThrow(new DataAccessException("Database error") {
//                });
//
//        // When & Then
//        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//            cpSearchCategoryService.getCpSearchCategoryByCpSearchCategorySeq(cpSearchCategorySeq);
//        });
//
//        // Validate the exception
//        Assertions.assertEquals(ErrorCode.INTERNAL_DATA_ACCESS_ERROR, exception.getErrorCode());
//        Mockito.verify(cpSearchCategoryRepository).findByCpSearchCategorySeq(cpSearchCategorySeq); // 메서드 호출 검증
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 생성 성공 테스트")
//    void createCpSearchCategory_Success() {
//        // Given
//        String cpSearchCategoryName = "식이";
//
//        // Mocking CpSearchCategory 엔티티
//        CpSearchCategory newCategory = CpSearchCategory.builder()
//                .cpSearchCategoryName(cpSearchCategoryName)
//                .userSeq(1L)
//                .build();
//
//        // Mocking Response DTO
//        ResponseCpSearchCategoryDTO responseDto = ResponseCpSearchCategoryDTO.builder()
//                .cpSearchCategorySeq(newCategory.getCpSearchCategorySeq())
//                .cpSearchCategoryName(newCategory.getCpSearchCategoryName())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
//                .deletedAt(null)
//                .userSeq(newCategory.getUserSeq())
//                .userName("임광택")
//                .userId("user01")
//                .build();
//
//        Mockito.when(cpSearchCategoryRepository.save(Mockito.any(CpSearchCategory.class)))
//                .thenReturn(newCategory); // 엔티티 저장 후 반환
//        Mockito.when(cpSearchCategoryRepository.findByCpSearchCategorySeq(newCategory.getCpSearchCategorySeq()))
//                .thenReturn(java.util.Optional.of(responseDto));
//
//        // When
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//
//            ResponseCpSearchCategoryDTO result = cpSearchCategoryService.createCpSearchCategory(cpSearchCategoryName);
//
//            // Then
//            Assertions.assertNotNull(result);
//            Assertions.assertEquals(responseDto.getCpSearchCategoryName(), result.getCpSearchCategoryName());
//            Mockito.verify(cpSearchCategoryRepository).save(Mockito.any(CpSearchCategory.class));
//            Mockito.verify(cpSearchCategoryRepository).findByCpSearchCategorySeq(newCategory.getCpSearchCategorySeq());
//        }
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 생성 중복 검사 실패 테스트")
//    void createCpSearchCategory_Duplicate() {
//        // Given
//        String cpSearchCategoryName = "식이";
//        Mockito.when(cpSearchCategoryRepository.save(Mockito.any(CpSearchCategory.class)))
//                .thenThrow(new DataAccessException("Duplicate entry") {
//                });
//
//        // When & Then
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//
//            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//                cpSearchCategoryService.createCpSearchCategory(cpSearchCategoryName);
//            });
//
//            // Validate the exception
//            Assertions.assertEquals(ErrorCode.INTERNAL_DATABASE_ERROR, exception.getErrorCode());
//            Mockito.verify(cpSearchCategoryRepository).save(Mockito.any(CpSearchCategory.class));
//        }
//    }
//
//    @Test
//    @DisplayName("저장된 CP 검색 카테고리 조회 실패 테스트")
//    void createCpSearchCategory_NotFound() {
//        // Given
//        String cpSearchCategoryName = "식이";
//        CpSearchCategory newCategory = CpSearchCategory.builder()
//                .cpSearchCategoryName(cpSearchCategoryName)
//                .userSeq(1L)
//                .build();
//
//        Mockito.when(cpSearchCategoryRepository.save(Mockito.any(CpSearchCategory.class)))
//                .thenReturn(newCategory);
//        Mockito.when(cpSearchCategoryRepository.findByCpSearchCategorySeq(3L))
//                .thenReturn(java.util.Optional.empty());
//
//        // When & Then
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//                cpSearchCategoryService.createCpSearchCategory(cpSearchCategoryName);
//            });
//
//            // Validate the exception
//            Assertions.assertEquals(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY, exception.getErrorCode());
//            Mockito.verify(cpSearchCategoryRepository).save(Mockito.any(CpSearchCategory.class));
//        }
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 업데이트 성공 테스트")
//    void updateCpSearchCategory_Success() {
//        // Given
//        long cpSearchCategorySeq = 1L;
//        String newCategoryName = "새로운 식이";
//
//        // Mocking CpSearchCategory 엔티티
//        CpSearchCategory existingCategory = CpSearchCategory.builder()
//                .cpSearchCategoryName("식이")
//                .userSeq(1L)
//                .build();
//
//        // Mocking updated Response DTO
//        ResponseCpSearchCategoryDTO updatedResponseDto = ResponseCpSearchCategoryDTO.builder()
//                .cpSearchCategorySeq(cpSearchCategorySeq)
//                .cpSearchCategoryName(newCategoryName)
//                .createdAt(existingCategory.getCreatedAt())
//                .updatedAt(LocalDateTime.now())
//                .deletedAt(null)
//                .userSeq(existingCategory.getUserSeq())
//                .userName("임광택")
//                .userId("user01")
//                .build();
//
//        Mockito.when(cpSearchCategoryRepository.findById(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.of(existingCategory)); // 기존 카테고리 반환
//        Mockito.when(cpSearchCategoryRepository.save(Mockito.any(CpSearchCategory.class)))
//                .thenReturn(existingCategory); // 저장된 엔티티 반환
//        Mockito.when(cpSearchCategoryRepository.findByCpSearchCategorySeq(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.of(updatedResponseDto)); // 업데이트 후 조회
//
//        // When
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//
//            ResponseCpSearchCategoryDTO result = cpSearchCategoryService.updateCpSearchCategory(cpSearchCategorySeq, newCategoryName);
//
//            // Then
//            Assertions.assertNotNull(result);
//            Assertions.assertEquals(updatedResponseDto.getCpSearchCategoryName(), result.getCpSearchCategoryName());
//            Assertions.assertEquals(updatedResponseDto.getCpSearchCategorySeq(), result.getCpSearchCategorySeq());
//            Mockito.verify(cpSearchCategoryRepository).findById(cpSearchCategorySeq);
//            Mockito.verify(cpSearchCategoryRepository).save(Mockito.any(CpSearchCategory.class));
//            Mockito.verify(cpSearchCategoryRepository).findByCpSearchCategorySeq(cpSearchCategorySeq);
//        }
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 업데이트 시 카테고리 없음 예외 테스트")
//    void updateCpSearchCategory_NotFound() {
//        // Given
//        long cpSearchCategorySeq = 2L; // 존재하지 않는 시퀀스
//        String newCategoryName = "새로운 식이";
//
//        Mockito.when(cpSearchCategoryRepository.findById(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.empty()); // 카테고리 없음
//
//        // When & Then
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//
//            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//                cpSearchCategoryService.updateCpSearchCategory(cpSearchCategorySeq, newCategoryName);
//            });
//
//            // Validate the exception
//            Assertions.assertEquals(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY, exception.getErrorCode());
//            Mockito.verify(cpSearchCategoryRepository).findById(cpSearchCategorySeq); // 메서드 호출 검증
//        }
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 업데이트 시 데이터베이스 접근 오류 테스트")
//    void updateCpSearchCategory_DataAccessException() {
//        // Given
//        long cpSearchCategorySeq = 1L;
//        String newCategoryName = "새로운 식이";
//
//        CpSearchCategory entity = CpSearchCategory.builder().build();
//
//        Mockito.when(cpSearchCategoryRepository.findById(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.of(entity)); // 기존 카테고리 반환
//
//        Mockito.when(cpSearchCategoryRepository.save(Mockito.any(CpSearchCategory.class)))
//                .thenThrow(new DataAccessException("Database error") {
//                });
//
//        // When & Then
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//
//            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//                cpSearchCategoryService.updateCpSearchCategory(cpSearchCategorySeq, newCategoryName);
//            });
//
//            // Validate the exception
//            Assertions.assertEquals(ErrorCode.INTERNAL_DATABASE_ERROR, exception.getErrorCode());
//            Mockito.verify(cpSearchCategoryRepository).findById(cpSearchCategorySeq); // 메서드 호출 검증
//            Mockito.verify(cpSearchCategoryRepository).save(Mockito.any(CpSearchCategory.class)); // 저장 메서드 호출 검증
//        }
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 삭제 성공 테스트")
//    void deleteCpSearchCategory_Success() {
//        // Given
//        long cpSearchCategorySeq = 1L;
//
//        // Mocking 기존 카테고리 엔티티
//        CpSearchCategory existingCategory = CpSearchCategory.builder()
//                .cpSearchCategoryName("식이")
//                .userSeq(1L)
//                .build();
//
//        // Mocking repository behavior
//        Mockito.when(cpSearchCategoryRepository.findById(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.of(existingCategory)); // 기존 카테고리 반환
//        Mockito.when(cpSearchCategoryDataRepository.existsByCpSearchCategorySeq(cpSearchCategorySeq))
//                .thenReturn(false); // 관련 데이터 없음
//
//        // When
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//
//            cpSearchCategoryService.deleteCpSearchCategory(cpSearchCategorySeq);
//
//            // Then
//            Mockito.verify(cpSearchCategoryRepository).findById(cpSearchCategorySeq); // 메서드 호출 검증
//            Mockito.verify(cpSearchCategoryDataRepository).existsByCpSearchCategorySeq(cpSearchCategorySeq); // 관련 데이터 체크
//            Mockito.verify(cpSearchCategoryRepository).save(existingCategory); // 삭제된 카테고리 저장 검증
//        }
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 삭제 시 카테고리 없음 예외 테스트")
//    void deleteCpSearchCategory_NotFound() {
//        // Given
//        long cpSearchCategorySeq = 2L; // 존재하지 않는 시퀀스
//
//        Mockito.when(cpSearchCategoryRepository.findById(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.empty()); // 카테고리 없음
//
//
//        // When & Then
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//
//            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//                cpSearchCategoryService.deleteCpSearchCategory(cpSearchCategorySeq);
//            });
//
//            // Validate the exception
//            Assertions.assertEquals(ErrorCode.NOT_FOUND_CP_SEARCH_CATEGORY, exception.getErrorCode());
//            Mockito.verify(cpSearchCategoryRepository).findById(cpSearchCategorySeq); // 메서드 호출 검증
//        }
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 삭제 시 관련 데이터 존재 예외 테스트")
//    void deleteCpSearchCategory_HasRelatedData() {
//        // Given
//        long cpSearchCategorySeq = 1L;
//
//        // Mocking 기존 카테고리 엔티티
//        CpSearchCategory existingCategory = CpSearchCategory.builder()
//                .cpSearchCategoryName("식이")
//                .userSeq(1L)
//                .build();
//
//        Mockito.when(cpSearchCategoryRepository.findById(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.of(existingCategory)); // 기존 카테고리 반환
//        Mockito.when(cpSearchCategoryDataRepository.existsByCpSearchCategorySeq(cpSearchCategorySeq))
//                .thenReturn(true); // 관련 데이터 있음
//
//        // When & Then
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(CURRENT_USER_SEQ);
//
//            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//                cpSearchCategoryService.deleteCpSearchCategory(cpSearchCategorySeq);
//            });
//
//            // Validate the exception
//            Assertions.assertEquals(ErrorCode.CANNOT_DELETE_DATA, exception.getErrorCode());
//            Mockito.verify(cpSearchCategoryRepository).findById(cpSearchCategorySeq); // 메서드 호출 검증
//            Mockito.verify(cpSearchCategoryDataRepository).existsByCpSearchCategorySeq(cpSearchCategorySeq); // 관련 데이터 체크
//        }
//    }
//
//    @Test
//    @DisplayName("CP 검색 카테고리 삭제 시 데이터베이스 접근 오류 테스트")
//    void deleteCpSearchCategory_DataAccessException() {
//        // Given
//        long cpSearchCategorySeq = 1L;
//
//        // Mocking 기존 카테고리 엔티티
//        CpSearchCategory existingCategory = CpSearchCategory.builder()
//                .cpSearchCategoryName("식이")
//                .userSeq(1L)
//                .build();
//
//        Mockito.when(cpSearchCategoryRepository.findById(cpSearchCategorySeq))
//                .thenReturn(java.util.Optional.of(existingCategory)); // 기존 카테고리 반환
//        Mockito.when(cpSearchCategoryDataRepository.existsByCpSearchCategorySeq(cpSearchCategorySeq))
//                .thenReturn(false); // 관련 데이터 없음
//
//        Mockito.doThrow(new DataAccessException("Database error") {})
//                .when(cpSearchCategoryRepository).save(Mockito.any(CpSearchCategory.class)); // 저장 시 오류 발생
//
//        // When & Then
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(1L); // 현재 사용자 시퀀스 설정
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities)
//                    .thenReturn(CURRENT_USER_AUTH_ADMIN);
//
//            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//                cpSearchCategoryService.deleteCpSearchCategory(cpSearchCategorySeq);
//            });
//
//            // Validate the exception
//            Assertions.assertEquals(ErrorCode.INTERNAL_DATABASE_ERROR, exception.getErrorCode());
//            Mockito.verify(cpSearchCategoryRepository).findById(cpSearchCategorySeq); // 메서드 호출 검증
//            Mockito.verify(cpSearchCategoryDataRepository).existsByCpSearchCategorySeq(cpSearchCategorySeq); // 관련 데이터 체크
//            Mockito.verify(cpSearchCategoryRepository).save(existingCategory); // 저장 메서드 호출 검증
//        }
//    }
//}