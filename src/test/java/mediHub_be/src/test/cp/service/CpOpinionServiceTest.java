package mediHub_be.src.test.cp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.board.service.PictureService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.entity.CpOpinion;
import mediHub_be.cp.repository.CpOpinionRepository;
import mediHub_be.cp.repository.CpOpinionVoteRepository;
import mediHub_be.cp.service.CpOpinionService;
import mediHub_be.security.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CpOpinionServiceTest {

    @InjectMocks
    private CpOpinionService cpOpinionService;

    @Mock
    private CpOpinionRepository cpOpinionRepository;

    @Mock
    private CpOpinionVoteRepository cpOpinionVoteRepository;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private PictureService pictureService;

    @Mock
    private KeywordService keywordService;

    @Mock
    private ViewCountManager viewCountManager;

    private static List<ResponseCpOpinionDTO> activeCpOpinionDtoList;
    private static List<ResponseCpOpinionDTO> deletedCpOpinionDtoList;
    private static CpOpinion cpOpinion;

    @BeforeEach
    public void setUpBeforeAll() {
        activeCpOpinionDtoList = new ArrayList<>();
        deletedCpOpinionDtoList = new ArrayList<>(); // 삭제된 데이터 리스트 초기화

        // 활성 상태의 DTO 설정
        ResponseCpOpinionDTO dto1 = ResponseCpOpinionDTO.builder()
                .cpOpinionSeq(1L)
                .cpOpinionContent("내용")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null) // 활성 상태
                .cpOpinionViewCount(100L)
                .userName("임광택")
                .userId("user01")
                .userSeq(1001L)
                .partName("안과")
                .build();

        ResponseCpOpinionDTO dto2 = ResponseCpOpinionDTO.builder()
                .cpOpinionSeq(2L)
                .cpOpinionContent("내용")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null) // 활성 상태
                .cpOpinionViewCount(200L)
                .userName("임채륜")
                .userId("user02")
                .userSeq(1002L)
                .partName("외과")
                .build();

        activeCpOpinionDtoList.add(dto1);
        activeCpOpinionDtoList.add(dto2);

        // 삭제된 상태의 DTO 설정
        ResponseCpOpinionDTO dto3 = ResponseCpOpinionDTO.builder()
                .cpOpinionSeq(3L)
                .cpOpinionContent("삭제된 내용")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusHours(1))
                .deletedAt(LocalDateTime.now()) // 삭제 상태
                .cpOpinionViewCount(50L)
                .userName("김철수")
                .userId("user03")
                .userSeq(1003L)
                .partName("내과")
                .build();

        ResponseCpOpinionDTO dto4 = ResponseCpOpinionDTO.builder()
                .cpOpinionSeq(4L)
                .cpOpinionContent("또 다른 삭제된 내용")
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusHours(2))
                .deletedAt(LocalDateTime.now()) // 삭제 상태
                .cpOpinionViewCount(30L)
                .userName("이영희")
                .userId("user04")
                .userSeq(1004L)
                .partName("외과")
                .build();

        deletedCpOpinionDtoList.add(dto3);
        deletedCpOpinionDtoList.add(dto4);

        // CP 의견 초기화
        cpOpinion = CpOpinion.builder()
                .userSeq(1L)
                .cpOpinionLocationSeq(1L)
                .cpOpinionContent("내용")
                .build();
    }

    @Test
    @DisplayName("CP 의견 위치 번호 -> CP 의견 조회 (활성 상태)")
    void getCpOpinionListByCpVersionSeq_Active() {
        // Given
        long cpVersionSeq = 1L;
        long cpOpinionLocationSeq = 1L;
        boolean isDeleted = false; // 활성 상태의 의견을 조회

        // Mock 설정
        Mockito.when(cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNull(cpOpinionLocationSeq))
                .thenReturn(activeCpOpinionDtoList);

        Mockito.when(keywordService.getKeywordList(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(new ArrayList<>());

        // When
        List<ResponseCpOpinionDTO> result = cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        // DTO의 모든 필드 검증
        ResponseCpOpinionDTO firstDto = result.get(0);
        assertEquals(1L, firstDto.getCpOpinionSeq());
        assertEquals("내용", firstDto.getCpOpinionContent());
        assertNotNull(firstDto.getCreatedAt());
        assertNull(firstDto.getUpdatedAt());
        assertNull(firstDto.getDeletedAt());
        assertEquals(100L, firstDto.getCpOpinionViewCount());
        assertEquals("임광택", firstDto.getUserName());
        assertEquals("user01", firstDto.getUserId());
        assertEquals(1001L, firstDto.getUserSeq());
        assertEquals("안과", firstDto.getPartName());

        ResponseCpOpinionDTO secondDto = result.get(1);
        assertEquals(2L, secondDto.getCpOpinionSeq());
        assertEquals("내용", secondDto.getCpOpinionContent());
        assertNotNull(secondDto.getCreatedAt());
        assertNull(secondDto.getUpdatedAt());
        assertNull(secondDto.getDeletedAt());
        assertEquals(200L, secondDto.getCpOpinionViewCount());
        assertEquals("임채륜", secondDto.getUserName());
        assertEquals("user02", secondDto.getUserId());
        assertEquals(1002L, secondDto.getUserSeq());
        assertEquals("외과", secondDto.getPartName());
    }

    @Test
    @DisplayName("CP 의견 위치 번호 -> CP 의견 조회 (삭제된 상태)")
    void getCpOpinionListByCpVersionSeq_Deleted() {
        // Given
        long cpVersionSeq = 3L;
        long cpOpinionLocationSeq = 1L;
        boolean isDeleted = true; // 삭제된 의견을 조회

        // Mock 설정
        Mockito.when(cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNotNull(cpOpinionLocationSeq))
                .thenReturn(deletedCpOpinionDtoList);

        Mockito.when(keywordService.getKeywordList(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(new ArrayList<>());

        // When
        List<ResponseCpOpinionDTO> result = cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);

        // Then
        Mockito.verify(cpOpinionRepository).findByCpOpinionLocationSeqAndDeletedAtIsNotNull(cpOpinionLocationSeq); // 확인
        assertEquals(2, result.size()); // 두 개의 DTO가 반환되어야 함

        // DTO의 모든 필드 검증
        ResponseCpOpinionDTO firstDto = result.get(0);
        assertEquals(3L, firstDto.getCpOpinionSeq()); // 삭제된 첫 번째 DTO의 seq
        assertEquals("삭제된 내용", firstDto.getCpOpinionContent());
        assertNotNull(firstDto.getCreatedAt());
        assertNotNull(firstDto.getUpdatedAt());
        assertNotNull(firstDto.getDeletedAt()); // 삭제된 상태이므로 null이 아님
        assertEquals(50L, firstDto.getCpOpinionViewCount());
        assertEquals("김철수", firstDto.getUserName());
        assertEquals("user03", firstDto.getUserId());
        assertEquals(1003L, firstDto.getUserSeq());
        assertEquals("내과", firstDto.getPartName());

        ResponseCpOpinionDTO secondDto = result.get(1);
        assertEquals(4L, secondDto.getCpOpinionSeq()); // 삭제된 두 번째 DTO의 seq
        assertEquals("또 다른 삭제된 내용", secondDto.getCpOpinionContent());
        assertNotNull(secondDto.getCreatedAt());
        assertNotNull(secondDto.getUpdatedAt());
        assertNotNull(secondDto.getDeletedAt()); // 삭제된 상태이므로 null이 아님
        assertEquals(30L, secondDto.getCpOpinionViewCount());
        assertEquals("이영희", secondDto.getUserName());
        assertEquals("user04", secondDto.getUserId());
        assertEquals(1004L, secondDto.getUserSeq());
        assertEquals("외과", secondDto.getPartName());
    }

    @Test
    @DisplayName("CP 의견 위치 번호 -> CP 의견 조회 (조회 결과 없음)")
    void getCpOpinionListByCpVersionSeq_NoResults() {
        // Given
        long cpVersionSeq = 1L;
        long cpOpinionLocationSeq = 1L;
        boolean isDeleted = false; // 활성 상태의 의견을 조회

        // Mock 설정: 활성 상태의 의견을 조회할 때 빈 리스트 반환
        Mockito.when(cpOpinionRepository.findByCpOpinionLocationSeqAndDeletedAtIsNull(cpOpinionLocationSeq))
                .thenReturn(new ArrayList<>()); // 빈 리스트 반환

        // When & Then
        assertThrows(CustomException.class, () -> {
            cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);
        });
    }

    @Test
    @DisplayName("존재하는 활성 상태의 CP 의견 조회")
    void findCpOpinionByCpOpinionSeq_Active() {
        // Given
        long cpOpinionSeq = 1L;

        ResponseCpOpinionDTO mockedResponseCpOpinionDto = ResponseCpOpinionDTO.builder()
                .cpOpinionSeq(cpOpinionSeq)
                .cpOpinionContent(cpOpinion.getCpOpinionContent())
                .build();

        // Mock 설정
        Mockito.when(cpOpinionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(cpOpinion));

        Mockito.when(cpOpinionRepository.findByCpOpinionSeq(cpOpinionSeq))
                .thenReturn(Optional.of(mockedResponseCpOpinionDto));

        Mockito.when(keywordService.getKeywordList(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(new ArrayList<>());

        Mockito.when(cpOpinionVoteRepository.findByCpOpinionSeq(Mockito.anyLong()))
                .thenReturn(new ArrayList<>());

        Mockito.when(pictureService.getUserProfileUrl(Mockito.anyLong()))
                .thenReturn(null);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // When
        ResponseCpOpinionDTO result = cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq, request, response);

        // Then
        assertNotNull(result);
        assertEquals(cpOpinion.getCpOpinionContent(), result.getCpOpinionContent());
        Mockito.verify(cpOpinionRepository).findById(cpOpinionSeq);
        Mockito.verify(cpOpinionRepository).findByCpOpinionSeq(cpOpinionSeq);
    }

    @Test
    @DisplayName("존재하는 삭제된 상태의 CP 의견 조회 (관리자)")
    void findCpOpinionByCpOpinionSeq_Deleted_Admin() {
        // Given
        long cpOpinionSeq = 1L;

        // cpOpinion 객체를 삭제된 상태로 설정
        cpOpinion.delete();

        // SecurityUtil Mock 설정
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
                    .thenReturn(1L); // 현재 사용자 시퀀스 설정
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities)
                    .thenReturn("ADMIN"); // 관리자 권한 설정

            // Mock 설정: CP 의견 조회
            Mockito.when(cpOpinionRepository.findById(cpOpinionSeq)).thenReturn(java.util.Optional.of(cpOpinion));
            Mockito.when(cpOpinionRepository.findByCpOpinionSeq(cpOpinionSeq)).thenReturn(java.util.Optional.of(activeCpOpinionDtoList.get(0)));

            // When
            ResponseCpOpinionDTO result = cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq, null, null);

            // Then
            assertNotNull(result);
            assertEquals(cpOpinion.getCpOpinionContent(), result.getCpOpinionContent());
            Mockito.verify(cpOpinionRepository).findById(cpOpinionSeq);
        }
    }

    @Test
    @DisplayName("존재하는 삭제된 상태의 CP 의견 조회 (비관리자)")
    void findCpOpinionByCpOpinionSeq_Deleted_NonAdmin() {
        // Given
        long cpOpinionSeq = 1L;

        // cpOpinion 객체를 삭제된 상태로 설정
        cpOpinion.delete();

        // SecurityUtil Mock 설정
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
                    .thenReturn(1L); // 현재 사용자 시퀀스 설정
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities)
                    .thenReturn("USER"); // 비관리자 권한 설정

            // Mock 설정: CP 의견 조회
            Mockito.when(cpOpinionRepository.findById(cpOpinionSeq)).thenReturn(Optional.of(cpOpinion));

            // When & Then
            CustomException thrown = assertThrows(CustomException.class, () -> {
                cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq, null, null);
            });
            assertEquals(ErrorCode.NOT_FOUND_CP_OPINION, thrown.getErrorCode());
            Mockito.verify(cpOpinionRepository).findById(cpOpinionSeq);
        }
    }

    @Test
    @DisplayName("존재하지 않는 CP 의견 조회")
    void findCpOpinionByCpOpinionSeq_NotFound() {
        // Given
        long cpOpinionSeq = 1L;

        // SecurityUtil Mock 설정
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
                    .thenReturn(1L); // 현재 사용자 시퀀스 설정
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities)
                    .thenReturn("USER"); // 비관리자 권한 설정

            // Mock 설정: CP 의견이 존재하지 않음
            Mockito.when(cpOpinionRepository.findById(cpOpinionSeq)).thenReturn(java.util.Optional.empty());

            // When & Then
            CustomException thrown = assertThrows(CustomException.class, () -> {
                cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq, null, null);
            });
            assertEquals(ErrorCode.NOT_FOUND_CP_OPINION, thrown.getErrorCode());
            Mockito.verify(cpOpinionRepository).findById(cpOpinionSeq);
        }
    }
}

