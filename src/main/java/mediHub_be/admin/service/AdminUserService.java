package mediHub_be.admin.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminUpdateDTO;
import mediHub_be.admin.dto.UserCreateDTO;
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
import mediHub_be.user.entity.User;
import mediHub_be.user.entity.UserAuth;
import mediHub_be.user.entity.UserStatus;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final RankingRepository rankingRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FlagRepository flagRepository;
    private final PictureRepository pictureRepository;

    // 회원 등록
    @Transactional
    public User registerUser(UserCreateDTO userCreateDTO) {
        Part part = partRepository.findById(userCreateDTO.getPartSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Ranking ranking = rankingRepository.findById(userCreateDTO.getRankingSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        String encodedPassword = passwordEncoder.encode(userCreateDTO.getUserPassword());

        User user = new User(
                userCreateDTO.getUserId(),
                encodedPassword,
                userCreateDTO.getUserName(),
                userCreateDTO.getUserEmail(),
                userCreateDTO.getUserPhone(),
                part,
                ranking,
                userCreateDTO.getUserAuth() != null ? UserAuth.valueOf(userCreateDTO.getUserAuth()) : UserAuth.USER,
                userCreateDTO.getUserStatus() != null ? UserStatus.valueOf(userCreateDTO.getUserStatus()) : UserStatus.ACTIVE
        );

        userRepository.save(user);

        // 2. Flag 생성
        Flag flag = Flag.builder()
                .flagtype("user") // 플래그 타입 설정
                .flagentity_seq(user.getUserSeq()) // 사용자와 연결
                .build();

        flagRepository.save(flag);

        // 3. 사진 등록
        Picture picture = Picture.builder()
                .user(user)
                .flag(flag)
                .pictureUrl("pictureUrl")
                .pictureName("pictureName")
                .pictureType("pictureType")
                .build();

        pictureRepository.save(picture);

        return user;
    }

    // 회원 수정
    @Transactional
    public User updateUser(Long userSeq, AdminUpdateDTO adminUpdateDTO) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Part part = partRepository.findById(adminUpdateDTO.getPartSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Ranking ranking = rankingRepository.findById(adminUpdateDTO.getRankingSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));


        user.updateUser(
                adminUpdateDTO.getUserEmail(),
                adminUpdateDTO.getUserPhone(),
                part,
                ranking,
                adminUpdateDTO.getUserAuth(),
                adminUpdateDTO.getUserStatus()
        );

        return user;
    }

    // 비밀번호 초기화
    @Transactional
    public void initializePassword(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 초기화 비밀번호 설정 및 암호화
        String defaultPassword = "12345";
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        // 비밀번호만 업데이트
        user.initializePassword(encodedPassword);
    }

    // 회원 삭제
    @Transactional
    public void deleteUser(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        userRepository.delete(user);
    }
}


