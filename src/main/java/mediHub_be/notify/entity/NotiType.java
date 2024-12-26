package mediHub_be.notify.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotiType {
    BOARD("님의 새로운 게시글"),  // 팔로우 한 사람의 새로운 게시글
    COMMENT("님이 댓글을 달았습니다"),    // 내 게시글에 대한 댓글
    CASE("님의 새로운 케이스 공유"),    // 팔로우 한 사람의 새로운 케이스공유 글
    NEWVERSION("해당 케이스 공유의 새로운 버전");
    private final String message;
}