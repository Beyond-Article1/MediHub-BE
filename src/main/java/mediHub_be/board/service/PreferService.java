package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.dto.PreferDTO;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Prefer;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PreferRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreferService {

    private final UserRepository userRepository;
    private final FlagRepository flagRepository;
    private final PreferRepository preferRepository;
    private final FlagService flagService;

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

    // 직원과 식별 번호로 본인이 좋아요 한 해당 게시판 종류 좋아요 찾기
    public List<PreferDTO> findByUserAndFlagType(User user, String flagType) {

        return preferRepository.findByUserAndFlagType(user, flagType);
    }

    // 게시글이 삭제되었을 때, 해당 게시글과 연결된 좋아요 삭제
    public void deletePreferByFlag(Flag flag) {

        preferRepository.deleteAllByFlagSeq(flag.getFlagSeq());
    }

    // 식별 번호를 찾아서 게시글이 삭제되었을 때, 해당 게시글과 연결된 좋아요 삭제
    public void deletePreferByFlag(String flagType, Long entitySeq) {

        // 1. Flag 조회
        Flag flag = flagService.findFlag(flagType, entitySeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 2. 북마크 삭제
        deletePreferByFlag(flag);
    }
}