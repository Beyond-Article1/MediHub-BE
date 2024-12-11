package mediHub_be.follow.service;

import mediHub_be.follow.dto.ResponseFollowingsDTO;

import java.util.List;

public interface FollowService {

    /**
     * User 팔로우 하기 (from이 to를 팔로우)
     * @param fromSeq
     * @param toSeq
     */
    public void follow(Long fromSeq, Long toSeq);

    /**
     * User 언팔로우 하기 (from이 to를 언팔로우)
     * @param fromSeq
     * @param toSeq
     */
    public void unfollow(Long fromSeq, Long toSeq);

    /**
     * 내 팔로잉 목록 확인
     */
    public List<ResponseFollowingsDTO> followings(Long fromSeq);

    /**
     * 내 팔로워 목록 확인
     */
    public Long followers(Long fromSeq);
}
