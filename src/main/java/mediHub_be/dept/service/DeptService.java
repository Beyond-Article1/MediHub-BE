package mediHub_be.dept.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
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

    // 부서 조회
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

    // 부서 생성
    @Transactional
    public DeptDTO createDept(DeptDTO deptDTO) {
        Dept dept = Dept.builder()
                .deptName(deptDTO.getDeptName())
                .build();

        Dept savedDept = deptRepository.save(dept);

        return DeptDTO.builder()
                .deptSeq(savedDept.getDeptSeq())
                .deptName(savedDept.getDeptName())
                .build();
    }

    // 부서 수정
    @Transactional
    public DeptDTO updateDept(Long deptSeq, DeptDTO deptDTO) {
        Dept dept = deptRepository.findById(deptSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PART));

        dept.updateDept(deptDTO.getDeptName());

        return DeptDTO.builder()
                .deptSeq(dept.getDeptSeq())
                .deptName(dept.getDeptName())
                .build();
    }

    // 부서 삭제
    @Transactional
    public void deleteDept(Long deptSeq) {
        if (!deptRepository.existsById(deptSeq)) {
            throw new CustomException(ErrorCode.NOT_FOUND_PART);
        }
        deptRepository.deleteById(deptSeq);
    }
}
