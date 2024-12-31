package mediHub_be.src.test.cp.service;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.CpOpinionVoteDTO;
import mediHub_be.cp.entity.CpOpinionVote;
import mediHub_be.cp.repository.CpOpinionVoteRepository;
import mediHub_be.cp.service.CpOpinionVoteService;
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

@ExtendWith(MockitoExtension.class)
class CpOpinionVoteServiceTest {

    @InjectMocks
    CpOpinionVoteService cpOpinionVoteService;

    @Mock
    CpOpinionVoteRepository repository;

    private static CpOpinionVote mockedCpOpinionVote;

    @BeforeEach
    public void setUp() {
        mockedCpOpinionVote = CpOpinionVote.builder()
                .cpOpinionSeq(1L)
                .userSeq(1L)
                .cpOpinionVote(true)
                .build();
    }

    @Test
    @DisplayName("CP 의견 생성 테스트")
    void createCpOpinionVote() {
        // Given
        long cpOpinionSeq = 1L;
        boolean cpOpinionVote = true;
        long currentUserSeq = 1L;
        String currentUserAuth = UserAuth.ADMIN.name();

        Mockito.when(repository.save(Mockito.any(CpOpinionVote.class)))
                .thenReturn(mockedCpOpinionVote);

        // When
        CpOpinionVoteDTO result;
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq)
                    .thenReturn(currentUserSeq);
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities)
                    .thenReturn(currentUserAuth);

            result = cpOpinionVoteService.createCpOpinionVote(cpOpinionSeq, cpOpinionVote);
        }

        // Then
        Assertions.assertNotNull(result);  // 결과가 null이 아님을 확인
        Assertions.assertEquals(mockedCpOpinionVote.getCpOpinionSeq(), result.getCpOpinionSeq());  // CP 의견 시퀀스 검증
        Assertions.assertEquals(mockedCpOpinionVote.getUserSeq(), result.getUserSeq());  // 사용자 시퀀스 검증
        Assertions.assertEquals(mockedCpOpinionVote.isCpOpinionVote(), result.isCpOpinionVote());  // 투표 여부 검증

        // verify 메서드가 호출되었는지 확인
        Mockito.verify(repository).save(Mockito.any(CpOpinionVote.class));
    }

    @Test
    @DisplayName("CP 의견 투표 삭제 성공 테스트")
    void deleteCpOpinionVote_Success() {
        // Given
        long cpOpinionVoteSeq = 1L;

        // When
        cpOpinionVoteService.deleteCpOpinionVote(cpOpinionVoteSeq);

        // Then
        Mockito.verify(repository).deleteById(cpOpinionVoteSeq);
    }

    @Test
    @DisplayName("CP 의견 투표 삭제 시 데이터베이스 접근 오류 테스트")
    void deleteCpOpinionVote_DataAccessException() {
        // Given
        long cpOpinionVoteSeq = 1L;
        Mockito.doThrow(new DataAccessException("Database error") {
        }).when(repository).deleteById(cpOpinionVoteSeq);

        // When & Then
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            cpOpinionVoteService.deleteCpOpinionVote(cpOpinionVoteSeq);
        });

        // Validate the exception
        Assertions.assertEquals(ErrorCode.INTERNAL_DATA_ACCESS_ERROR, exception.getErrorCode());
        Mockito.verify(repository).deleteById(cpOpinionVoteSeq);
    }

    @Test
    @DisplayName("CP 의견 투표 삭제 시 예기치 않은 오류 테스트")
    void deleteCpOpinionVote_UnexpectedException() {
        // Given
        long cpOpinionVoteSeq = 1L;
        Mockito.doThrow(new RuntimeException("Unexpected error")).when(repository).deleteById(cpOpinionVoteSeq);

        // When & Then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            cpOpinionVoteService.deleteCpOpinionVote(cpOpinionVoteSeq);
        });

        // Validate the exception message
        Assertions.assertEquals("예기치 않은 오류가 발생했습니다. 투표 ID: 1", exception.getMessage());
        Mockito.verify(repository).deleteById(cpOpinionVoteSeq);
    }
}
