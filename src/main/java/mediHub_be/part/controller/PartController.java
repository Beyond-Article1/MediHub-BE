package mediHub_be.part.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.part.dto.PartDTO;
import mediHub_be.part.dto.PartRequestDTO;
import mediHub_be.part.service.PartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/part")
@RequiredArgsConstructor
@Tag(name = "과", description = "과 API")
public class PartController {

    private final PartService partService;

    @Operation(summary = "과 조회", description = "과 조회" )
    @GetMapping
    public ResponseEntity<ApiResponse<List<PartDTO>>> getAllParts() {
        List<PartDTO> parts = partService.getAllParts();
        return ResponseEntity.ok(ApiResponse.ok(parts));
    }

    @Operation(summary = "부서에 따른 과 조회", description = "부서에 따른 과 조회" )
    @GetMapping("/{deptSeq}")
    public ResponseEntity<ApiResponse<List<PartDTO>>> getAllPartsByDept(@PathVariable Long deptSeq) {
        List<PartDTO> parts = partService.getAllPartsByDept(deptSeq);
        return ResponseEntity.ok(ApiResponse.ok(parts));
    }

    @Operation(summary = "과 등록", description = "과 등록" )
    @PostMapping
    public ResponseEntity<ApiResponse<PartDTO>> createPart(@RequestBody PartRequestDTO partRequestDTO) {
        PartDTO createdPart = partService.createPart(partRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(createdPart));
    }

    @Operation(summary = "과 수정", description = "과 수정" )
    @PutMapping("/{partSeq}")
    public ResponseEntity<ApiResponse<PartDTO>> updatePart(@RequestBody PartDTO partDTO) {
        PartDTO updatedPart = partService.updatePart(partDTO);
        return ResponseEntity.ok(ApiResponse.ok(updatedPart));
    }

    @Operation(summary = "과 삭제", description = "과 삭제" )
    @DeleteMapping("/{partSeq}")
    public ResponseEntity<ApiResponse<Long>> deletePart(@PathVariable long partSeq) {
        partService.deletePart(partSeq);
        return ResponseEntity.ok(ApiResponse.ok(partSeq));
    }
}
