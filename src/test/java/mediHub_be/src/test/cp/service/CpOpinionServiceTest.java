//package mediHub_be.src.test.cp.service;
//
//import mediHub_be.board.service.BookmarkService;
//import mediHub_be.board.service.FlagService;
//import mediHub_be.board.service.KeywordService;
//import mediHub_be.common.exception.CustomException;
//import mediHub_be.common.exception.ErrorCode;
//import mediHub_be.cp.dto.ResponseCpOpinionDTO;
//import mediHub_be.cp.entity.CpOpinion;
//import mediHub_be.cp.repository.CpOpinionRepository;
//import mediHub_be.cp.service.CpOpinionService;
//import mediHub_be.security.util.SecurityUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class CpOpinionServiceTest {
//
//    @Autowired
//    CpOpinionService cpOpinionService;
//
//    @MockBean
//    CpOpinionRepository cpOpinionRepository;
//
//    @MockBean
//    FlagService flagService;
//
//    @MockBean
//    BookmarkService bookmarkService;
//
//    @MockBean
//    KeywordService keywordService;
//
//    private static List<ResponseCpOpinionDTO> responseCpOpinionDtoList;
//    private static CpOpinion cpOpinion;
//
//    @BeforeEach
//    public void setUp() {
//        responseCpOpinionDtoList = new ArrayList<>();
//
//        ResponseCpOpinionDTO dto1 = ResponseCpOpinionDTO.builder()
//                .cpOpinionSeq(1L)
//                .cpOpinionContent("내용")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
//                .deletedAt(null)
//                .cpOpinionViewCount(100L)
//                .userName("임광택")
//                .userId("user01")
//                .userSeq(1001L)
//                .partName("안과")
//                .build();
//
//        ResponseCpOpinionDTO dto2 = ResponseCpOpinionDTO.builder()
//                .cpOpinionSeq(2L)
//                .cpOpinionContent("내용")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(null)
//                .deletedAt(null)
//                .cpOpinionViewCount(200L)
//                .userName("임채륜")
//                .userId("user02")
//                .userSeq(1002L)
//                .partName("외과")
//                .build();
//
//        responseCpOpinionDtoList.add(dto1);
//        responseCpOpinionDtoList.add(dto2);
//
//        // CP 의견 초기화
//        cpOpinion = CpOpinion.builder()
//                .userSeq(1L)
//                .cpOpinionLocationSeq(1L)
//                .cpOpinionContent("내용")
//                .build();
//        // 기타 필드 설정
//    }
//
//    @Test
//    @DisplayName("CP 의견 위치 번호 -> CP 의견 조회 (활성 상태)")
//    void getCpOpinionListByCpVersionSeq_Active() {
//        // Given
//        long cpVersionSeq = 1L;
//        long cpOpinionLocationSeq = 1L;
//        boolean isDeleted = false; // 활성 상태의 의견을 조회
//
//        // Mock 설정: 활성 상태의 의견을 조회할 때 사용되는 메서드
//        Mockito.when(cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNull(cpOpinionLocationSeq))
//                .thenReturn(responseCpOpinionDtoList);
//
//        Mockito.when(bookmarkService.isBookmarked(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
//                .thenReturn(false);
//
//        // When
//        List<ResponseCpOpinionDTO> result = cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);
//
//        // Then
//        Mockito.verify(cpOpinionRepository).findByCpOpinionLocationSeqAndDeletedAtIsNull(cpOpinionLocationSeq); // 확인
//        assertEquals(2, result.size()); // 두 개의 DTO가 반환되어야 함
//
//        // DTO의 모든 필드 검증
//        ResponseCpOpinionDTO firstDto = result.get(0);
//        assertEquals(1L, firstDto.getCpOpinionSeq());
//        assertEquals("내용", firstDto.getCpOpinionContent());
//        assertNotNull(firstDto.getCreatedAt());
//        assertNull(firstDto.getUpdatedAt());
//        assertNull(firstDto.getDeletedAt());
//        assertEquals(100L, firstDto.getCpOpinionViewCount());
//        assertEquals("임광택", firstDto.getUserName());
//        assertEquals("user01", firstDto.getUserId());
//        assertEquals(1001L, firstDto.getUserSeq());
//        assertEquals("안과", firstDto.getPartName());
//
//        ResponseCpOpinionDTO secondDto = result.get(1);
//        assertEquals(2L, secondDto.getCpOpinionSeq());
//        assertEquals("내용", secondDto.getCpOpinionContent());
//        assertNotNull(secondDto.getCreatedAt());
//        assertNull(secondDto.getUpdatedAt());
//        assertNull(secondDto.getDeletedAt());
//        assertEquals(200L, secondDto.getCpOpinionViewCount());
//        assertEquals("임채륜", secondDto.getUserName());
//        assertEquals("user02", secondDto.getUserId());
//        assertEquals(1002L, secondDto.getUserSeq());
//        assertEquals("외과", secondDto.getPartName());
//    }
//
//    @Test
//    @DisplayName("CP 의견 위치 번호 -> CP 의견 조회 (삭제된 상태)")
//    void getCpOpinionListByCpVersionSeq_Deleted() {
//        // Given
//        long cpVersionSeq = 1L;
//        long cpOpinionLocationSeq = 1L;
//        boolean isDeleted = true; // 삭제된 의견을 조회
//
//        // Mock 설정: 삭제된 의견을 조회할 때 사용되는 메서드
//        Mockito.when(cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNotNull(cpOpinionLocationSeq))
//                .thenReturn(responseCpOpinionDtoList);
//
//        Mockito.when(keywordService.getKeywordList(Mockito.anyString(), Mockito.anyLong()))
//                .thenReturn(null);
//
//        // When
//        List<ResponseCpOpinionDTO> result = cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);
//
//        // Then
//        Mockito.verify(cpOpinionRepository).findByCpOpinionLocationSeqAndDeletedAtIsNotNull(cpOpinionLocationSeq); // 확인
//        assertEquals(2, result.size()); // 두 개의 DTO가 반환되어야 함
//
//        // DTO의 모든 필드 검증
//        ResponseCpOpinionDTO firstDto = result.get(0);
//        assertEquals(1L, firstDto.getCpOpinionSeq());
//        assertEquals("내용", firstDto.getCpOpinionContent());
//        assertNotNull(firstDto.getCreatedAt());
//        assertNull(firstDto.getUpdatedAt());
//        assertNull(firstDto.getDeletedAt());
//        assertEquals(100L, firstDto.getCpOpinionViewCount());
//        assertEquals("임광택", firstDto.getUserName());
//        assertEquals("user01", firstDto.getUserId());
//        assertEquals(1001L, firstDto.getUserSeq());
//        assertEquals("안과", firstDto.getPartName());
//
//        ResponseCpOpinionDTO secondDto = result.get(1);
//        assertEquals(2L, secondDto.getCpOpinionSeq());
//        assertEquals("내용", secondDto.getCpOpinionContent());
//        assertNotNull(secondDto.getCreatedAt());
//        assertNull(secondDto.getUpdatedAt());
//        assertNull(secondDto.getDeletedAt());
//        assertEquals(200L, secondDto.getCpOpinionViewCount());
//        assertEquals("임채륜", secondDto.getUserName());
//        assertEquals("user02", secondDto.getUserId());
//        assertEquals(1002L, secondDto.getUserSeq());
//        assertEquals("외과", secondDto.getPartName());
//    }
//
//    @Test
//    @DisplayName("CP 의견 위치 번호 -> CP 의견 조회 (조회 결과 없음)")
//    void getCpOpinionListByCpVersionSeq_NoResults() {
//        // Given
//        long cpVersionSeq = 1L;
//        long cpOpinionLocationSeq = 1L;
//        boolean isDeleted = false; // 활성 상태의 의견을 조회
//
//        // Mock 설정: 활성 상태의 의견이 없을 경우
//        Mockito.when(cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNull(cpOpinionLocationSeq))
//                .thenReturn(new ArrayList<>()); // 빈 리스트 반환
//
//        Mockito.when(cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNotNull(cpOpinionLocationSeq))
//                .thenReturn(responseCpOpinionDtoList);
//
//        // When & Then
//        assertThrows(CustomException.class, () -> {
//            cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);
//        });
//    }
//
//    @Test
//    @DisplayName("존재하는 활성 상태의 CP 의견 조회")
//    void findCpOpinionByCpOpinionSeq_Active() {
//        // Given
//        long cpOpinionSeq = 1L;
//        Mockito.when(cpOpinionRepository.findById(cpOpinionSeq))
//                .thenReturn(java.util.Optional.of(cpOpinion));
//        Mockito.when(cpOpinionRepository.findByCpOpinionSeq(cpOpinionSeq))
//                .thenReturn(java.util.Optional.of(responseCpOpinionDtoList.get(0)));
//
//        // When
//        ResponseCpOpinionDTO result = cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(cpOpinion.getCpOpinionContent(), result.getCpOpinionContent());
//        Mockito.verify(cpOpinionRepository).findById(cpOpinionSeq);
//    }
//
//    @Test
//    @DisplayName("존재하는 삭제된 상태의 CP 의견 조회 (관리자)")
//    void findCpOpinionByCpOpinionSeq_Deleted_Admin() {
//        // Given
//        long cpOpinionSeq = 1L;
//
//        // cpOpinion 객체를 삭제된 상태로 설정
//        cpOpinion.delete();
//
//        // SecurityUtil Mock 설정
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(1L); // 현재 사용자 시퀀스 설정
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities)
//                    .thenReturn("ADMIN"); // 관리자 권한 설정
//
//            // Mock 설정: CP 의견 조회
//            Mockito.when(cpOpinionRepository.findById(cpOpinionSeq)).thenReturn(java.util.Optional.of(cpOpinion));
//            Mockito.when(cpOpinionRepository.findByCpOpinionSeq(cpOpinionSeq)).thenReturn(java.util.Optional.of(responseCpOpinionDtoList.get(0)));
//
//            // When
//            ResponseCpOpinionDTO result = cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq);
//
//            // Then
//            assertNotNull(result);
//            assertEquals(cpOpinion.getCpOpinionContent(), result.getCpOpinionContent());
//            Mockito.verify(cpOpinionRepository).findById(cpOpinionSeq);
//        }
//    }
//
//    @Test
//    @DisplayName("존재하는 삭제된 상태의 CP 의견 조회 (비관리자)")
//    void findCpOpinionByCpOpinionSeq_Deleted_NonAdmin() {
//        // Given
//        long cpOpinionSeq = 1L;
//
//        // cpOpinion 객체를 삭제된 상태로 설정
//        cpOpinion.delete();
//
//        // SecurityUtil Mock 설정
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(1L); // 현재 사용자 시퀀스 설정
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities)
//                    .thenReturn("USER"); // 비관리자 권한 설정
//
//            // Mock 설정: CP 의견 조회
//            Mockito.when(cpOpinionRepository.findById(cpOpinionSeq)).thenReturn(java.util.Optional.of(cpOpinion));
//
//            // When & Then
//            CustomException thrown = assertThrows(CustomException.class, () -> {
//                cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq);
//            });
//            assertEquals(ErrorCode.NOT_FOUND_CP_OPINION, thrown.getErrorCode());
//            Mockito.verify(cpOpinionRepository).findById(cpOpinionSeq);
//        }
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 CP 의견 조회")
//    void findCpOpinionByCpOpinionSeq_NotFound() {
//        // Given
//        long cpOpinionSeq = 1L;
//
//        // SecurityUtil Mock 설정
//        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
//                    .thenReturn(1L); // 현재 사용자 시퀀스 설정
//            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities)
//                    .thenReturn("USER"); // 비관리자 권한 설정
//
//            // Mock 설정: CP 의견이 존재하지 않음
//            Mockito.when(cpOpinionRepository.findById(cpOpinionSeq)).thenReturn(java.util.Optional.empty());
//
//            // When & Then
//            CustomException thrown = assertThrows(CustomException.class, () -> {
//                cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq);
//            });
//            assertEquals(ErrorCode.NOT_FOUND_CP_OPINION, thrown.getErrorCode());
//            Mockito.verify(cpOpinionRepository).findById(cpOpinionSeq);
//        }
//    }
//}
//
