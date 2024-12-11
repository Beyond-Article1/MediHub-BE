package mediHub_be.follow.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.follow.dto.ResponseFollowingsDTO;
import mediHub_be.follow.entity.Follow;
import mediHub_be.follow.repository.FollowRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    // 팔로우 from이 to를
    @Override
    @Transactional
    public void follow(Long fromSeq, Long toSeq) {
        User fromUser = userService.findUser(fromSeq);
        User toUser = userService.findUser(toSeq);

        // 팔로우 생성
        followRepository.save(new Follow(fromUser, toUser));
    }

    // 언팔로우 from이 to를
    @Override
    @Transactional
    public void unfollow(Long fromSeq, Long toSeq) {
        User fromUser = userService.findUser(fromSeq);
        User toUser = userService.findUser(toSeq);

        // 팔로우가 존재하는지 확인
        Follow follow = followRepository.findByUserFromAndUserTo(fromUser, toUser)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FOLLOW));

        // 팔로우 삭제
        followRepository.delete(follow);
    }

    // 팔로잉 목록 확인
    @Override
    public List<ResponseFollowingsDTO> followings(Long fromSeq) {
        User fromUser = userService.findUser(fromSeq);

        return followRepository.findByFollowings(fromUser);
    }

    // 팔로워 목록 확인
    @Override
    public Long followers(Long toSeq) {
        User toUser = userService.findUser(toSeq);

        return followRepository.countByToUser(toUser);
    }

    // 팔로워들 User로 뽑기 (notify 생성용)
    public List<User> userByFollowers(Long toSeq) {
        User toUser = userService.findUser(toSeq);

        return followRepository.findUserFromByToUser(toUser);
    }

}
