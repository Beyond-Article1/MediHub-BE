package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.entity.Prefer;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.PreferRepository;
import mediHub_be.board.repository.FlagRepository;
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
    public boolean togglePrefer(String boardFlag, Long postSeq, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));
        Flag flag = flagRepository.findByFlagBoardFlagAndFlagPostSeq(boardFlag, postSeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보를 찾을 수 없습니다."));
        // 기존 좋아요 존재 여부 확인
        Optional<Prefer> existingPrefer = preferRepository.findByUserAndFlag(user, flag);

        if(existingPrefer.isPresent()) {
            // 좋아요 해제 (삭제)
            preferRepository.delete(existingPrefer.get());

            return false; // 좋아요 해제 상태 반환
        } else {
            // 좋아요 설정 (생성)
            Prefer prefer = Prefer.builder()
                    .user(user)
                    .flag(flag)
                    .build();

            preferRepository.save(prefer);

            return true; // 좋아요 설정 상태 반환
        }
    }

    public boolean isPreferred(String boardFlag, Long postSeq, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));
        Flag flag = flagRepository.findByFlagBoardFlagAndFlagPostSeq(boardFlag, postSeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보를 찾을 수 없습니다."));

        // 좋아요 존재 여부 반환
        return preferRepository.existsByUserAndFlag(user, flag);
    }
}