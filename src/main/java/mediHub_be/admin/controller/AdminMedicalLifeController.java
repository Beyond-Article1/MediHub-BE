//package mediHub_be.admin.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import mediHub_be.admin.dto.AdminMedicalLifeCommentDTO;
//import mediHub_be.admin.dto.AdminMedicalLifeDTO;
//import mediHub_be.admin.dto.AdminMedicalLifeDetailDTO;
//import mediHub_be.admin.service.AdminMedicalLifeService;
//import mediHub_be.common.response.ApiResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Tag(name = "관리자", description = "관리자 API")
//@RestController
//@RequestMapping("/api/v1/admin/medical-life")
//@RequiredArgsConstructor
//public class AdminMedicalLifeController {
//
//    private final AdminMedicalLifeService adminMedicalLifeService;
//
//    @Operation(summary = "모든 메디컬 라이프 게시글 조회", description = "관리자가 모든 게시글을 조회합니다.")
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<AdminMedicalLifeDTO>>> getAllMedicalLifePosts() {
//        List<AdminMedicalLifeDTO> posts = adminMedicalLifeService.getAllMedicalLifePosts();
//        return ResponseEntity.ok(ApiResponse.ok(posts));
//    }
//
//    @Operation(summary = "메디컬 라이프 게시글 상세 조회", description = "관리자가 특정 게시글의 상세 정보를 조회합니다.")
//    @GetMapping("/{medicalLifeSeq}")
//    public ResponseEntity<ApiResponse<AdminMedicalLifeDetailDTO>> getMedicalLifeDetail(@PathVariable Long medicalLifeSeq) {
//        AdminMedicalLifeDetailDTO postDetail = adminMedicalLifeService.getMedicalLifeDetail(medicalLifeSeq);
//        return ResponseEntity.ok(ApiResponse.ok(postDetail));
//    }
//
//    @Operation(summary = "메디컬 라이프 게시글 삭제", description = "관리자가 특정 게시글을 삭제합니다.")
//    @DeleteMapping("/{medicalLifeSeq}")
//    public ResponseEntity<ApiResponse<Void>> deleteMedicalLife(@PathVariable Long medicalLifeSeq) {
//        adminMedicalLifeService.deleteMedicalLife(medicalLifeSeq);
//        return ResponseEntity.noContent().build();
//    }
//
//    @Operation(summary = "특정 게시글의 댓글 목록 조회", description = "관리자가 특정 게시글의 댓글 목록을 조회합니다.")
//    @GetMapping("/{medicalLifeSeq}/comments")
//    public ResponseEntity<ApiResponse<List<AdminMedicalLifeCommentDTO>>> getMedicalLifeComments(@PathVariable Long medicalLifeSeq) {
//        List<AdminMedicalLifeCommentDTO> comments = adminMedicalLifeService.getMedicalLifeComments(medicalLifeSeq);
//        return ResponseEntity.ok(ApiResponse.ok(comments));
//    }
//
//    @Operation(summary = "특정 댓글 삭제", description = "관리자가 특정 댓글을 삭제합니다.")
//    @DeleteMapping("/comments/{commentSeq}")
//    public ResponseEntity<ApiResponse<Void>> deleteMedicalLifeComment(@PathVariable Long commentSeq) {
//        adminMedicalLifeService.deleteMedicalLifeComment(commentSeq);
//        return ResponseEntity.noContent().build();
//    }
//}
