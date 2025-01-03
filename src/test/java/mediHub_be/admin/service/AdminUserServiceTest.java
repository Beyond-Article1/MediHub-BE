package mediHub_be.admin.service;

import mediHub_be.admin.dto.AdminResponseDTO;
import mediHub_be.admin.dto.UserCreateDTO;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.part.entity.Part;
import mediHub_be.part.repository.PartRepository;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;
import mediHub_be.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminUserServiceTest {

    @InjectMocks
    private AdminUserService adminUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PartRepository partRepository;

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private FlagRepository flagRepository;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AmazonS3Service amazonS3Service;

    @Mock
    private MultipartFile profileImage;

    @Mock
    private Part part;

    @Mock
    private Ranking ranking;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock 데이터 설정
        when(part.getPartSeq()).thenReturn(1L);
        when(ranking.getRankingSeq()).thenReturn(1L);
        when(user.getUserSeq()).thenReturn(1L);
        when(user.getUserId()).thenReturn("testUser");
        when(user.getUserPassword()).thenReturn("encodedPassword");
        when(user.getUserName()).thenReturn("Test Name");
        when(user.getUserEmail()).thenReturn("test@example.com");
        when(user.getUserPhone()).thenReturn("123456789");
        when(user.getPart()).thenReturn(part);
        when(user.getRanking()).thenReturn(ranking);
        when(user.getUserAuth()).thenReturn(UserAuth.USER);
        when(user.getUserStatus()).thenReturn(UserStatus.ACTIVE);
    }

    @DisplayName("회원 등록")
    @Test
    void testRegisterUser_Success() throws IOException {
        // given
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUserId("testUser");
        userCreateDTO.setUserPassword("password");
        userCreateDTO.setUserName("Test Name");
        userCreateDTO.setUserEmail("test@example.com");
        userCreateDTO.setUserPhone("123456789");
        userCreateDTO.setPartSeq(1L);
        userCreateDTO.setRankingSeq(1L);

        MultipartFile profileImage = mock(MultipartFile.class);

        // Mock MetaData 객체 생성
        AmazonS3Service.MetaData metaData = mock(AmazonS3Service.MetaData.class);
        when(metaData.getOriginalFileName()).thenReturn("originalFileName");
        when(metaData.getUrl()).thenReturn("fileUrl");
        when(metaData.getType()).thenReturn("image/jpeg");

        // Mock 설정
        when(partRepository.findById(1L)).thenReturn(Optional.of(part));
        when(rankingRepository.findById(1L)).thenReturn(Optional.of(ranking));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(amazonS3Service.upload(profileImage)).thenReturn(metaData);

        // when
        User registeredUser = adminUserService.registerUser(userCreateDTO, profileImage, "currentUser");

        // then
        assertNotNull(registeredUser);
        assertEquals("testUser", registeredUser.getUserId());
        verify(userRepository, times(1)).save(any(User.class));
        verify(flagRepository, times(1)).save(any(Flag.class));
        verify(amazonS3Service, times(1)).upload(profileImage);
    }

    @DisplayName("파트 네임 존재하지 않음")
    @Test
    void testRegisterUser_PartNotFound() {
        // given
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setPartSeq(999L);

        when(partRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> adminUserService.registerUser(userCreateDTO, null, "currentUser"));

        assertEquals(ErrorCode.NOT_FOUND_PART, exception.getErrorCode());
        verify(partRepository, times(1)).findById(999L); // findById 호출 여부 확인
    }

    @DisplayName("패스워드 초기화")
    @Test
    void testInitializePassword_Success() {
        // given
        // defaultPassword 값을 명시적으로 설정
        ReflectionTestUtils.setField(adminUserService, "defaultPassword", "defaultPassword");

        // userRepository와 passwordEncoder Mock 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("defaultPassword")).thenReturn("newEncodedPassword");

        // when
        adminUserService.initializePassword(1L);

        // then
        // userRepository의 findById 호출 여부 확인
        verify(userRepository, times(1)).findById(1L);

        // user 객체의 initializePassword 메서드가 "newEncodedPassword"로 호출되었는지 확인
        verify(user, times(1)).initializePassword("newEncodedPassword");
    }

    @DisplayName("회원 소프트 삭제")
    @Test
    void testDeleteUser_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        adminUserService.deleteUser(1L);

        // then
        verify(userRepository, times(1)).findById(1L);
        verify(user).markAsDeleted();
    }

    @DisplayName("모든 유저 조회")
    @Test
    void testGetAllUsers_Success() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(flagRepository.findByFlagTypeAndFlagEntitySeq("USER", 1L)).thenReturn(Optional.of(new Flag()));

        // when
        List<AdminResponseDTO> users = adminUserService.getAllUsers();

        // then
        assertNotNull(users);
        assertEquals(1, users.size());
        AdminResponseDTO dto = users.get(0);
        assertEquals("testUser", dto.getUserId());
        verify(userRepository, times(1)).findAll();
    }
}
