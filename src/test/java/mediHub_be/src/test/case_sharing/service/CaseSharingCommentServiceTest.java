package mediHub_be.src.test.case_sharing.service;

import mediHub_be.board.entity.Flag;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.PictureService;
import mediHub_be.case_sharing.dto.CaseSharingCommentDetailDTO;
import mediHub_be.case_sharing.dto.CaseSharingCommentListDTO;
import mediHub_be.case_sharing.dto.CaseSharingCommentRequestDTO;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.entity.CaseSharingComment;
import mediHub_be.case_sharing.repository.CaseSharingCommentRepository;
import mediHub_be.case_sharing.service.CaseSharingCommentService;
import mediHub_be.case_sharing.service.CaseSharingService;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.notify.service.NotifyServiceImlp;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaseSharingCommentServiceTest {

    @InjectMocks
    private CaseSharingCommentService commentService;

    @Mock
    private CaseSharingService caseSharingService;

    @Mock
    private UserService userService;

    @Mock
    private PictureService pictureService;

    @Mock
    private CaseSharingCommentRepository commentRepository;

    @Mock
    private NotifyServiceImlp notifyServiceImlp;

    @Mock
    private FlagService flagService;

    private static final String USER_ID = "testUser";
    private static final Long CASE_SHARING_SEQ = 1L;
    private static final Long COMMENT_SEQ = 1L;
    private static final String BLOCK_ID = "block123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCommentList() {
        // Arrange
        User user = mock(User.class);
        when(userService.findByUserId(USER_ID)).thenReturn(user);

        CaseSharingComment comment = CaseSharingComment.builder()
                .caseSharingBlockId(BLOCK_ID)
                .build();
        when(commentRepository.findByCaseSharing_CaseSharingSeqAndDeletedAtIsNull(CASE_SHARING_SEQ))
                .thenReturn(List.of(comment));

        // Act
        List<CaseSharingCommentListDTO> result = commentService.getCommentList(USER_ID, CASE_SHARING_SEQ);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BLOCK_ID, result.get(0).getBlockId());
    }

    @Test
    void testGetCommentsByBlock() {
        // Arrange
        User user = mock(User.class);
        Ranking ranking = mock(Ranking.class); // Ranking 객체를 Mock으로 생성

        // User와 Ranking의 동작 정의
        when(userService.findByUserId(USER_ID)).thenReturn(user);
        when(user.getRanking()).thenReturn(ranking);
        when(ranking.getRankingName()).thenReturn("Test Rank");

        CaseSharingComment comment = CaseSharingComment.builder()
                .caseSharingBlockId(BLOCK_ID)
                .caseSharingCommentSeq(COMMENT_SEQ)
                .user(user)
                .build();

        when(commentRepository.findByCaseSharing_CaseSharingSeqAndCaseSharingBlockIdAndDeletedAtIsNull(
                CASE_SHARING_SEQ, BLOCK_ID)).thenReturn(List.of(comment));

        when(pictureService.getUserProfileUrl(anyLong())).thenReturn("http://profile.url");

        // Act
        List<CaseSharingCommentDetailDTO> result = commentService.getCommentsByBlock(USER_ID, CASE_SHARING_SEQ, BLOCK_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BLOCK_ID, result.get(0).getBlockId());
        assertEquals("Test Rank", result.get(0).getUserRankName()); // Ranking 확인
    }

    @Test
    void testCreateCaseSharingComment() {
        // Arrange
        User user = mock(User.class);
        CaseSharing caseSharing = mock(CaseSharing.class);
        Flag flag = mock(Flag.class);
        CaseSharingCommentRequestDTO requestDTO = new CaseSharingCommentRequestDTO();
        requestDTO.setContent("Test Content");
        requestDTO.setBlockId(BLOCK_ID);

        when(userService.findByUserId(USER_ID)).thenReturn(user);
        when(caseSharingService.findCaseSharing(CASE_SHARING_SEQ)).thenReturn(caseSharing);
        when(flagService.findFlag("CASE_SHARING", CASE_SHARING_SEQ)).thenReturn(Optional.of(flag));

        // Mock: commentRepository.save()
        when(commentRepository.save(any(CaseSharingComment.class))).thenAnswer(invocation -> {
            CaseSharingComment argument = invocation.getArgument(0);
            Field field = CaseSharingComment.class.getDeclaredField("caseSharingCommentSeq");
            field.setAccessible(true);
            field.set(argument, COMMENT_SEQ); // 리플렉션으로 값 설정
            return argument;
        });

        User caseOwner = mock(User.class);
        when(caseSharing.getUser()).thenReturn(caseOwner);

        doNothing().when(notifyServiceImlp).send(
                eq(user),
                eq(caseOwner),
                eq(flag),
                eq(NotiType.COMMENT),
                eq("/case_sharing/" + CASE_SHARING_SEQ)
        );

        // Act
        Long result = commentService.createCaseSharingComment(USER_ID, CASE_SHARING_SEQ, requestDTO);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(COMMENT_SEQ, result, "Returned commentSeq does not match expected value");
        verify(commentRepository, times(1)).save(any(CaseSharingComment.class));
        verify(notifyServiceImlp, times(1)).send(
                eq(user),
                eq(caseOwner),
                eq(flag),
                eq(NotiType.COMMENT),
                eq("/case_sharing/" + CASE_SHARING_SEQ)
        );
    }

    @Test
    void testUpdateCaseSharingComment() {
        // Arrange
        User user = mock(User.class);
        CaseSharingComment comment = mock(CaseSharingComment.class);
        CaseSharingCommentRequestDTO requestDTO = new CaseSharingCommentRequestDTO();
        requestDTO.setContent("Updated Content");
        requestDTO.setBlockId(BLOCK_ID);

        // Mock 설정
        when(userService.findByUserId(USER_ID)).thenReturn(user);
        when(commentRepository.findById(COMMENT_SEQ)).thenReturn(Optional.of(comment));
        when(comment.getUser()).thenReturn(user); // comment.getUser() 호출 시 user 반환
        doNothing().when(comment).updateComment(requestDTO.getContent(), requestDTO.getBlockId());

        // Act
        commentService.updateCaseSharingComment(USER_ID, COMMENT_SEQ, requestDTO);

        // Assert
        verify(comment).updateComment(requestDTO.getContent(), requestDTO.getBlockId());
        verify(commentRepository).save(comment);
    }

    @Test
    void testDeleteCaseSharingComment() {
        // Arrange
        User user = mock(User.class);
        CaseSharingComment comment = mock(CaseSharingComment.class);

        // Mock 설정
        when(userService.findByUserId(USER_ID)).thenReturn(user);
        when(commentRepository.findById(COMMENT_SEQ)).thenReturn(Optional.of(comment));
        when(comment.getUser()).thenReturn(user); // comment.getUser() 호출 시 user 반환
        doNothing().when(comment).markAsDeleted();

        // Act
        commentService.deleteCaseSharingComment(USER_ID, COMMENT_SEQ);

        // Assert
        verify(comment).markAsDeleted(); // markAsDeleted()가 호출되었는지 확인
        verify(commentRepository).save(comment); // commentRepository.save()가 호출되었는지 확인
    }


}
