package mediHub_be.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.dto.UserResponseDTO;
import mediHub_be.user.dto.UserSearchDTO;
import mediHub_be.user.dto.UserUpdateRequestDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FlagRepository flagRepository;
    private final PictureRepository pictureRepository;
    private final AmazonS3Service amazonS3Service;
    private final BCryptPasswordEncoder passwordEncoder;

    // ############################ 기본 프로필 사진 url(S3에 업로드 하면 주소 바꿔주세요!!!) ############################
    public static final String DEFAULT_PROFILE_URL = "https://png.pngtree.com/png-clipart/20220112/ourmid/pngtree-cartoon-hand-drawn-default-avatar-png-image_4154232.png";
    // ############################ 기본 프로필 사진 url(S3에 업로드 하면 주소 바꿔주세요!!!) ############################

    // 회원 자기 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDTO getUserInfo(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // USER Flag 조회
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("USER", userSeq)
                .orElse(null);

        // 프로필 이미지 URL 조회
        String profileImage = null;
        if (flag != null) {
            profileImage = pictureRepository.findByFlag_FlagSeq(flag.getFlagSeq())
                    .map(Picture::getPictureUrl)
                    .orElse(null);
        }

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userPhone(user.getUserPhone())
                .rankingName(user.getRanking().getRankingName())
                .partName(user.getPart().getPartName())
                .profileImage(profileImage)
                .build();
    }

    // 회원 자기 정보 수정
    @Transactional
    public User updateUser(Long userSeq, UserUpdateRequestDTO userUpdateRequestDTO, MultipartFile profileImage) throws IOException {
        // 사용자 정보 조회
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 이메일, 전화번호, 비밀번호 수정
        String updatedEmail = userUpdateRequestDTO.getUserEmail() != null ? userUpdateRequestDTO.getUserEmail() : user.getUserEmail();
        String updatedPhone = userUpdateRequestDTO.getUserPhone() != null ? userUpdateRequestDTO.getUserPhone() : user.getUserPhone();
        String updatedPassword = userUpdateRequestDTO.getUserPassword() != null
                ? passwordEncoder.encode(userUpdateRequestDTO.getUserPassword())
                : user.getUserPassword();

        user.updateUserinfo(updatedEmail, updatedPhone, updatedPassword);

        // 프로필 이미지 처리
        if (profileImage != null && !profileImage.isEmpty()) {
            Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("USER", userSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

            // 기존 사진 삭제
            List<Picture> pictures = pictureRepository.findAllByFlag_FlagSeq(flag.getFlagSeq());
            for (Picture picture : pictures) {
                amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
                picture.setDeleted();
                pictureRepository.save(picture);
            }

            // 새 사진 업로드
            AmazonS3Service.MetaData metaData = amazonS3Service.upload(profileImage);
            Picture newPicture = Picture.builder()
                    .flag(flag)
                    .pictureName(metaData.getOriginalFileName())
                    .pictureUrl(metaData.getUrl())
                    .pictureType(metaData.getType())
                    .pictureIsDeleted(false)
                    .build();
            pictureRepository.save(newPicture);
        }

        return user;
    }

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<UserSearchDTO> getAllUsers() {
        // 모든 사용자 조회
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {

                    // USER Flag 조회
                    Optional<Flag> optionalFlag = flagRepository.findByFlagTypeAndFlagEntitySeq("USER", user.getUserSeq());

                    // 프로필 이미지 URL 가져오기
                    String profileImage = null;
                    if (optionalFlag.isPresent()) {
                        Long flagSeq = optionalFlag.get().getFlagSeq();
                        Optional<Picture> picture = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flagSeq);
                        profileImage = picture.map(Picture::getPictureUrl).orElse(null);
                    }

                    // UserSearchDTO 생성
                    UserSearchDTO dto = new UserSearchDTO();
                    dto.setUserName(user.getUserName());
                    dto.setUserEmail(user.getUserEmail());
                    dto.setUserPhone(user.getUserPhone());
                    dto.setRankingName(user.getRanking().getRankingName());
                    dto.setPartName(user.getPart().getPartName());
                    dto.setProfileImage(profileImage);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // userSeq로 User 조회
    public User findUser(Long userSeq) {
        return userRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    // userId로 User 조회
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public boolean validateAdmin(User user){
        return user.getUserAuth().equals(UserAuth.ADMIN);
    }

}

