package mediHub_be.medicalLife.service;

import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Comment;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.CommentRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.KeywordService;
import mediHub_be.board.service.PictureService;
import mediHub_be.dept.entity.Dept;
import mediHub_be.medicalLife.dto.*;
import mediHub_be.medicalLife.entity.MedicalLife;
import mediHub_be.medicalLife.repository.MedicalLifeRepository;
import mediHub_be.notify.service.NotifyServiceImlp;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import mediHub_be.user.service.UserService;
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

class MedicalLifeServiceTest {

    @InjectMocks
    private MedicalLifeService medicalLifeService;

    @Mock
    private MedicalLifeRepository medicalLifeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlagRepository flagRepository;

    @Mock
    private KeywordRepository keywordRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ViewCountManager viewCountManager;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private PictureService pictureService;

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private NotifyServiceImlp notifyServiceImlp;

    @Mock
    private FlagService flagService;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private UserService userService;

    @Mock
    private KeywordService keywordService;

    @Mock
    private Dept dept;

    private User user;
    private MedicalLife medicalLife;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Part
        Part part = mock(Part.class);
        when(part.getPartName()).thenReturn("Test Part");
        when(part.getDept()).thenReturn(dept);

        // Mock Ranking
        Ranking ranking = mock(Ranking.class);
        when(ranking.getRankingName()).thenReturn("Test Ranking");

        // Mock Dept
        Dept dept = mock(Dept.class);
        when(dept.getDeptName()).thenReturn("Test Dept");

        // Mock User
        user = mock(User.class);
        when(user.getUserSeq()).thenReturn(1L);
        when(user.getUserName()).thenReturn("Test User");
        when(user.getPart()).thenReturn(part);
        when(user.getRanking()).thenReturn(ranking);

        // Mock MedicalLife
        medicalLife = mock(MedicalLife.class);
        when(medicalLife.getMedicalLifeSeq()).thenReturn(1L);
        when(medicalLife.getMedicalLifeTitle()).thenReturn("Test Title");
        when(medicalLife.getMedicalLifeContent()).thenReturn("Test Content");
        when(medicalLife.getUser()).thenReturn(user);
        when(medicalLife.getMedicalLifeViewCount()).thenReturn(100L);

    }

    @DisplayName("상세 조회 성공")
    @Test
    void testGetMedicalLifeDetail_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(medicalLifeRepository.findByMedicalLifeSeqAndMedicalLifeIsDeletedFalse(1L)).thenReturn(medicalLife);
        when(viewCountManager.shouldIncreaseViewCount(eq(1L), any(), any())).thenReturn(true);

        // when
        MedicalLifeDetailDTO result = medicalLifeService.getMedicalLifeDetail(1L, 1L, null, null);

        // then
        assertNotNull(result);
        assertEquals("Test Title", result.getMedicalLifeTitle());
        verify(viewCountManager, times(1)).shouldIncreaseViewCount(eq(1L), any(), any());
        verify(medicalLifeRepository, times(1)).save(medicalLife);
    }

    @DisplayName("댓글 조회")
    @Test
    void testGetMedicalLifeCommentList_Success() {
        // given
        Long medicalLifeSeq = 1L;
        Long userSeq = 1L;

        Flag flag = mock(Flag.class);
        Comment comment = mock(Comment.class);
        User commentUser = mock(User.class);

        when(userRepository.findById(userSeq)).thenReturn(Optional.of(user));
        when(flagService.findFlag("MEDICAL_LIFE", medicalLifeSeq)).thenReturn(Optional.of(flag));
        when(flag.getFlagSeq()).thenReturn(1L);
        when(commentRepository.findByFlag_FlagSeqAndCommentIsDeletedFalse(1L)).thenReturn(List.of(comment));
        when(comment.getUser()).thenReturn(commentUser);
        when(comment.getCommentContent()).thenReturn("Test Comment");
        when(comment.getCommentSeq()).thenReturn(1L);
        when(comment.getCreatedAt()).thenReturn(null);
        when(commentUser.getUserSeq()).thenReturn(2L);
        when(commentUser.getUserName()).thenReturn("Comment User");
        when(commentUser.getPart()).thenReturn(null);
        when(commentUser.getRanking()).thenReturn(null);

        // when
        List<MedicalLifeCommentListDTO> result = medicalLifeService.getMedicalLifeCommentList(medicalLifeSeq, userSeq);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Comment", result.get(0).getCommentContent());
        verify(userRepository, times(1)).findById(userSeq);
        verify(commentRepository, times(1)).findByFlag_FlagSeqAndCommentIsDeletedFalse(1L);
    }

    @DisplayName("댓글 삭제")
    @Test
    void testDeleteMedicalLifeComment_Success() {
        // given
        Long medicalLifeSeq = 1L;
        Long commentSeq = 1L;
        Long userSeq = 1L;

        Comment comment = mock(Comment.class);

        when(userRepository.findById(userSeq)).thenReturn(Optional.of(user));
        when(commentRepository.findByCommentSeqAndCommentIsDeletedFalse(commentSeq)).thenReturn(comment);
        when(comment.getUser()).thenReturn(user);
        when(comment.getDeletedAt()).thenReturn(null);

        // when
        boolean result = medicalLifeService.deleteMedicalLifeComment(medicalLifeSeq, commentSeq, userSeq);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findById(userSeq);
        verify(commentRepository, times(1)).findByCommentSeqAndCommentIsDeletedFalse(commentSeq);
        verify(comment, times(1)).setDeleted();
        verify(commentRepository, times(1)).save(comment);
    }

    @DisplayName("북마크")
    @Test
    void testIsBookmarked_Success() {
        // given
        Long medicalLifeSeq = 1L;
        String userId = "testUser";

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(bookmarkService.isBookmarked("MEDICAL_LIFE", medicalLifeSeq, userId)).thenReturn(true);

        // when
        boolean result = medicalLifeService.isBookmarked(medicalLifeSeq, userId);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByUserId(userId);
        verify(bookmarkService, times(1)).isBookmarked("MEDICAL_LIFE", medicalLifeSeq, userId);
    }

    @DisplayName("북마크 등록")
    @Test
    void testToggleBookmark_Success() {
        // given
        Long medicalLifeSeq = 1L;
        String userId = "testUser";

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(bookmarkService.toggleBookmark("MEDICAL_LIFE", medicalLifeSeq, userId)).thenReturn(true);

        // when
        boolean result = medicalLifeService.toggleBookmark(medicalLifeSeq, userId);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByUserId(userId);
        verify(bookmarkService, times(1)).toggleBookmark("MEDICAL_LIFE", medicalLifeSeq, userId);
    }

    @DisplayName("상위 3개 조회")
    @Test
    void testGetTop3MedicalLifeByViewCount_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(medicalLifeRepository.findTop3ByMedicalLifeIsDeletedFalseOrderByMedicalLifeViewCountDesc())
                .thenReturn(List.of(medicalLife));

        // when
        List<MedicalLifeTop3DTO> result = medicalLifeService.getTop3MedicalLifeByViewCount(1L);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getMedicalLifeTitle());
        assertEquals("Test Part", result.get(0).getPartName()); // PartName 검증
        verify(medicalLifeRepository, times(1))
                .findTop3ByMedicalLifeIsDeletedFalseOrderByMedicalLifeViewCountDesc();
    }

    @DisplayName("북마크 한 메디컬 라이프 조회")
    @Test
    void testGetBookMarkedMedicalLifeList_Success() {

        // given
        Long userSeq = 1L;

        Flag flag = mock(Flag.class);
        BookmarkDTO bookmarkDTO = mock(BookmarkDTO.class);

        when(userRepository.findById(userSeq)).thenReturn(Optional.of(user));
        when(bookmarkService.findByUserAndFlagType(user, "MEDICAL_LIFE")).thenReturn(List.of(bookmarkDTO));
        when(bookmarkDTO.getFlag()).thenReturn(flag);
        when(flag.getFlagEntitySeq()).thenReturn(1L);
        when(medicalLifeRepository.findAllById(List.of(1L))).thenReturn(List.of(medicalLife));
        when(medicalLife.getMedicalLifeSeq()).thenReturn(1L);
        when(medicalLife.getUser()).thenReturn(user);

        // when
        List<MedicalLifeBookMarkDTO> result = medicalLifeService.getBookMarkedMedicalLifeList(userSeq);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getMedicalLifeTitle());
        verify(userRepository, times(1)).findById(userSeq);
        verify(bookmarkService, times(1)).findByUserAndFlagType(user, "MEDICAL_LIFE");
        verify(medicalLifeRepository, times(1)).findAllById(List.of(1L));
    }

    @DisplayName("메디컬 라이프 조회")
    @Test
    void testGetMedicalLifeList_Success() {

        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(medicalLifeRepository.findAllByMedicalLifeIsDeletedFalse()).thenReturn(List.of(medicalLife));

        // when
        List<MedicalLifeListDTO> result = medicalLifeService.getMedicalLifeList(1L);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(1L);
        verify(medicalLifeRepository, times(1)).findAllByMedicalLifeIsDeletedFalse();
    }


}

