package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Bookmark;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.BookmarkRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final FlagRepository flagRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleBookmark(String flagType, Long entitySeq, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보를 찾을 수 없습니다."));

        // 기존 북마크 존재 여부 확인
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserAndFlag(user, flag);

        if (existingBookmark.isPresent()) {
            // 북마크 해제 (삭제)
            bookmarkRepository.delete(existingBookmark.get());
            return false; // 북마크 해제 상태 반환
        } else {
            // 북마크 설정 (생성)
            Bookmark bookmark = Bookmark.builder()
                    .user(user)
                    .flag(flag)
                    .build();
            bookmarkRepository.save(bookmark);
            return true; // 북마크 설정 상태 반환
        }
    }

    public boolean isBookmarked(String flagType, Long entitySeq, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq)
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보를 찾을 수 없습니다."));

        // 북마크 존재 여부 반환
        return bookmarkRepository.existsByUserAndFlag(user, flag);
    }

    // 게시판 식별과 유저로 본인이 북마크한 해당 게시판 종류의 북마크들 찾기
    public List<BookmarkDTO> findByUserAndFlagType(User user, String flagType) {

        return bookmarkRepository.findByUserAndFlagType(user, flagType);
    }
}
