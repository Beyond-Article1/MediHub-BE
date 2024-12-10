package mediHub_be.admin.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminUpdateDTO;
import mediHub_be.admin.dto.UserCreateDTO;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.anonymousBoard.dto.RequestPicture;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
    @RequiredArgsConstructor
    public class AdminUserService {

        private final UserRepository userRepository;
        private final PartRepository partRepository;
        private final RankingRepository rankingRepository;
        private final FlagRepository flagRepository;
        private final PictureRepository pictureRepository;
        private final BCryptPasswordEncoder passwordEncoder;
        private final AmazonS3Service amazonS3Service; // S3 서비스 추가

        @Transactional

        public User registerUser(UserCreateDTO userCreateDTO, MultipartFile profileImage) throws IOException {
            // 1. 사용자 생성
            Part part = partRepository.findById(userCreateDTO.getPartSeq())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PART));

            Ranking ranking = rankingRepository.findById(userCreateDTO.getRankingSeq())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RANKING));

            String encodedPassword = passwordEncoder.encode(userCreateDTO.getUserPassword());

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

            // 2. Flag 생성
            Flag flag = Flag.builder()
                    .flagType("USER")
                    .flagEntitySeq(user.getUserSeq())
                    .build();

            flagRepository.save(flag);

            // 3. 프로필 사진 업로드 및 저장
            if (profileImage != null && !profileImage.isEmpty()) {
                // S3에 업로드
                AmazonS3Service.MetaData metaData = amazonS3Service.upload(profileImage);

                // Picture 데이터 생성
                RequestPicture requestPicture = new RequestPicture();

                requestPicture.setFlagSeq(flag.getFlagSeq());
                requestPicture.setPictureName(metaData.getOriginalFileName());
                requestPicture.setPictureChangedName(metaData.getChangeFileName());
                requestPicture.setPictureUrl(metaData.getUrl());
                requestPicture.setPictureType(metaData.getType());

                Picture picture = new Picture();

                picture.create(flag, requestPicture);

                pictureRepository.save(picture);
            }

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

}


