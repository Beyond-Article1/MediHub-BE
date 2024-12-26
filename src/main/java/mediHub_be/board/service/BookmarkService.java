package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Bookmark;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.BookmarkRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final FlagRepository flagRepository;
    private final UserRepository userRepository;
    private final FlagService flagService;

    @Transactional
    public boolean toggleBookmark(String flagType, Long entitySeq, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));
        // 기존 북마크 존재 여부 확인
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserAndFlag(user, flag);

        if (existingBookmark.isPresent()) {

            // 북마크 해제 (삭제)
            bookmarkRepository.delete(existingBookmark.get());

            // 북마크 해제 상태 반환
            return false;
        } else {

            // 북마크 설정 (생성)
            Bookmark bookmark = Bookmark.builder()
                    .user(user)
                    .flag(flag)
                    .build();

            bookmarkRepository.save(bookmark);

            // 북마크 설정 상태 반환
            return true;
        }
    }

    public boolean isBookmarked(String flagType, Long entitySeq, String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 북마크 존재 여부 반환
        return bookmarkRepository.existsByUserAndFlag(user, flag);
    }

    // 직원과 식별 번호로 본인이 북마크 한 해당 게시판 종류 북마크 찾기
    public List<BookmarkDTO> findByUserAndFlagType(User user, String flagType) {

        return bookmarkRepository.findByUserAndFlagType(user, flagType);
    }

    // 게시글이 삭제되었을 때, 해당 게시글과 연결된 북마크 삭제
    public void deleteBookmarkByFlag(Flag flag) {

        bookmarkRepository.deleteAllByFlagSeq(flag.getFlagSeq());
    }

    // 식별 번호를 찾아서 게시글이 삭제되었을 때, 해당 게시글과 연결된 북마크 삭제
    public void deleteBookmarkByFlag(String flagType, Long entitySeq) {

        // 1. flag 조회
        Flag flag = flagService.findFlag(flagType, entitySeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        // 2. 북마크 삭제
        deleteBookmarkByFlag(flag);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersWhoBookmarkedCaseSharing(Long caseSharingSeq) {
        List<Bookmark> bookmarks = bookmarkRepository.findByFlag_FlagTypeAndFlag_FlagEntitySeq("case_sharing", caseSharingSeq);
        return bookmarks.stream()
                .map(Bookmark::getUser) // Bookmark에서 User를 추출
                .collect(Collectors.toList());
    }
}