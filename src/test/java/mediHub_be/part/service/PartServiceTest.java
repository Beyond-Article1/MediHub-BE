package mediHub_be.part.service;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.dept.entity.Dept;
import mediHub_be.dept.repository.DeptRepository;
import mediHub_be.part.dto.PartDTO;
import mediHub_be.part.dto.PartRequestDTO;
import mediHub_be.part.entity.Part;
import mediHub_be.part.repository.PartRepository;
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

class PartServiceTest {

    @InjectMocks
    private PartService partService;

    @Mock
    private PartRepository partRepository;

    @Mock
    private DeptRepository deptRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 초기화
    }


    @Test
    @DisplayName("모든 과 조회")
    void testGetAllParts() {

        Dept dept = mock(Dept.class);
        when(dept.getDeptSeq()).thenReturn(1L);


        Part part = mock(Part.class);
        when(part.getPartSeq()).thenReturn(1L);
        when(part.getPartName()).thenReturn("Test Part");
        when(part.getDept()).thenReturn(dept);


        when(partRepository.findByDeletedAtIsNull()).thenReturn(List.of(part));

        List<PartDTO> result = partService.getAllParts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Part", result.get(0).getPartName());
        assertEquals(1L, result.get(0).getDeptSeq());

        verify(partRepository, times(1)).findByDeletedAtIsNull();
    }

    @Test
    @DisplayName("과 등록")
    void testCreatePart() {

        PartRequestDTO requestDTO = new PartRequestDTO();
        requestDTO.setDeptSeq(1L);
        requestDTO.setPartName("New Part");

        Dept dept = mock(Dept.class);
        when(dept.getDeptSeq()).thenReturn(1L);

        Part part = mock(Part.class);
        when(part.getDept()).thenReturn(dept);
        when(part.getPartName()).thenReturn("New Part");

        // Mock Repository 설정
        when(deptRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(partRepository.save(any(Part.class))).thenReturn(part);

        PartDTO result = partService.createPart(requestDTO);

        assertNotNull(result);
        assertEquals("New Part", result.getPartName());
        assertEquals(1L, result.getDeptSeq());

        verify(deptRepository, times(1)).findById(1L);
        verify(partRepository, times(1)).save(any(Part.class));
    }

    @Test
    @DisplayName("과 수정")
    void testUpdatePart() {
        // Arrange
        PartDTO partDTO = PartDTO.builder()
                .partSeq(1L)
                .deptSeq(1L)
                .partName("Updated Part")
                .build();

        Dept dept = mock(Dept.class);
        when(dept.getDeptSeq()).thenReturn(1L);

        Part part = mock(Part.class);
        when(part.getPartSeq()).thenReturn(1L);
        when(part.getDept()).thenReturn(dept);
        when(part.getPartName()).thenReturn("Old Part");

        // UpdatePart 호출 시 동작 정의
        doAnswer(invocation -> {
            when(part.getPartName()).thenReturn("Updated Part");
            return null;
        }).when(part).updatePart(dept, "Updated Part");

        when(partRepository.findById(1L)).thenReturn(Optional.of(part));
        when(deptRepository.findById(1L)).thenReturn(Optional.of(dept));

        // Act
        PartDTO result = partService.updatePart(partDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getPartSeq());
        assertEquals(1L, result.getDeptSeq());
        assertEquals("Updated Part", result.getPartName());

        verify(partRepository, times(1)).findById(1L);
        verify(deptRepository, times(1)).findById(1L);
        verify(part, times(1)).updatePart(dept, "Updated Part");
    }

    @Test
    @DisplayName("존재하지 않는 과 수정 시 예외 발생")
    void testUpdatePart_NotFound() {
        PartDTO partDTO = PartDTO.builder()
                .partSeq(99L)
                .deptSeq(1L)
                .partName("Nonexistent Part")
                .build();

        when(partRepository.findById(99L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> partService.updatePart(partDTO));

        assertEquals(ErrorCode.NOT_FOUND_PART, exception.getErrorCode());

        verify(partRepository, times(1)).findById(99L);
        verify(deptRepository, times(0)).findById(anyLong()); // DeptRepository는 호출되지 않아야 함
    }

}
