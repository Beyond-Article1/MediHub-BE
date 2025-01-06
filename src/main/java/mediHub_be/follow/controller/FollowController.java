package mediHub_be.follow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.follow.dto.ResponseFollowingsDTO;
import mediHub_be.follow.service.FollowService;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Tag(name = "팔로잉과 팔로워")
public class FollowController {
    
    private final FollowService followService;

    // 내 팔로잉 목록 확인
    @Operation(summary = "내 팔로잉 목록 확인", description = "내가 팔로우 한 사용자들 확인")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponseFollowingsDTO>>> followings(){
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        return ResponseEntity.ok(
                ApiResponse.ok(followService.followings(currentUserSeq))
        );
    }
    
    // 내 팔로워 인원 확인
    @Operation(summary = "내 팔로워 인원 확인", description = "나를 팔로우 한 사용자들 몇명인지 확인")
    @GetMapping("/followers")
    public ResponseEntity<ApiResponse<Long>> getFollowers(){
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        return ResponseEntity.ok(
                ApiResponse.ok(followService.followers(currentUserSeq))
        );
    }

    // 팔로우 하기
    @Operation(summary = "팔로우", description = "팔로우 하기")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> follow(@RequestParam Long toUserSeq){
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        followService.follow(currentUserSeq, toUserSeq);

        return ResponseEntity.ok(ApiResponse.created("OK"));
    }

    // 팔로우 삭제하기
    @Operation(summary = "언팔로우", description = "팔로우 삭제하기")
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>>deleteFollow(@RequestParam Long toUserSeq){
        Long currentUserSeq = SecurityUtil.getCurrentUserSeq();

        followService.unfollow(currentUserSeq, toUserSeq);

        return ResponseEntity.ok(ApiResponse.ok("OK"));
    }
}
