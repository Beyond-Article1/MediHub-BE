package mediHub_be.part.service;

import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    public List<PartDTO> getAllParts() {
        return partRepository.findAll()
                .stream()
                .map(part -> PartDTO.builder()
                        .partSeq(part.getPartSeq())
                        .deptSeq(part.getDeptSeq())
                        .partName(part.getPartName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public PartDTO createPart(PartDTO partDTO) {
        Part part = Part.builder()
                .deptSeq(partDTO.getDeptSeq())
                .partName(partDTO.getPartName())
                .build();

        part = partRepository.save(part);

        return PartDTO.builder()
                .partSeq(part.getPartSeq())
                .deptSeq(part.getDeptSeq())
                .partName(part.getPartName())
                .build();
    }

    @Transactional
    public PartDTO updatePart(long partSeq, PartDTO partDTO) {
        Part part = partRepository.findById(partSeq)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        part.updatePart(partDTO.getDeptSeq(), partDTO.getPartName());

        return PartDTO.builder()
                .partSeq(part.getPartSeq())
                .deptSeq(part.getDeptSeq())
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
