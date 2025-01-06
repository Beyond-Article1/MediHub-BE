package mediHub_be.src.test.case_sharing.service;

import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.board.service.PictureService;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.CaseSharingGroup;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.case_sharing.repository.CaseSharingGroupRepository;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.case_sharing.service.CaseSharingService;
import mediHub_be.case_sharing.service.TemplateService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.notify.service.NotifyServiceImlp;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CaseSharingServiceTest {

    @InjectMocks
    private CaseSharingService caseSharingService;

    @Mock
    private CaseSharingRepository caseSharingRepository;

    @Mock
    private UserService userService;

    @Mock
    private PictureService pictureService;

    @Mock
    private KeywordService keywordService;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private AmazonS3Service amazonS3Service;

    @Mock
    private FlagService flagService;

    @Mock
    private NotifyServiceImlp notifyServiceImlp;

    @Mock
    private TemplateService templateService;

    @Mock
    private ViewCountManager viewCountManager;

    @Mock
    private CaseSharingGroupRepository caseSharingGroupRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 기본 Mock 설정
        when(viewCountManager.shouldIncreaseViewCount(anyLong(), any(), any())).thenReturn(true);
        when(flagService.createFlag(any(), any())).thenReturn(mock(Flag.class));
        when(flagService.findFlag(any(), anyLong())).thenReturn(Optional.of(mock(Flag.class)));
        when(caseSharingGroupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }


    @Test
    void testGetCaseList() {
        // Arrange
        String userId = "user1";
        User user = mock(User.class);
        Ranking ranking = mock(Ranking.class);
        CaseSharing caseSharing = mock(CaseSharing.class);

        when(userService.findByUserId(userId)).thenReturn(user);
        when(caseSharingRepository.findAllLatestVersionsNotDraftAndDeletedAtIsNull())
                .thenReturn(List.of(caseSharing));

        when(caseSharing.getCaseSharingSeq()).thenReturn(1L);
        when(caseSharing.getCaseSharingTitle()).thenReturn("Test Case");
        when(caseSharing.getUser()).thenReturn(user);
        when(caseSharing.getCreatedAt()).thenReturn(null);
        when(caseSharing.getCaseSharingViewCount()).thenReturn(10L);

        when(user.getUserName()).thenReturn("Test Author");
        when(user.getRanking()).thenReturn(ranking);
        when(ranking.getRankingName()).thenReturn("Test Rank");

        // Act
        List<CaseSharingListDTO> result = caseSharingService.getCaseList(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Case", result.get(0).getCaseSharingTitle());
        assertEquals("Test Author", result.get(0).getCaseAuthor());
        assertEquals("Test Rank", result.get(0).getCaseAuthorRankName());
    }

    @Test
    void testGetCaseSharingDetail() {
        // Arrange
        Long caseSharingSeq = 1L;
        String userId = "user1";
        User user = mock(User.class);
        Ranking ranking = mock(Ranking.class);
        CaseSharing caseSharing = mock(CaseSharing.class);
        CaseSharingGroup caseSharingGroup = mock(CaseSharingGroup.class);
        Template template = mock(Template.class);

        // Mock 설정
        when(userService.findByUserId(userId)).thenReturn(user);
        when(caseSharingRepository.findById(caseSharingSeq)).thenReturn(Optional.of(caseSharing));
        when(caseSharing.getDeletedAt()).thenReturn(null);
        when(caseSharing.getUser()).thenReturn(user);
        when(caseSharing.getCaseSharingGroup()).thenReturn(caseSharingGroup);
        when(caseSharing.getTemplate()).thenReturn(template);
        when(caseSharingGroup.getCaseSharingGroupSeq()).thenReturn(10L);
        when(user.getRanking()).thenReturn(ranking);
        when(ranking.getRankingName()).thenReturn("Test Rank");
        when(template.getTemplateSeq()).thenReturn(100L);

        // Act
        CaseSharingDetailDTO result = caseSharingService.getCaseSharingDetail(caseSharingSeq, userId, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getCaseSharingGroupSeq());
        assertEquals("Test Rank", result.getCaseAuthorRankName());
        assertEquals(100L, result.getTemplateSeq());
    }

    @Test
    void testDeleteCaseSharing() {
        // Arrange
        Long caseSharingSeq = 1L;
        String userId = "user1";
        User user = mock(User.class);
        CaseSharing caseSharing = mock(CaseSharing.class);
        Flag flag = mock(Flag.class);

        when(userService.findByUserId(userId)).thenReturn(user);
        when(caseSharingRepository.findById(caseSharingSeq)).thenReturn(Optional.of(caseSharing));
        when(flagService.findFlag(any(), anyLong())).thenReturn(Optional.of(flag));

        // Act
        caseSharingService.deleteCaseSharing(caseSharingSeq, userId);

        // Assert
        verify(caseSharingRepository, times(1)).save(caseSharing);
    }


    @Test
    void testToggleBookmark() {
        // Arrange
        Long caseSharingSeq = 1L;
        String userId = "user1";

        when(bookmarkService.toggleBookmark(any(), eq(caseSharingSeq), eq(userId))).thenReturn(true);

        // Act
        boolean result = caseSharingService.toggleBookmark(caseSharingSeq, userId);

        // Assert
        assertTrue(result);
        verify(bookmarkService, times(1)).toggleBookmark(any(), eq(caseSharingSeq), eq(userId));
    }

    @Test
    void testGetCasesByPart() {
        // Arrange
        Long partSeq = 1L;
        String userId = "user1";
        User user = mock(User.class);
        CaseSharing caseSharing = mock(CaseSharing.class);

        Ranking ranking = mock(Ranking.class);
        when(userService.findByUserId(userId)).thenReturn(user);
        when(caseSharingRepository.findByPartPartSeqAndCaseSharingIsLatestTrueAndIsDraftFalseAndDeletedAtIsNull(partSeq))
                .thenReturn(List.of(caseSharing));

        when(caseSharing.getCaseSharingSeq()).thenReturn(1L);
        when(caseSharing.getCaseSharingTitle()).thenReturn("Test Title");
        when(caseSharing.getUser()).thenReturn(user);
        when(caseSharing.getCreatedAt()).thenReturn(null);
        when(caseSharing.getCaseSharingViewCount()).thenReturn(10L);
        when(user.getUserName()).thenReturn("Author Name");
        when(user.getRanking()).thenReturn(ranking);
        when(ranking.getRankingName()).thenReturn("Test Rank");

        // Act
        List<CaseSharingListDTO> result = caseSharingService.getCasesByPart(partSeq, userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getCaseSharingTitle());
        assertEquals("Author Name", result.get(0).getCaseAuthor());
    }

    @Test
    void testGetCaseVersionList() {
        // Arrange
        Long caseSharingSeq = 1L;
        String userId = "user1";
        User user = mock(User.class);
        CaseSharing caseSharing = mock(CaseSharing.class);
        CaseSharingGroup group = mock(CaseSharingGroup.class);

        when(userService.findByUserId(userId)).thenReturn(user);
        when(caseSharingRepository.findById(caseSharingSeq)).thenReturn(Optional.of(caseSharing));
        when(caseSharing.getCaseSharingGroup()).thenReturn(group);
        when(group.getCaseSharingGroupSeq()).thenReturn(2L);
        when(caseSharingRepository.findByCaseSharingGroupAndIsDraftFalseAndDeletedAtIsNull(2L))
                .thenReturn(List.of(caseSharing));

        when(caseSharing.getCaseSharingSeq()).thenReturn(1L);
        when(caseSharing.getCaseSharingTitle()).thenReturn("Version 1");
        when(caseSharing.getCreatedAt()).thenReturn(null);
        when(caseSharing.getCaseSharingViewCount()).thenReturn(5L);

        // Act
        List<CaseSharingVersionListDTO> result = caseSharingService.getCaseVersionList(caseSharingSeq, userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Version 1", result.get(0).getCaseSharingTitle());
    }

    @Test
    void testGetDraftsByUser() {
        // Arrange
        String userId = "user1";
        User user = mock(User.class);
        CaseSharing draft = mock(CaseSharing.class);

        when(userService.findByUserId(userId)).thenReturn(user);
        when(user.getUserSeq()).thenReturn(1L);
        when(caseSharingRepository.findByUserUserSeqAndCaseSharingIsDraftTrueAndDeletedAtIsNull(1L))
                .thenReturn(List.of(draft));

        when(draft.getCaseSharingSeq()).thenReturn(1L);
        when(draft.getCaseSharingTitle()).thenReturn("Draft Title");
        when(draft.getCreatedAt()).thenReturn(null);

        // Act
        List<CaseSharingDraftListDTO> result = caseSharingService.getDraftsByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Draft Title", result.get(0).getCaseSharingTitle());
    }

    @Test
    void testGetDraftDetail() {
        // Arrange
        Long caseSharingSeq = 1L;
        String userId = "user1";
        User user = mock(User.class);
        CaseSharing draft = mock(CaseSharing.class);
        CaseSharingGroup group = mock(CaseSharingGroup.class);

        when(userService.findByUserId(userId)).thenReturn(user);
        when(caseSharingRepository.findByCaseSharingSeqAndCaseSharingIsDraftTrueAndDeletedAtIsNull(caseSharingSeq))
                .thenReturn(Optional.of(draft));
        when(draft.getUser()).thenReturn(user);
        when(draft.getCaseSharingSeq()).thenReturn(caseSharingSeq);
        when(draft.getCaseSharingTitle()).thenReturn("Draft Detail Title");
        when(draft.getCaseSharingContent()).thenReturn("Draft Content");
        when(draft.getCaseSharingGroup()).thenReturn(group);
        when(group.getCaseSharingGroupSeq()).thenReturn(10L);

        when(keywordService.getKeywords(any(), eq(caseSharingSeq)))
                .thenReturn(List.of(new CaseSharingKeywordDTO(1L, "Keyword1")));

        // Act
        CaseSharingDraftDetailDTO result = caseSharingService.getDraftDetail(caseSharingSeq, userId);

        // Assert
        assertNotNull(result);
        assertEquals("Draft Detail Title", result.getCaseSharingTitle());
        assertEquals("Draft Content", result.getCaseSharingContent());
        assertEquals(10L, result.getCaseSharingGroupSeq());
    }

    @Test
    void testDeleteDraft() {
        // Arrange
        Long caseSharingSeq = 1L;
        String userId = "user1";
        User user = mock(User.class);
        CaseSharing draft = mock(CaseSharing.class);
        Flag flag = mock(Flag.class);
        Picture picture = mock(Picture.class);

        when(userService.findByUserId(userId)).thenReturn(user);
        when(caseSharingRepository.findById(caseSharingSeq)).thenReturn(Optional.of(draft));
        when(draft.getUser()).thenReturn(user);
        when(draft.getCaseSharingSeq()).thenReturn(caseSharingSeq);
        when(pictureService.getPicturesByFlagTypeAndEntitySeqAndIsDeletedIsNotNull(any(), eq(caseSharingSeq)))
                .thenReturn(List.of(picture));
        when(flagService.findFlag(any(), eq(caseSharingSeq))).thenReturn(Optional.of(flag));
        when(picture.getPictureUrl()).thenReturn("http://example.com/image.jpg");

        // Act
        caseSharingService.deleteDraft(caseSharingSeq, userId);

        // Assert
        verify(amazonS3Service, times(1)).deleteImageFromS3("http://example.com/image.jpg");
        verify(caseSharingRepository, times(1)).delete(draft);
    }

    @Test
    void testIsBookmarked() {
        // Arrange
        Long caseSharingSeq = 1L;
        String userId = "user1";

        when(userService.findByUserId(userId)).thenReturn(mock(User.class));
        when(bookmarkService.isBookmarked(any(), eq(caseSharingSeq), eq(userId))).thenReturn(true);

        // Act
        boolean result = caseSharingService.isBookmarked(caseSharingSeq, userId);

        // Assert
        assertTrue(result);
        verify(bookmarkService, times(1)).isBookmarked(any(), eq(caseSharingSeq), eq(userId));
    }

    @Test
    void testGetMyCaseList() {
        // Arrange
        String userId = "user1";
        User user = mock(User.class);
        CaseSharing caseSharing = mock(CaseSharing.class);

        when(userService.findByUserId(userId)).thenReturn(user);
        when(user.getUserSeq()).thenReturn(1L);
        when(caseSharingRepository.findByUserUserSeqAndCaseSharingIsDraftFalseAndDeletedAtIsNullAndCaseSharingIsLatestIsTrue(1L))
                .thenReturn(List.of(caseSharing));

        when(caseSharing.getCaseSharingSeq()).thenReturn(1L);
        when(caseSharing.getCaseSharingTitle()).thenReturn("My Case Title");
        when(caseSharing.getCreatedAt()).thenReturn(null);
        when(caseSharing.getCaseSharingViewCount()).thenReturn(10L);

        // Act
        List<CaseSharingMyListDTO> result = caseSharingService.getMyCaseList(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("My Case Title", result.get(0).getCaseSharingTitle());
    }

    @Test
    void testGetBookMarkedCaseList() {
        // Arrange
        String userId = "user1";
        User user = mock(User.class);
        CaseSharing caseSharing = mock(CaseSharing.class);
        Flag flag = mock(Flag.class);
        BookmarkDTO bookmarkDTO = mock(BookmarkDTO.class);
        Ranking ranking = mock(Ranking.class);

        when(userService.findByUserId(userId)).thenReturn(user);
        when(bookmarkService.findByUserAndFlagType(eq(user), any())).thenReturn(List.of(bookmarkDTO));
        when(bookmarkDTO.getFlag()).thenReturn(flag);
        when(flag.getFlagEntitySeq()).thenReturn(1L);
        when(caseSharingRepository.findAllById(List.of(1L))).thenReturn(List.of(caseSharing));

        when(caseSharing.getCaseSharingSeq()).thenReturn(1L);
        when(caseSharing.getCaseSharingTitle()).thenReturn("Bookmarked Case");
        when(caseSharing.getUser()).thenReturn(user);
        when(user.getUserName()).thenReturn("Test Author");
        when(user.getRanking()).thenReturn(ranking);
        when(ranking.getRankingName()).thenReturn("Test Rank");

        // Act
        List<CaseSharingListDTO> result = caseSharingService.getBookMarkedCaseList(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bookmarked Case", result.get(0).getCaseSharingTitle());
        assertEquals("Test Author", result.get(0).getCaseAuthor());
        assertEquals("Test Rank", result.get(0).getCaseAuthorRankName());
    }



    @Test
    void testGetTop3Cases() {
        // Arrange
        CaseSharing caseSharing = mock(CaseSharing.class);
        when(caseSharingRepository.findTop3ByCreatedAtAfterOrderByCaseSharingViewCountDesc(any(), any()))
                .thenReturn(List.of(caseSharing));

        when(caseSharing.getCaseSharingSeq()).thenReturn(1L);
        when(caseSharing.getCaseSharingTitle()).thenReturn("Top Case");
        when(caseSharing.getUser()).thenReturn(mock(User.class));
        when(caseSharing.getPart()).thenReturn(mock(Part.class));
        when(caseSharing.getUser().getUserName()).thenReturn("Author");
        when(caseSharing.getPart().getPartName()).thenReturn("Part Name");
        when(caseSharing.getUser().getRanking()).thenReturn(mock(Ranking.class));
        when(caseSharing.getUser().getRanking().getRankingName()).thenReturn("Rank");
        when(pictureService.getCaseSharingFirstImageUrl(1L)).thenReturn("http://example.com/image.jpg");

        // Act
        List<CaseSharingMain3DTO> result = caseSharingService.getTop3Cases();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Top Case", result.get(0).getCaseSharingTitle());
        assertEquals("http://example.com/image.jpg", result.get(0).getFirstPictureUrl());
    }
}
