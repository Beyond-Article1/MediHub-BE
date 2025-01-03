package mediHub_be.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.repository.BookmarkRepository;
import mediHub_be.board.repository.PreferRepository;
import mediHub_be.config.amazonS3.AmazonS3Service;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.follow.repository.FollowRepository;
import mediHub_be.user.dto.UserResponseDTO;
import mediHub_be.user.dto.UserSearchDTO;
import mediHub_be.user.dto.UserTop3DTO;
import mediHub_be.user.dto.UserUpdateRequestDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
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
        // 현재 날짜에서 이전 달의 첫날과 마지막 날 계산
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDate startOfPreviousMonth = previousMonth.atDay(1);
        LocalDate endOfPreviousMonth = previousMonth.atEndOfMonth();
        log.info("이전달 날짜"+previousMonth);

        // 이전 달에 작성된 글에 대한 유저 조회
        List<User> users = userRepository.findAll();

        // 유저별 데이터 조합
        List<UserTop3DTO> userTop3List = users.stream()
                .map(user -> {
                    // 이전 달 북마크 수 조회
                    Long bookmarkCount = bookmarkRepository.countCaseSharingBookmarksByUserAndDateRange(
                            user, startOfPreviousMonth.atStartOfDay(), endOfPreviousMonth.atTime(23, 59, 59));

                    // 이전 달 좋아요 수 조회
                    Long likeCount = preferRepository.countMedicalLifeLikesByUserAndDateRange(
                            user, startOfPreviousMonth.atStartOfDay(), endOfPreviousMonth.atTime(23, 59, 59));

                    // 프로필 이미지 URL 조회
                    String profileImage = null;
                    Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("USER", user.getUserSeq()).orElse(null);
                    if (flag != null) {
                        profileImage = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())
                                .map(Picture::getPictureUrl)
                                .orElse(null);
                    }

                    // 파트 및 부서 정보 조회
                    String partName = null;
                    if (user.getPart() != null) {
                        partName = user.getPart().getPartName();
                    }

                    // 랭킹 정보 조회
                    String rankingName = user.getRanking() != null ? user.getRanking().getRankingName() : null;

                    // 총 점수 계산
                    Long totalScore = (bookmarkCount == null ? 0 : bookmarkCount) +
                            (likeCount == null ? 0 : likeCount);

                    // DTO 빌드
                    return UserTop3DTO.builder()
                            .userName(user.getUserName())
                            .rankingName(rankingName)
                            .partName(partName)
                            .likeNum(likeCount)
                            .bookmarkNum(bookmarkCount)
                            .profileUrl(profileImage) // 프로필 이미지 추가
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

