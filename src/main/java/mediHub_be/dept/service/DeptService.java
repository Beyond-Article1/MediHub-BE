package mediHub_be.dept.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.dept.dto.DeptDTO;
import mediHub_be.dept.entity.Dept;
import mediHub_be.dept.repository.DeptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;


    @Transactional(readOnly = true)
    public List<DeptDTO> getAllDepts() {
        return deptRepository.findAll()
                .stream()
                .map(dept -> DeptDTO.builder()
                        .deptSeq(dept.getDeptSeq())
                        .deptName(dept.getDeptName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public DeptDTO createDept(DeptDTO deptDTO) {
        Dept dept = Dept.builder()
                .deptName(deptDTO.getDeptName())
                .build();
        dept = deptRepository.save(dept);
        return DeptDTO.builder()
                .deptSeq(dept.getDeptSeq())
                .deptName(dept.getDeptName())
                .build();
    }

    @Transactional
    public DeptDTO updateDept(Long deptSeq, DeptDTO deptDTO) {
        Dept dept = deptRepository.findById(deptSeq)
                .orElseThrow(() -> new RuntimeException("Dept not found"));

        dept.updateDept(deptDTO.getDeptName());

        return DeptDTO.builder()
                .deptSeq(dept.getDeptSeq())
                .deptName(dept.getDeptName())
                .build();
    }

    @Transactional
    public void deleteDept(Long deptSeq) {
        if (!deptRepository.existsById(deptSeq)) {
            throw new RuntimeException("Dept not found");
        }
        deptRepository.deleteById(deptSeq);
    }
}
