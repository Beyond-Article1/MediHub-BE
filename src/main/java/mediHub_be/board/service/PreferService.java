package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.entity.Prefer;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.PreferRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreferService {

    private final PreferRepository preferRepository;
    private final FlagRepository flagRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean togglePrefer(String flagType, Long entitySeq, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));
        // 기존 좋아요 존재 여부 확인
        Optional<Prefer> existingPrefer = preferRepository.findByUserAndFlag(user, flag);

        if(existingPrefer.isPresent()) {
            // 좋아요 해제 (삭제)
            preferRepository.delete(existingPrefer.get());

            // 좋아요 해제 상태 반환
            return false;
        } else {
            // 좋아요 설정(생성)
            Prefer prefer = Prefer.builder()
                    .user(user)
                    .flag(flag)
                    .build();

            preferRepository.save(prefer);

            // 좋아요 설정 상태 반환
            return true;
        }
    }

    public boolean isPreferred(String flagType, Long entitySeq, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 좋아요 존재 여부 반환
        return preferRepository.existsByUserAndFlag(user, flag);
    }
}