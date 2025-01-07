package mediHub_be.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.repository.BookmarkRepository;
import mediHub_be.board.repository.PreferRepository;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.follow.repository.FollowRepository;
import mediHub_be.medicalLife.entity.MedicalLife;
import mediHub_be.medicalLife.repository.MedicalLifeRepository;
import mediHub_be.user.dto.UserResponseDTO;
import mediHub_be.user.dto.UserSearchDTO;
import mediHub_be.user.dto.UserTop3DTO;
import mediHub_be.user.dto.UserUpdateRequestDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
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
    private final FollowRepository followRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PreferRepository preferRepository;
    private final CaseSharingRepository caseSharingRepository;
    private final MedicalLifeRepository medicalLifeRepository;

    public static final String DEFAULT_PROFILE_URL = "https://medihub.s3.ap-northeast-2.amazonaws.com/istockphoto-981306968-1024x1024.jpg";

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
            profileImage = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())
                    .map(Picture::getPictureUrl)
                    .orElse(null);
        }

        String deptName = user.getPart() != null && user.getPart().getDept() != null
                ? user.getPart().getDept().getDeptName() : null;

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userPhone(user.getUserPhone())
                .rankingName(user.getRanking().getRankingName())
                .partName(user.getPart() != null ? user.getPart().getPartName() : null)
                .deptName(deptName)
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
        String updatedEmail = (userUpdateRequestDTO.getUserEmail() != null && !userUpdateRequestDTO.getUserEmail().isEmpty())
                ? userUpdateRequestDTO.getUserEmail()
                : user.getUserEmail();
        String updatedPhone = (userUpdateRequestDTO.getUserPhone() != null && !userUpdateRequestDTO.getUserPhone().isEmpty())
                ? userUpdateRequestDTO.getUserPhone()
                : user.getUserPhone();
        String updatedPassword = (userUpdateRequestDTO.getUserPassword() != null && !userUpdateRequestDTO.getUserPassword().isEmpty())
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
                    dto.setUserSeq(user.getUserSeq());
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

    public boolean isDoctor(Long userSeq) {
        User user = userRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if ("진료과".equals(user.getPart().getDept().getDeptName())) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<UserTop3DTO> getTop3Users() {
        // 현재 날짜 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfPreviousMonth = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
        LocalDateTime endOfPreviousMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(23, 59, 59);
        List<User> users = userRepository.findAll();
        // 유저별 데이터 조합
        List<UserTop3DTO> userTop3List = users.stream()
                .map(user -> {
                    // 유저가 작성한 이전 달의 CaseSharing 조회
                    List<Long> caseSharingIds = caseSharingRepository.findAllByUserAndCreatedAtBetween(
                            user, startOfPreviousMonth, endOfPreviousMonth
                    ).stream().map(CaseSharing::getCaseSharingSeq).collect(Collectors.toList());

                    // 유저가 작성한 이전 달의 MedicalLife 조회
                    List<Long> medicalLifeIds = medicalLifeRepository.findAllByUserAndCreatedAtBetween(
                            user, startOfPreviousMonth, endOfPreviousMonth
                    ).stream().map(MedicalLife::getMedicalLifeSeq).collect(Collectors.toList());

                    // 작성한 CaseSharing 글들에 대한 북마크 수 합산
                    Long caseSharingBookmarkCount = bookmarkRepository.countByFlag_FlagEntitySeqInAndFlag_FlagType(
                            caseSharingIds, "CASE_SHARRING");

                    // 작성한 MedicalLife 글들에 대한 북마크 수 합산
                    Long medicalLifeBookmarkCount = bookmarkRepository.countByFlag_FlagEntitySeqInAndFlag_FlagType(
                            medicalLifeIds, "MEDICAL_LIFE");

                    // 작성한 MedicalLife 글들에 대한 좋아요 수 합산
                    Long medicalLifeLikeCount = preferRepository.countByFlag_FlagEntitySeqInAndFlag_FlagType(
                            medicalLifeIds, "MEDICAL_LIFE");

                    // 프로필 이미지 URL 조회
                    String profileImage = null;
                    Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("USER", user.getUserSeq()).orElse(null);
                    if (flag != null) {
                        profileImage = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())
                                .map(Picture::getPictureUrl)
                                .orElse(null);
                    }

                    // 파트 및 부서 정보 조회
                    String partName = user.getPart() != null ? user.getPart().getPartName() : null;

                    // 랭킹 정보 조회
                    String rankingName = user.getRanking() != null ? user.getRanking().getRankingName() : null;

                    // 총 점수 계산
                    Long totalScore = (caseSharingBookmarkCount == null ? 0 : caseSharingBookmarkCount) +
                            (medicalLifeBookmarkCount == null ? 0 : medicalLifeBookmarkCount) +
                            (medicalLifeLikeCount == null ? 0 : medicalLifeLikeCount);

                    // DTO 빌드
                    return UserTop3DTO.builder()
                            .userName(user.getUserName())
                            .rankingName(rankingName)
                            .partName(partName)
                            .likeNum(medicalLifeLikeCount == null ? 0 : medicalLifeLikeCount)
                            .bookmarkNum((caseSharingBookmarkCount == null ? 0 : caseSharingBookmarkCount) +
                                    (medicalLifeBookmarkCount == null ? 0 : medicalLifeBookmarkCount))
                            .profileUrl(profileImage)
                            .totalScore(totalScore)
                            .build();
                })
                .sorted((a, b) -> b.getTotalScore().compareTo(a.getTotalScore())) // 총 점수 기준 내림차순 정렬
                .limit(3) // 상위 3명
                .collect(Collectors.toList());

        return userTop3List;

    }


    // userSeqs로 Users 조회 (한번에 여러 사용자 정보 조회)
    public List<User> findUsersBySeqs(List<Long> allUserSeqs) {
        return userRepository.findAllById(allUserSeqs);
    }

    public boolean validateAdmin(User user){
        return !user.getUserAuth().equals(UserAuth.ADMIN);
    }

    public List<User> findFollowersByUser(User user) {
        return followRepository.findFollowersByUser(user);
    }


}

