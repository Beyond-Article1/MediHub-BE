package mediHub_be.medical_life.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.medical_life.dto.DeptPartFilterDTO;
import mediHub_be.medical_life.dto.MedicalLifeDTO;
import mediHub_be.medical_life.repository.MedicalLifeRepository;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalLifeService {

    private final MedicalLifeRepository medicalLifeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MedicalLifeDTO> getMedicalLifeListByUsername(String username, DeptPartFilterDTO filterDTO) {
        
        // 게시판 조회 (부서와 과 필터링)
        return medicalLifeRepository.findAllByDeptAndPart(filterDTO.getDeptSeq(), filterDTO.getPartSeq())
                .stream()
                .map(medicalLife -> MedicalLifeDTO.builder()
                        .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                        .userName(medicalLife.getUser().getUserName())
                        .deptName(medicalLife.getDept().getDeptName())
                        .partName(medicalLife.getPart().getPartName())
                        .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                        .medicalLifeContent(medicalLife.getMedicalLifeContent())
                        .medicalLifeIsDeleted(medicalLife.getMedicalLifeIsDeleted())
                        .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                        .build())
                .collect(Collectors.toList());
    }

}
