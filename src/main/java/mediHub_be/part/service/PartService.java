package mediHub_be.part.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.dept.entity.Dept;
import mediHub_be.dept.repository.DeptRepository;
import mediHub_be.part.dto.PartDTO;
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

    @Transactional(readOnly = true)
    public List<PartDTO> getAllParts() {
        return partRepository.findAll()
                .stream()
                .map(part -> PartDTO.builder()
                        .partSeq(part.getPartSeq())
                        .deptSeq(part.getDept().getDeptSeq()) // Dept 객체에서 ID 추출
                        .partName(part.getPartName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public PartDTO createPart(PartDTO partDTO) {

        Dept dept = deptRepository.findById(partDTO.getDeptSeq())
                .orElseThrow(() -> new RuntimeException("Dept not found"));

        Part part = Part.builder()
                .dept(dept)
                .partName(partDTO.getPartName())
                .build();

        part = partRepository.save(part);

        return PartDTO.builder()
                .partSeq(part.getPartSeq())
                .deptSeq(part.getDept().getDeptSeq()) // Dept 객체에서 ID 추출
                .partName(part.getPartName())
                .build();
    }

    @Transactional
    public PartDTO updatePart(long partSeq, PartDTO partDTO) {
        Part part = partRepository.findById(partSeq)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        // Dept 객체 조회
        Dept dept = deptRepository.findById(partDTO.getDeptSeq())
                .orElseThrow(() -> new RuntimeException("Dept not found"));

        // Part 업데이트
        part.updatePart(dept, partDTO.getPartName());

        return PartDTO.builder()
                .partSeq(part.getPartSeq())
                .deptSeq(part.getDept().getDeptSeq()) // Dept 객체에서 ID 추출
                .partName(part.getPartName())
                .build();
    }

    @Transactional
    public void deletePart(long partSeq) {
        if (!partRepository.existsById(partSeq)) {
            throw new RuntimeException("Part not found");
        }
        partRepository.deleteById(partSeq);
    }
}
