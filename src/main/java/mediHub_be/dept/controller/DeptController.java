package mediHub_be.dept.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.dept.dto.DeptDTO;
import mediHub_be.dept.service.DeptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dept")
@RequiredArgsConstructor
@Tag(name = "부서", description = "부서 API")
public class DeptController {

    private final DeptService deptService;

    @Operation(summary = "부서 조회", description = "부서 조회" )
    @GetMapping
    public ResponseEntity<ApiResponse<List<DeptDTO>>> getAllDept() {
        List<DeptDTO> dept = deptService.getAllDepts();
        return ResponseEntity.ok(ApiResponse.ok(dept));
    }

    @Operation(summary = "부서 등록", description = "부서 등록" )
    @PostMapping
    public ResponseEntity<ApiResponse<DeptDTO>> createDept(@RequestBody DeptDTO deptDTO) {
        DeptDTO createdDept = deptService.createDept(deptDTO);
        return ResponseEntity.ok(ApiResponse.ok(createdDept));
    }

    @Operation(summary = "부서 수정", description = "부서 수정" )
    @PutMapping("/{deptSeq}")
    public ResponseEntity<ApiResponse<DeptDTO>> updateDept(@PathVariable Long deptSeq, @RequestBody DeptDTO deptDTO) {
        DeptDTO updatedDept = deptService.updateDept(deptSeq, deptDTO);
        return ResponseEntity.ok(ApiResponse.ok(updatedDept));
    }

    @Operation(summary = "부서 삭제", description = "부서 삭제" )
    @DeleteMapping("/{deptSeq}")
    public ResponseEntity<ApiResponse<Long>> deleteDept(@PathVariable Long deptSeq) {
        deptService.deleteDept(deptSeq);
        return ResponseEntity.ok(ApiResponse.ok(deptSeq));
    }
}
