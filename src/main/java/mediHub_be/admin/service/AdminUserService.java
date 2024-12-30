package mediHub_be.admin.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminResponseDTO;
import mediHub_be.admin.dto.AdminUpdateDTO;
import mediHub_be.admin.dto.AdminUserDetailResponseDTO;
import mediHub_be.admin.dto.UserCreateDTO;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.part.entity.Part;
import mediHub_be.part.repository.PartRepository;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.user.dto.UserSearchDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;
import mediHub_be.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final RankingRepository rankingRepository;
    private final FlagRepository flagRepository;
    private final PictureRepository pictureRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AmazonS3Service amazonS3Service;

    @Value("${user.default-password}")
    private String defaultPassword;


    @Transactional
    public User registerUser(UserCreateDTO userCreateDTO, MultipartFile profileImage, String currentUserId) throws IOException {

        // Part 및 Ranking 정보 확인
        Part part = partRepository.findById(userCreateDTO.getPartSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PART));
        Ranking ranking = rankingRepository.findById(userCreateDTO.getRankingSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RANKING));

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userCreateDTO.getUserPassword());

        // User 저장
        User user = new User(
                userCreateDTO.getUserId(),
                encodedPassword,
                userCreateDTO.getUserName(),
                userCreateDTO.getUserEmail(),
                userCreateDTO.getUserPhone(),
                part,
                ranking,
                userCreateDTO.getUserAuth() != null ? userCreateDTO.getUserAuth() : UserAuth.USER,
                userCreateDTO.getUserStatus() != null ? userCreateDTO.getUserStatus() : UserStatus.ACTIVE
        );
        userRepository.save(user);

        // Flag 저장
        Flag flag = Flag.builder()
                .flagType("USER")
                .flagEntitySeq(user.getUserSeq())
                .build();
        flagRepository.save(flag);

        // S3에 프로필 사진 업로드 및 Picture 저장
        if (profileImage != null && !profileImage.isEmpty()) {
            AmazonS3Service.MetaData metaData = amazonS3Service.upload(profileImage);

            Picture picture = Picture.builder()
                    .flag(flag)
                    .pictureName(metaData.getOriginalFileName())
                    .pictureUrl(metaData.getUrl())
                    .pictureType(metaData.getType())
                    .pictureIsDeleted(false)
                    .build();
            pictureRepository.save(picture);
        }

        return user;
    }

    // 비밀번호 초기화
    @Transactional
    public void initializePassword(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));


        String encodedPassword = passwordEncoder.encode(defaultPassword);
        user.initializePassword(encodedPassword);
    }

    @Transactional
    public User updateUser(Long userSeq, AdminUpdateDTO adminUpdateDTO, MultipartFile profileImage) throws IOException {
        // 사용자 정보 조회
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 사용자 정보 수정
        Part part = partRepository.findById(adminUpdateDTO.getPartSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PART));
        Ranking ranking = rankingRepository.findById(adminUpdateDTO.getRankingSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RANKING));

        user.updateUserDetails(
                adminUpdateDTO.getUserSeq(),
                adminUpdateDTO.getUserEmail(),
                adminUpdateDTO.getUserPhone(),
                part,
                ranking,
                adminUpdateDTO.getUserAuth(),
                adminUpdateDTO.getUserStatus()
        );

        // Flag 조회
        List<Flag> flags = flagRepository.findAllByFlagEntitySeq(userSeq);

        Flag flag = flags.stream()
                .filter(f -> f.getFlagType().equals("USER"))
                .findFirst()
                .orElse(null);

        if (flag == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_FLAG);
        }

        // 기존 사진 삭제
        if (profileImage != null && !profileImage.isEmpty()) {
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

    // 유저 소프트 삭제 - > 상태 값이 ACTIVE 에서 DELETE 로 변환
    @Transactional
    public void deleteUser(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        user.markAsDeleted();
    }

    @Transactional(readOnly = true)
    public List<AdminResponseDTO> getAllUsers() {
        // 모든 사용자 조회
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {

                    // USER Flag 조회
                    Optional<Flag> optionalFlag = flagRepository.findByFlagTypeAndFlagEntitySeq("USER", user.getUserSeq());

                    // 프로필 이미지 URL 가져오기
                    String profileImage = optionalFlag.flatMap(flag ->
                            pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())
                    ).map(Picture::getPictureUrl).orElse(null);

                    // AdminResponseDTO 생성 (빌더 패턴 사용)
                    return AdminResponseDTO.builder()
                            .userSeq(user.getUserSeq())
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .userEmail(user.getUserEmail())
                            .userPhone(user.getUserPhone())
                            .rankingName(user.getRanking() != null ? user.getRanking().getRankingName() : null)
                            .partName(user.getPart() != null ? user.getPart().getPartName() : null)
                            .deptName(user.getPart() != null && user.getPart().getDept() != null
                                    ? user.getPart().getDept().getDeptName() : null)
                            .profileImage(profileImage)
                            .userStatus(user.getUserStatus().name())
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 특정 유저 조회
    @Transactional(readOnly = true)
    public AdminUserDetailResponseDTO getUserBySeq(Long userSeq) {

        // User 조회
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // USER Flag 조회
        Optional<Flag> optionalFlag = flagRepository.findByFlagTypeAndFlagEntitySeq("USER", user.getUserSeq());

        // 프로필 사진 URL 조회
        String profileImage = null;
        if (optionalFlag.isPresent()) {
            Long flagSeq = optionalFlag.get().getFlagSeq();
            Optional<Picture> picture = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flagSeq);
            profileImage = picture.map(Picture::getPictureUrl).orElse(null);
        }

        return AdminUserDetailResponseDTO.builder()
                .userSeq(user.getUserSeq())
                .userName(user.getUserName())
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userPhone(user.getUserPhone())
                .userAuth(UserAuth.valueOf(user.getUserAuth().name()))
                .userStatus(UserStatus.valueOf(user.getUserStatus().name()))
                .rankingName(user.getRanking().getRankingName())
                .partName(user.getPart().getPartName())
                .deptSeq(user.getPart().getDept().getDeptSeq())
                .profileImage(profileImage)
                .build();
    }


}


