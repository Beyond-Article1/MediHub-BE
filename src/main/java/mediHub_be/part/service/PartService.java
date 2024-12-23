package mediHub_be.part.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.dept.entity.Dept;
import mediHub_be.dept.repository.DeptRepository;
import mediHub_be.part.dto.PartDTO;
import mediHub_be.part.dto.PartRequestDTO;
import mediHub_be.part.entity.Part;
import mediHub_be.part.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;
    private final DeptRepository deptRepository;

    // 과 등록
    @Transactional(readOnly = true)
    public List<PartDTO> getAllParts() {
        return partRepository.findByDeletedAtIsNull()
                .stream()
                .map(part -> PartDTO.builder()
                        .partSeq(part.getPartSeq())
                        .deptSeq(part.getDept().getDeptSeq())
                        .partName(part.getPartName())
                        .build())
                .collect(Collectors.toList());
    }

    // 과 등록
    @Transactional
    public PartDTO createPart(PartRequestDTO partRequestDTO) {
        Dept dept = deptRepository.findById(partRequestDTO.getDeptSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PART));

        Part part = Part.builder()
                .dept(dept)
                .partName(partRequestDTO.getPartName())
                .build();

        Part savedPart = partRepository.save(part);

        return PartDTO.builder()
                .partSeq(savedPart.getPartSeq())
                .deptSeq(savedPart.getDept().getDeptSeq())
                .partName(savedPart.getPartName())
                .build();
    }

    // 과 수정
    @Transactional
    public PartDTO updatePart(PartDTO partDTO) {
        Part part = partRepository.findById(partDTO.getPartSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PART));

        Dept dept = deptRepository.findById(partDTO.getDeptSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PART));

        part.updatePart(dept, partDTO.getPartName());

        return PartDTO.builder()
                .partSeq(part.getPartSeq())
                .deptSeq(part.getDept().getDeptSeq())
                .partName(part.getPartName())
                .build();
    }

    // 과 삭제
    @Transactional
    public void deletePart(long partSeq) {
        if (!partRepository.existsById(partSeq)) {
            throw new CustomException(ErrorCode.NOT_FOUND_PART);
        }
        partRepository.deleteById(partSeq);
    }

    @Transactional(readOnly = true)
    public List<PartDTO> getAllPartsByDept(Long deptSeq) {
        return partRepository.findByDept_DeptSeqOrderByPartName(deptSeq)
                .stream()
                .map(part -> PartDTO.builder()
                        .partSeq(part.getPartSeq())
                        .partName(part.getPartName())
                        .build())
                .collect(Collectors.toList());

    }
}
