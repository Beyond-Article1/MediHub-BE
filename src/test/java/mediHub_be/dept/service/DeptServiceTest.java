package mediHub_be.dept.service;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.dept.dto.DeptDTO;
import mediHub_be.dept.entity.Dept;
import mediHub_be.dept.repository.DeptRepository;
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

class DeptServiceTest {

    @InjectMocks
    private DeptService deptService;

    @Mock
    private DeptRepository deptRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("모든 부서 조회")
    void testGetAllDepts() {

        Dept dept = mock(Dept.class);
        when(dept.getDeptSeq()).thenReturn(1L);
        when(dept.getDeptName()).thenReturn("Test Dept");

        when(deptRepository.findAll()).thenReturn(List.of(dept));

        List<DeptDTO> result = deptService.getAllDepts();

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result size should be 1");
        assertEquals("Test Dept", result.get(0).getDeptName(), "Dept name mismatch");
        assertEquals(1L, result.get(0).getDeptSeq(), "DeptSeq mismatch");

        verify(deptRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("부서 생성")
    void testCreateDept() {
        DeptDTO deptDTO = DeptDTO.builder()
                .deptName("New Dept")
                .build();

        Dept dept = mock(Dept.class);
        when(dept.getDeptSeq()).thenReturn(1L);
        when(dept.getDeptName()).thenReturn("New Dept");

        when(deptRepository.save(any(Dept.class))).thenReturn(dept);

        DeptDTO result = deptService.createDept(deptDTO);

        assertNotNull(result, "Result should not be null");
        assertEquals("New Dept", result.getDeptName(), "Dept name mismatch");
        assertEquals(1L, result.getDeptSeq(), "DeptSeq mismatch");

        verify(deptRepository, times(1)).save(any(Dept.class));
    }

    @Test
    @DisplayName("부서 수정")
    void testUpdateDept() {
        DeptDTO deptDTO = DeptDTO.builder()
                .deptName("Updated Dept")
                .build();

        Dept dept = mock(Dept.class);
        when(dept.getDeptSeq()).thenReturn(1L);
        when(dept.getDeptName()).thenReturn("Old Dept");

        doAnswer(invocation -> {
            when(dept.getDeptName()).thenReturn("Updated Dept");
            return null;
        }).when(dept).updateDept("Updated Dept");

        when(deptRepository.findById(1L)).thenReturn(Optional.of(dept));

        DeptDTO result = deptService.updateDept(1L, deptDTO);

        assertNotNull(result, "Result should not be null");
        assertEquals("Updated Dept", result.getDeptName(), "Dept name mismatch");
        assertEquals(1L, result.getDeptSeq(), "DeptSeq mismatch");

        verify(deptRepository, times(1)).findById(1L);
        verify(dept, times(1)).updateDept("Updated Dept");
    }

    @Test
    @DisplayName("존재하지 않는 부서 수정 시 예외 발생")
    void testUpdateDept_NotFound() {
        DeptDTO deptDTO = DeptDTO.builder()
                .deptName("Nonexistent Dept")
                .build();

        when(deptRepository.findById(99L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> deptService.updateDept(99L, deptDTO));

        assertEquals(ErrorCode.NOT_FOUND_PART, exception.getErrorCode());

        verify(deptRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("부서 삭제")
    void testDeleteDept() {
        when(deptRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> deptService.deleteDept(1L));

        verify(deptRepository, times(1)).existsById(1L);
        verify(deptRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 부서 삭제 시 예외 발생")
    void testDeleteDept_NotFound() {
        when(deptRepository.existsById(1L)).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> deptService.deleteDept(1L));
        
        assertEquals(ErrorCode.NOT_FOUND_PART, exception.getErrorCode());

        verify(deptRepository, times(1)).existsById(1L);
        verify(deptRepository, never()).deleteById(anyLong());
    }
}
