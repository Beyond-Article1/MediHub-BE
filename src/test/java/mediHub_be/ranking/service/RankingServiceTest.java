package mediHub_be.ranking.service;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.ranking.dto.RankingDTO;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RankingServiceTest {

    @InjectMocks
    private RankingService rankingService;

    @Mock
    private RankingRepository rankingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("모든 직급 조회")
    void testGetAllRankings() {
        Ranking ranking = mock(Ranking.class);
        when(ranking.getRankingSeq()).thenReturn(1L);
        when(ranking.getDeptSeq()).thenReturn(1L);
        when(ranking.getRankingNum()).thenReturn(1);
        when(ranking.getRankingName()).thenReturn("Manager");

        when(rankingRepository.findAll()).thenReturn(List.of(ranking));

        List<RankingDTO> result = rankingService.getAllRankings();

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result size mismatch");
        assertEquals("Manager", result.get(0).getRankingName(), "Ranking name mismatch");
        assertEquals(1L, result.get(0).getDeptSeq(), "DeptSeq mismatch");
        assertEquals(1L, result.get(0).getRankingSeq(), "RankingSeq mismatch");

        verify(rankingRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("직급 등록")
    void testCreateRanking() {
        RankingDTO rankingDTO = RankingDTO.builder()
                .deptSeq(1L)
                .rankingNum(1)
                .rankingName("Director")
                .build();

        Ranking ranking = mock(Ranking.class);
        when(ranking.getRankingSeq()).thenReturn(1L);
        when(ranking.getDeptSeq()).thenReturn(1L);
        when(ranking.getRankingNum()).thenReturn(1);
        when(ranking.getRankingName()).thenReturn("Director");

        when(rankingRepository.save(any(Ranking.class))).thenReturn(ranking);

        RankingDTO result = rankingService.createRanking(rankingDTO);

        assertNotNull(result, "Result should not be null");
        assertEquals("Director", result.getRankingName(), "Ranking name mismatch");
        assertEquals(1L, result.getDeptSeq(), "DeptSeq mismatch");
        assertEquals(1L, result.getRankingSeq(), "RankingSeq mismatch");

        verify(rankingRepository, times(1)).save(any(Ranking.class));
    }

    @Test
    @DisplayName("직급 수정")
    void testUpdateRanking() {
        RankingDTO rankingDTO = RankingDTO.builder()
                .deptSeq(2L)
                .rankingNum(2)
                .rankingName("Senior Manager")
                .build();

        Ranking ranking = mock(Ranking.class);

        when(ranking.getRankingSeq()).thenReturn(1L);
        when(ranking.getDeptSeq()).thenReturn(1L);
        when(ranking.getRankingNum()).thenReturn(1);
        when(ranking.getRankingName()).thenReturn("Manager");

        doAnswer(invocation -> {
            Long deptSeq = invocation.getArgument(0);
            Integer rankingNum = invocation.getArgument(1);
            String rankingName = invocation.getArgument(2);

            when(ranking.getDeptSeq()).thenReturn(deptSeq);
            when(ranking.getRankingNum()).thenReturn(rankingNum);
            when(ranking.getRankingName()).thenReturn(rankingName);

            return null;
        }).when(ranking).updateRanking(2L, 2, "Senior Manager");

        when(rankingRepository.findById(1L)).thenReturn(Optional.of(ranking));

        RankingDTO result = rankingService.updateRanking(1L, rankingDTO);

        assertNotNull(result, "Result should not be null");
        assertEquals("Senior Manager", result.getRankingName(), "Ranking name mismatch");
        assertEquals(2L, result.getDeptSeq(), "DeptSeq mismatch");
        assertEquals(2, result.getRankingNum(), "Ranking number mismatch");

        verify(rankingRepository, times(1)).findById(1L);
        verify(ranking, times(1)).updateRanking(2L, 2, "Senior Manager");
    }


    @Test
    @DisplayName("존재하지 않는 직급 수정 시 예외 발생")
    void testUpdateRanking_NotFound() {
        RankingDTO rankingDTO = RankingDTO.builder()
                .deptSeq(1L)
                .rankingNum(1)
                .rankingName("Nonexistent")
                .build();

        when(rankingRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> rankingService.updateRanking(1L, rankingDTO));

        assertEquals(ErrorCode.NOT_FOUND_RANKING, exception.getErrorCode());
        verify(rankingRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("직급 삭제")
    void testDeleteRanking() {
        when(rankingRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> rankingService.deleteRanking(1L));

        verify(rankingRepository, times(1)).existsById(1L);
        verify(rankingRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 직급 삭제 시 예외 발생")
    void testDeleteRanking_NotFound() {
        when(rankingRepository.existsById(1L)).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> rankingService.deleteRanking(1L));

        assertEquals(ErrorCode.NOT_FOUND_RANKING, exception.getErrorCode());

        verify(rankingRepository, times(1)).existsById(1L);
        verify(rankingRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("특정 부서의 직급 조회")
    void testGetRankingsByDeptSeq() {
        Ranking ranking = mock(Ranking.class);
        when(ranking.getRankingSeq()).thenReturn(1L);
        when(ranking.getDeptSeq()).thenReturn(1L);
        when(ranking.getRankingNum()).thenReturn(1);
        when(ranking.getRankingName()).thenReturn("Engineer");

        when(rankingRepository.findByDeptSeq(1L)).thenReturn(List.of(ranking));

        List<RankingDTO> result = rankingService.getRankingsByDeptSeq(1L);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result size should be 1");
        assertEquals("Engineer", result.get(0).getRankingName(), "Ranking name mismatch");
        assertEquals(1L, result.get(0).getDeptSeq(), "DeptSeq mismatch");

        verify(rankingRepository, times(1)).findByDeptSeq(1L);
    }

}
