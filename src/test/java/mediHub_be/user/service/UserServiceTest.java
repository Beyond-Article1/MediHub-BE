package mediHub_be.user.service;

import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.follow.repository.FollowRepository;
import mediHub_be.part.entity.Part;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.user.dto.UserResponseDTO;
import mediHub_be.user.dto.UserSearchDTO;
import mediHub_be.user.dto.UserUpdateRequestDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlagRepository flagRepository;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private AmazonS3Service amazonS3Service;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private MultipartFile profileImage;

    @Mock
    private User user;

    @Mock
    private Flag flag;

    @Mock
    private Picture picture;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("회원 정보 조회")
    @Test
    void testGetUserInfo_Success() {
        // given
        Long userSeq = 1L;

        // Mock Ranking 객체 생성 및 설정
        Ranking ranking = mock(Ranking.class);
        when(ranking.getRankingName()).thenReturn("Test Ranking");

        // Mock User 객체 설정
        when(user.getRanking()).thenReturn(ranking);
        when(user.getUserId()).thenReturn("testUser");
        when(user.getUserName()).thenReturn("Test Name");
        when(user.getUserEmail()).thenReturn("test@example.com");
        when(user.getUserPhone()).thenReturn("123456789");

        // Mock Flag 및 Picture 설정
        when(userRepository.findById(userSeq)).thenReturn(Optional.of(user));
        when(flagRepository.findByFlagTypeAndFlagEntitySeq("USER", userSeq)).thenReturn(Optional.of(flag));
        when(pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq()))
                .thenReturn(Optional.of(picture));
        when(picture.getPictureUrl()).thenReturn("testUrl");

        // when
        UserResponseDTO responseDTO = userService.getUserInfo(userSeq);

        // then
        assertNotNull(responseDTO); // 반환된 DTO가 null이 아님을 확인
        assertEquals("testUser", responseDTO.getUserId());
        assertEquals("Test Name", responseDTO.getUserName());
        assertEquals("test@example.com", responseDTO.getUserEmail());
        assertEquals("123456789", responseDTO.getUserPhone());
        assertEquals("Test Ranking", responseDTO.getRankingName());
        assertEquals("testUrl", responseDTO.getProfileImage());

        // Mock 호출 검증
        verify(userRepository, times(1)).findById(userSeq);
        verify(flagRepository, times(1)).findByFlagTypeAndFlagEntitySeq("USER", userSeq);
    }

    @DisplayName("자기 정보 업데이트")
    @Test
    void testUpdateUser_Success() throws IOException {
        // given
        Long userSeq = 1L;
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setUserEmail("newEmail@example.com");
        userUpdateRequestDTO.setUserPhone("123456789");
        userUpdateRequestDTO.setUserPassword("newPassword");

        // Mock MetaData 객체 생성
        AmazonS3Service.MetaData metaData = mock(AmazonS3Service.MetaData.class);
        when(metaData.getOriginalFileName()).thenReturn("fileName");
        when(metaData.getUrl()).thenReturn("fileUrl");
        when(metaData.getType()).thenReturn("image/jpeg");

        // Mock 객체 설정
        when(userRepository.findById(userSeq)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(flagRepository.findByFlagTypeAndFlagEntitySeq("USER", userSeq)).thenReturn(Optional.of(flag));
        when(amazonS3Service.upload(profileImage)).thenReturn(metaData);

        // when
        User updatedUser = userService.updateUser(userSeq, userUpdateRequestDTO, profileImage);

        // then
        assertNotNull(updatedUser);
        verify(userRepository, times(1)).findById(userSeq);
        verify(user, times(1)).updateUserinfo("newEmail@example.com", "123456789", "encodedPassword");
        verify(flagRepository, times(1)).findByFlagTypeAndFlagEntitySeq("USER", userSeq);
        verify(amazonS3Service, times(1)).upload(profileImage);
    }


    @DisplayName("모든 회원 조회")
    @Test
    void testGetAllUsers_Success() {
        // given
        Ranking ranking = mock(Ranking.class);
        when(ranking.getRankingName()).thenReturn("Test Ranking");

        Part part = mock(Part.class);
        when(part.getPartName()).thenReturn("Test Part");

        when(user.getRanking()).thenReturn(ranking);
        when(user.getPart()).thenReturn(part);
        when(user.getUserSeq()).thenReturn(1L);
        when(user.getUserName()).thenReturn("Test User");
        when(user.getUserEmail()).thenReturn("test@example.com");
        when(user.getUserPhone()).thenReturn("123456789");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(flagRepository.findByFlagTypeAndFlagEntitySeq("USER", user.getUserSeq())).thenReturn(Optional.of(flag));
        when(pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())).thenReturn(Optional.of(picture));
        when(picture.getPictureUrl()).thenReturn("testUrl");

        // when
        List<UserSearchDTO> users = userService.getAllUsers();

        // then
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Test User", users.get(0).getUserName());
        assertEquals("test@example.com", users.get(0).getUserEmail());
        assertEquals("Test Ranking", users.get(0).getRankingName());
        assertEquals("Test Part", users.get(0).getPartName());
        verify(userRepository, times(1)).findAll();
    }

    @DisplayName("유저 찾기 ")
    @Test
    void testFindUser_Success() {
        // given
        Long userSeq = 1L;
        when(userRepository.findByUserSeq(userSeq)).thenReturn(Optional.of(user));

        // when
        User result = userService.findUser(userSeq);

        // then
        assertNotNull(result);
        verify(userRepository, times(1)).findByUserSeq(userSeq);
    }

    @DisplayName("유저를 찾을 수 없습니다.")
    @Test
    void testFindUser_NotFound() {
        // given
        Long userSeq = 999L;
        when(userRepository.findByUserSeq(userSeq)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> userService.findUser(userSeq));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
        verify(userRepository, times(1)).findByUserSeq(userSeq);
    }
}
