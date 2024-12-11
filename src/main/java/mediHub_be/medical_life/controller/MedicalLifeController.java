package mediHub_be.medical_life.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.medical_life.dto.DeptPartFilterDTO;
import mediHub_be.medical_life.dto.MedicalLifeDTO;
import mediHub_be.medical_life.service.MedicalLifeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "메디컬 라이프", description = "메디컬 라이프 API")
@RestController
@RequestMapping("/api/v1/medical-life")
@RequiredArgsConstructor
public class MedicalLifeController {

    private final MedicalLifeService medicalLifeService;

    @GetMapping
    public List<MedicalLifeDTO> getMedicalLifeList(
            @RequestParam(required = false) Long deptSeq,
            @RequestParam(required = false) Long partSeq
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        DeptPartFilterDTO filterDTO = new DeptPartFilterDTO();
        filterDTO.setDeptSeq(deptSeq);
        filterDTO.setPartSeq(partSeq);

        return medicalLifeService.getMedicalLifeListByUsername(username, filterDTO);
    }
}
