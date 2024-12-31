package mediHub_be.src.test.case_sharing.service;

import mediHub_be.board.entity.Flag;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.PictureService;
import mediHub_be.case_sharing.dto.TemplateDetailDTO;
import mediHub_be.case_sharing.dto.TemplateListDTO;
import mediHub_be.case_sharing.dto.TemplateRequestDTO;
import mediHub_be.case_sharing.entity.OpenScope;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.case_sharing.repository.TemplateRepository;
import mediHub_be.case_sharing.service.TemplateService;
import mediHub_be.part.entity.Part;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TemplateServiceTest {

    @InjectMocks
    private TemplateService templateService;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private UserService userService;

    @Mock
    private PictureService pictureService;

    @Mock
    private FlagService flagService;

    private static final String USER_ID = "testUser";
    private static final Long TEMPLATE_SEQ = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTemplates() {
        // Arrange
        User user = mock(User.class);
        Template template = mock(Template.class);
        Flag flag = mock(Flag.class);

        when(userService.findByUserId(USER_ID)).thenReturn(user);
        when(templateRepository.findByDeletedAtIsNull()).thenReturn(List.of(template));
        when(template.getOpenScope()).thenReturn(OpenScope.PUBLIC);
        when(template.getTemplateSeq()).thenReturn(TEMPLATE_SEQ);
        when(template.getTemplateTitle()).thenReturn("Test Template");
        when(pictureService.getPicturesURLByFlagTypeAndEntitySeqAndIsDeletedIsNotNull(anyString(), eq(TEMPLATE_SEQ)))
                .thenReturn(List.of("http://example.com/image.jpg"));

        // Act
        List<TemplateListDTO> result = templateService.getAllTemplates(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Template", result.get(0).getTemplateTitle());
    }

    @Test
    void testGetTemplateDetail() {
        // Arrange
        Template template = mock(Template.class);
        Part part = mock(Part.class);

        when(templateRepository.findByTemplateSeqAndDeletedAtIsNull(TEMPLATE_SEQ)).thenReturn(Optional.of(template));
        when(template.getTemplateSeq()).thenReturn(TEMPLATE_SEQ);
        when(template.getTemplateTitle()).thenReturn("Test Template");
        when(template.getTemplateContent()).thenReturn("<p>Content</p>");
        when(template.getOpenScope()).thenReturn(OpenScope.PUBLIC);
        when(template.getPart()).thenReturn(part);
        when(part.getPartName()).thenReturn("Test Part");

        // Act
        TemplateDetailDTO result = templateService.getTemplateDetail(TEMPLATE_SEQ);

        // Assert
        assertNotNull(result);
        assertEquals(TEMPLATE_SEQ, result.getTemplateSeq());
        assertEquals("Test Template", result.getTemplateTitle());
        assertEquals("Test Part", result.getPartName());
    }

    @Test
    void testCreateTemplate() throws Exception {
        // Arrange
        User user = mock(User.class);
        when(user.getPart()).thenReturn(mock(Part.class)); // user.getPart()가 null이 되지 않도록 설정

        TemplateRequestDTO requestDTO = new TemplateRequestDTO();
        requestDTO.setTemplateTitle("New Template");
        requestDTO.setTemplateContent("<p>Content</p>");
        requestDTO.setOpenScope("PUBLIC");

        Template template = Template.builder()
                .templateTitle("New Template")
                .templateContent("<p>Content</p>")
                .openScope(OpenScope.PUBLIC)
                .user(user)
                .build();

        MultipartFile previewImage = mock(MultipartFile.class);
        Flag mockFlag = mock(Flag.class);

        when(userService.findByUserId(USER_ID)).thenReturn(user);
        when(templateRepository.save(any(Template.class))).thenReturn(template);
        when(flagService.createFlag(anyString(), anyLong())).thenReturn(mockFlag);

        // PictureService.uploadPicture()에 대한 반환값을 설정
        doAnswer(invocation -> null) // void 메서드의 동작을 설정
                .when(pictureService)
                .uploadPicture(eq(previewImage), eq(mockFlag));

        // Act
        Long result = templateService.createTemplate(USER_ID, List.of(), previewImage, requestDTO);

        // Assert
        assertNotNull(result, "Result should not be null");
        verify(templateRepository, times(1)).save(any(Template.class));
        verify(pictureService, times(1)).uploadPicture(eq(previewImage), eq(mockFlag));
    }




    @Test
    void testUpdateTemplate() {
        // Arrange
        User user = mock(User.class);
        Template template = mock(Template.class);
        TemplateRequestDTO requestDTO = new TemplateRequestDTO();
        requestDTO.setTemplateTitle("Updated Template");
        requestDTO.setTemplateContent("<p>Updated Content</p>");
        requestDTO.setOpenScope("PUBLIC");

        MultipartFile previewImage = mock(MultipartFile.class);

        when(userService.findByUserId(USER_ID)).thenReturn(user);
        when(templateRepository.findByTemplateSeqAndDeletedAtIsNull(TEMPLATE_SEQ)).thenReturn(Optional.of(template));
        doNothing().when(template).updateTemplate(anyString(), anyString(), any(OpenScope.class));

        // Act
        templateService.updateTemplate(USER_ID, TEMPLATE_SEQ, previewImage, List.of(), requestDTO);

        // Assert
        verify(template).updateTemplate(eq("Updated Template"), eq("<p>Updated Content</p>"), eq(OpenScope.PUBLIC));
        verify(templateRepository, times(1)).save(template);
    }

    @Test
    void testDeleteTemplate() {
        // Arrange
        User user = mock(User.class);
        Template template = mock(Template.class);
        Flag templateFlag = mock(Flag.class);
        Flag previewFlag = mock(Flag.class);

        when(userService.findByUserId(USER_ID)).thenReturn(user);
        when(templateRepository.findByTemplateSeqAndDeletedAtIsNull(TEMPLATE_SEQ)).thenReturn(Optional.of(template));
        when(flagService.findFlag(anyString(), anyLong())).thenReturn(Optional.of(templateFlag)).thenReturn(Optional.of(previewFlag));
        doNothing().when(pictureService).deletePictures(any(Flag.class));
        doNothing().when(template).markAsDeleted();

        // Act
        templateService.deleteTemplate(USER_ID, TEMPLATE_SEQ);

        // Assert
        verify(template).markAsDeleted();
        verify(templateRepository, times(1)).save(template);
        verify(pictureService, times(2)).deletePictures(any(Flag.class));
    }
}
