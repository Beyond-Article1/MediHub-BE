package mediHub_be.admin.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminDTO;
import mediHub_be.admin.dto.CreateUserDTO;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.part.entity.Part;
import mediHub_be.part.repository.PartRepository;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mediHub_be.board.repository.PictureRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final RankingRepository rankingRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PictureRepository pictureRepository;
    private final FlagRepository flagRepository;

    private static final String userFlag = "user_flag";

    @Transactional
    public Long registerUser(CreateUserDTO createUserDTO, String userId) {
        // 관리자 확인
        User admin = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("관리자 계정이 필요합니다."));

        // 중복 사용자 확인
        if (userRepository.findByUserId(createUserDTO.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다: " + createUserDTO.getUserId());
        }

        // 부서 및 등급 조회
        Part part = partRepository.findById(createUserDTO.getPartSeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서 ID입니다."));
        Ranking ranking = rankingRepository.findById(createUserDTO.getRankingSeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 등급 ID입니다."));

        // User 생성 및 저장
        User user = new User(
                createUserDTO.getUserId(),
                passwordEncoder.encode(createUserDTO.getUserPassword()),
                createUserDTO.getUserName(),
                createUserDTO.getUserEmail(),
                createUserDTO.getUserPhone(),
                part,
                ranking,
                null, // Picture는 나중에 설정
                createUserDTO.getUserAuth(),
                createUserDTO.getUserStatus()
        );
        userRepository.save(user);

        // Flag 생성 및 저장
        Flag flag = Flag.builder()
                .flagBoardFlag(userFlag)
                .flagPostSeq(user.getUserSeq()) // User ID를 기준으로 설정
                .build();
        flagRepository.save(flag);

        // Picture 생성 및 저장
        Picture picture = Picture.builder()
                .user(user)
                .flag(flag)
                .pictureName("default_picture")
                .pictureType("image/jpeg")
                .pictureUrl("https://example.com/default.jpg")
                .build();
        pictureRepository.save(picture);

        // User에 Picture 연관 설정 후 저장
        user.updateUser(
                user.getUserPassword(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getPart(),
                user.getRanking()
        );
        userRepository.save(user);

        return user.getUserSeq();
    }

    // 회원 정보 수정
//    @Transactional
//    public Long updateUser(Long userSeq, CreateUserDTO createUserDTO) {
//        // User 조회
//        User user = userRepository.findById(userSeq)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
//
//        // 부서 및 등급 조회
//        Part part = partRepository.findById(createUserDTO.getPartSeq())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서 ID입니다."));
//        Ranking ranking = rankingRepository.findById(createUserDTO.getRankingSeq())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 등급 ID입니다."));
//
//        // User 정보 업데이트
//        user.updateUser(
//                createUserDTO.getUserPassword() != null ? passwordEncoder.encode(createUserDTO.getUserPassword()) : user.getUserPassword(),
//                createUserDTO.getUserName(),
//                createUserDTO.getUserEmail(),
//                createUserDTO.getUserPhone(),
//                part,
//                ranking
//        );
//
//        // Picture가 변경될 경우 처리
//        if (createUserDTO.getPictureUrl() != null) {
//            Picture picture = user.getPicture();
//            if (picture != null) {
//                // 기존 Picture 업데이트
//                picture.setPictureUrl(createUserDTO.getPictureUrl());
//                picture.setPictureName(createUserDTO.getPictureName() != null ? createUserDTO.getPictureName() : picture.getPictureName());
//                picture.setPictureType(createUserDTO.getPictureType() != null ? createUserDTO.getPictureType() : picture.getPictureType());
//                pictureRepository.save(picture);
//            } else {
//                // 새로운 Picture 생성 및 저장
//                Picture newPicture = Picture.builder()
//                        .user(user) // User와 연관
//                        .flag(null) // 필요 시 Flag 설정
//                        .pictureName(createUserDTO.getPictureName() != null ? createUserDTO.getPictureName() : "default_picture")
//                        .pictureType(createUserDTO.getPictureType() != null ? createUserDTO.getPictureType() : "image/jpeg")
//                        .pictureUrl(createUserDTO.getPictureUrl())
//                        .build();
//                pictureRepository.save(newPicture);
//
//                // User와 새로운 Picture 연관 설정
//                user.updateUser(
//                        user.getUserPassword(),
//                        user.getUserName(),
//                        user.getUserEmail(),
//                        user.getUserPhone(),
//                        user.getPart(),
//                        user.getRanking()
//                );
//            }
//        }
//
//        userRepository.save(user);
//        return user.getUserSeq();
//    }

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<AdminDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    AdminDTO dto = new AdminDTO();
                    dto.setUserId(user.getUserId());
                    dto.setUserName(user.getUserName());
                    dto.setUserEmail(user.getUserEmail());
                    dto.setUserPhone(user.getUserPhone());
                    dto.setPartSeq(user.getPart());
                    dto.setRankingSeq(user.getRanking());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 특정 회원 조회
    @Transactional(readOnly = true)
    public AdminDTO getUserById(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        AdminDTO dto = new AdminDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserPhone(user.getUserPhone());
        dto.setPartSeq(user.getPart());
        dto.setRankingSeq(user.getRanking());
        return dto;
    }
}
