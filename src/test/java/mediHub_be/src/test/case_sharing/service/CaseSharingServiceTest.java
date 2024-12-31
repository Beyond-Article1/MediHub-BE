/*
package mediHub_be.src.test.case_sharing.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.PictureService;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.CaseSharingGroup;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.case_sharing.repository.CaseSharingGroupRepository;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.case_sharing.service.CaseSharingService;
import mediHub_be.case_sharing.service.TemplateService;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CaseSharingServiceTest {

    @InjectMocks
    private CaseSharingService caseSharingService;

    @Mock
    private CaseSharingRepository caseSharingRepository;

    @Mock
    private CaseSharingGroupRepository caseSharingGroupRepository;

    @Mock
    private UserService userService;

    @Mock
    private ViewCountManager viewCountManager;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private AmazonS3Service amazonS3Service;

    @Mock
    private FlagService flagService;

    @Mock
    private TemplateService templateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCaseList() {
        when(caseSharingRepository.findAllLatestVersionsNotDraftAndDeletedAtIsNull())
                .thenReturn(Collections.emptyList());

        List<CaseSharingListDTO> result = caseSharingService.getCaseList("testUser");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(caseSharingRepository, times(1)).findAllLatestVersionsNotDraftAndDeletedAtIsNull();
    }

    @Test
    void testGetCaseSharingDetail() {
        CaseSharing mockCaseSharing = mock(CaseSharing.class);
        User mockUser = mock(User.class);
        when(caseSharingRepository.findById(anyLong())).thenReturn(Optional.of(mockCaseSharing));
        when(userService.findByUserId(anyString())).thenReturn(mockUser);
        when(viewCountManager.shouldIncreaseViewCount(anyLong(), any(), any())).thenReturn(true);

        CaseSharingDetailDTO result = caseSharingService.getCaseSharingDetail(1L, "testUser", mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        assertNotNull(result);
        verify(caseSharingRepository, times(1)).findById(anyLong());
        verify(viewCountManager, times(1)).shouldIncreaseViewCount(anyLong(), any(), any());
    }

    @Test
    void testCreateCaseSharing() {
        User mockUser = mock(User.class);
        Template mockTemplate = mock(Template.class);
        CaseSharingGroup mockGroup = mock(CaseSharingGroup.class);
        CaseSharing mockCaseSharing = mock(CaseSharing.class);
        when(userService.findByUserId(anyString())).thenReturn(mockUser);
        when(templateService.getTemplate(anyLong())).thenReturn(mockTemplate);
        when(caseSharingGroupRepository.save(any(CaseSharingGroup.class))).thenReturn(mockGroup);
        when(caseSharingRepository.save(any(CaseSharing.class))).thenReturn(mockCaseSharing);

        Long result = caseSharingService.createCaseSharing(new CaseSharingCreateRequestDTO(), Collections.emptyList(), "testUser");

        assertNotNull(result);
        verify(caseSharingRepository, times(1)).save(any(CaseSharing.class));
    }

    @Test
    void testCreateNewVersion() {
        CaseSharing mockCaseSharing = mock(CaseSharing.class);
        when(caseSharingRepository.findById(anyLong())).thenReturn(Optional.of(mockCaseSharing));

        Long result = caseSharingService.createNewVersion(1L, new CaseSharingUpdateRequestDTO(), Collections.emptyList(), "testUser");

        assertNotNull(result);
        verify(caseSharingRepository, times(2)).save(any(CaseSharing.class));
    }

    @Test
    void testDeleteCaseSharing() {
        CaseSharing mockCaseSharing = mock(CaseSharing.class);
        when(caseSharingRepository.findById(anyLong())).thenReturn(Optional.of(mockCaseSharing));

        caseSharingService.deleteCaseSharing(1L, "testUser");

        verify(caseSharingRepository, times(1)).save(any(CaseSharing.class));
    }

    @Test
    void testGetCasesByPart() {
        when(caseSharingRepository.findByPartPartSeqAndCaseSharingIsLatestTrueAndIsDraftFalseAndDeletedAtIsNull(anyLong()))
                .thenReturn(Collections.emptyList());

        List<CaseSharingListDTO> result = caseSharingService.getCasesByPart(1L, "testUser");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(caseSharingRepository, times(1)).findByPartPartSeqAndCaseSharingIsLatestTrueAndIsDraftFalseAndDeletedAtIsNull(anyLong());
    }

    @Test
    void testGetCaseVersionList() {
        CaseSharing mockCaseSharing = mock(CaseSharing.class);
        when(caseSharingRepository.findById(anyLong())).thenReturn(Optional.of(mockCaseSharing));
        when(caseSharingRepository.findByCaseSharingGroupAndIsDraftFalseAndDeletedAtIsNull(anyLong()))
                .thenReturn(Collections.emptyList());

        List<CaseSharingVersionListDTO> result = caseSharingService.getCaseVersionList(1L, "testUser");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(caseSharingRepository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveDraft() {
        User mockUser = mock(User.class);
        Template mockTemplate = mock(Template.class);
        CaseSharingGroup mockGroup = mock(CaseSharingGroup.class);
        CaseSharing mockDraft = mock(CaseSharing.class);
        when(userService.findByUserId(anyString())).thenReturn(mockUser);
        when(templateService.getTemplate(anyLong())).thenReturn(mockTemplate);
        when(caseSharingGroupRepository.save(any(CaseSharingGroup.class))).thenReturn(mockGroup);
        when(caseSharingRepository.save(any(CaseSharing.class))).thenReturn(mockDraft);

        Long result = caseSharingService.saveDraft(new CaseSharingCreateRequestDTO(), Collections.emptyList(), "testUser");

        assertNotNull(result);
        verify(caseSharingRepository, times(1)).save(any(CaseSharing.class));
    }

    @Test
    void testDeleteDraft() {
        CaseSharing mockDraft = mock(CaseSharing.class);
        when(caseSharingRepository.findById(anyLong())).thenReturn(Optional.of(mockDraft));

        caseSharingService.deleteDraft(1L, "testUser");

        verify(caseSharingRepository, times(1)).delete(any(CaseSharing.class));
    }

    @Test
    void testToggleBookmark() {
        when(bookmarkService.toggleBookmark(anyString(), anyLong(), anyString())).thenReturn(true);

        boolean result = caseSharingService.toggleBookmark(1L, "testUser");

        assertTrue(result);
        verify(bookmarkService, times(1)).toggleBookmark(anyString(), anyLong(), anyString());
    }

    @Test
    void testIsBookmarked() {
        when(bookmarkService.isBookmarked(anyString(), anyLong(), anyString())).thenReturn(true);

        boolean result = caseSharingService.isBookmarked(1L, "testUser");

        assertTrue(result);
        verify(bookmarkService, times(1)).isBookmarked(anyString(), anyLong(), anyString());
    }

    @Test
    void testGetMyCaseList() {
        when(caseSharingRepository.findByUserUserSeqAndCaseSharingIsDraftFalseAndDeletedAtIsNullAndCaseSharingIsLatestIsTrue(anyLong()))
                .thenReturn(Collections.emptyList());

        List<CaseSharingMyListDTO> result = caseSharingService.getMyCaseList("testUser");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(caseSharingRepository, times(1)).findByUserUserSeqAndCaseSharingIsDraftFalseAndDeletedAtIsNullAndCaseSharingIsLatestIsTrue(anyLong());
    }

    @Test
    void testGetBookMarkedCaseList() {
        when(bookmarkService.findByUserAndFlagType(any(), anyString())).thenReturn(Collections.emptyList());

        List<CaseSharingListDTO> result = caseSharingService.getBookMarkedCaseList("testUser");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookmarkService, times(1)).findByUserAndFlagType(any(), anyString());
    }

    @Test
    void testGetTop3Cases() {
        Pageable pageable = PageRequest.of(0, 3);
        when(caseSharingRepository.findTop3ByCreatedAtAfterOrderByCaseSharingViewCountDesc(any(LocalDateTime.class), eq(pageable)))
                .thenReturn(Collections.emptyList());

        List<CaseSharingMain3DTO> result = caseSharingService.getTop3Cases();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(caseSharingRepository, times(1)).findTop3ByCreatedAtAfterOrderByCaseSharingViewCountDesc(any(LocalDateTime.class), eq(pageable));
    }
}*/
