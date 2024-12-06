package mediHub_be.dept.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.dept.dto.DeptDTO;
import mediHub_be.dept.service.DeptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dept")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @GetMapping
    public ResponseEntity<List<DeptDTO>> getAllDepts() {
        List<DeptDTO> depts = deptService.getAllDepts();
        return ResponseEntity.ok(depts);
    }

    @PostMapping
    public ResponseEntity<DeptDTO> createDept(@RequestBody DeptDTO deptDTO) {
        DeptDTO createdDept = deptService.createDept(deptDTO);
        return ResponseEntity.ok(createdDept);
    }

    @PutMapping("/{deptSeq}")
    public ResponseEntity<DeptDTO> updateDept(@PathVariable Long deptSeq, @RequestBody DeptDTO deptDTO) {
        DeptDTO updatedDept = deptService.updateDept(deptSeq, deptDTO);
        return ResponseEntity.ok(updatedDept);
    }

    @DeleteMapping("/{deptSeq}")
    public ResponseEntity<Void> deleteDept(@PathVariable Long deptSeq) {
        deptService.deleteDept(deptSeq);
        return ResponseEntity.noContent().build();
    }
}
