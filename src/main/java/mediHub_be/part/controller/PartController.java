package mediHub_be.part.controller;

import io.swagger.v3.oas.annotations.Operation;
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
public class PartController {

    private final PartService partService;

    @GetMapping
    public ResponseEntity<List<PartDTO>> getAllParts() {
        List<PartDTO> parts = partService.getAllParts();
        return ResponseEntity.ok(parts);
    }

    @GetMapping("/{deptSeq}")
    public ResponseEntity<List<PartDTO>> getAllPartsByDept(@PathVariable Long deptSeq) {
        List<PartDTO> parts = partService.getAllPartsByDept(deptSeq);
        return ResponseEntity.ok(parts);
    }

    @Operation(summary = "새 파트 생성", description = "새로운 파트를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<PartDTO>> createPart(@RequestBody PartRequestDTO partRequestDTO) {
        PartDTO createdPart = partService.createPart(partRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(createdPart));
    }

    @Operation(summary = "파트 수정", description = "파트 정보를 수정합니다.")
    @PutMapping("/{partSeq}")
    public ResponseEntity<ApiResponse<PartDTO>> updatePart(@RequestBody PartDTO partDTO) {
        PartDTO updatedPart = partService.updatePart(partDTO);
        return ResponseEntity.ok(ApiResponse.ok(updatedPart));
    }

    @Operation(summary = "파트 삭제", description = "파트 정보를 삭제합니다.")
    @DeleteMapping("/{partSeq}")
    public ResponseEntity<ApiResponse<Void>> deletePart(@PathVariable long partSeq) {
        partService.deletePart(partSeq);
        return ResponseEntity.noContent().build();
    }
}
