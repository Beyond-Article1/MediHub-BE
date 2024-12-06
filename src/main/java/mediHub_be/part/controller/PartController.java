package mediHub_be.part.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.part.dto.PartDTO;
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

    @PostMapping
    public ResponseEntity<PartDTO> createPart(@RequestBody PartDTO partDTO) {
        PartDTO createdPart = partService.createPart(partDTO);
        return ResponseEntity.ok(createdPart);
    }

    @PutMapping("/{partSeq}")
    public ResponseEntity<PartDTO> updatePart(@PathVariable long partSeq, @RequestBody PartDTO partDTO) {
        PartDTO updatedPart = partService.updatePart(partSeq, partDTO);
        return ResponseEntity.ok(updatedPart);
    }

    @DeleteMapping("/{partSeq}")
    public ResponseEntity<Void> deletePart(@PathVariable long partSeq) {
        partService.deletePart(partSeq);
        return ResponseEntity.noContent().build();
    }
}
