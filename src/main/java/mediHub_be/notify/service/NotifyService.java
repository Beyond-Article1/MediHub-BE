package mediHub_be.notify.service;

import mediHub_be.board.entity.Flag;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.user.entity.User;

import java.util.List;

public interface NotifyService {

    /**
     * 단일 리시버 (게시글에 대한 댓글 발생시 받는 유저는 게시글 작성자 한 명)
     */
    public void send(User sender, User receiver, Flag flag, NotiType notiType, String url);

    /** 다중 리시버
     * - 케이스 공유시 팔로워들에게 알림 발생
     * - 게시글 작성시 팔로워들에게 알림 발생
     */
    public void send(User sender, List<User> receivers, Flag flag, NotiType notiType, String url);
}
