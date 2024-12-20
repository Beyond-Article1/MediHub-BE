package mediHub_be.medical_life.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.medical_life.dto.MedicalLifeFlagDTO;
import mediHub_be.medical_life.dto.MedicalLifeKeywordDTO;
import mediHub_be.medical_life.dto.MedicalLifeListDTO;
import mediHub_be.medical_life.repository.MedicalLifeRepository;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalLifeService {

    private final MedicalLifeRepository medicalLifeRepository;
    private final UserRepository userRepository;
    private final FlagRepository flagRepository;
    private final KeywordRepository keywordRepository;

    private static final String MEDICAL_LIFE_FLAG = "MEDICAL_LIFE";


    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<MedicalLifeListDTO> getMedicalLifeList(Long userSeq) {

        // 유저 존재 확인
        userRepository.findById(userSeq).orElseThrow(() -> new CustomException(ErrorCode.NEED_LOGIN));

        // 플래그 데이터 가져오기
        List<MedicalLifeFlagDTO> medicalLifeFlagDTOList = flagRepository.findAll().stream()
                .map(flag -> MedicalLifeFlagDTO.builder()
                        .flagSeq(flag.getFlagSeq())
                        .flagType(flag.getFlagType())
                        .flagEntitySeq(flag.getFlagEntitySeq())
                        .build())
                .toList();

        // 키워드 데이터 가져오기
        List<MedicalLifeKeywordDTO> medicalLifeKeywordDTOList = keywordRepository.findAll().stream()
                .map(keyword -> MedicalLifeKeywordDTO.builder()
                        .keywordSeq(keyword.getKeywordSeq())
                        .flagSeq(keyword.getFlagSeq())
                        .keywordName(keyword.getKeywordName())
                        .build())
                .toList();

        // 메디컬 라이프 게시물 조회 및 DTO 매핑
        List<MedicalLifeListDTO> medicalLifeListDTOList = medicalLifeRepository
                .findAllByMedicalLifeIsDeletedFalse().stream()
                .map(medicalLife -> {

                    // 해당 게시물에 연결된 플래그 조회
                    List<MedicalLifeFlagDTO> flagsForMedicalLife = medicalLifeFlagDTOList.stream()
                            .filter(flag -> flag.getFlagType().equals(MEDICAL_LIFE_FLAG))
                            .filter(flag -> flag.getFlagEntitySeq() == medicalLife.getMedicalLifeSeq())
                            .toList();

                    // 플래그를 통해 해당 게시물의 키워드 조회
                    List<MedicalLifeKeywordDTO> keywordsForFlag = medicalLifeKeywordDTOList.stream()
                            .filter(keyword -> flagsForMedicalLife.stream()
                                    .anyMatch(flag -> Objects.equals(flag.getFlagSeq(), keyword.getFlagSeq())))
                            .toList();

                    // DTO 빌드
                    return MedicalLifeListDTO.builder()
                            .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                            .userSeq(medicalLife.getUser().getUserSeq())
                            .userName(medicalLife.getUser().getUserName())
                            .PartSeq(medicalLife.getPart().getPartName())
                            .DeptSeq(medicalLife.getDept().getDeptName())
                            .medicalLifeName(medicalLife.getUser().getUserName())
                            .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                            .medicalLifeContent(medicalLife.getMedicalLifeContent())
                            .medicalLifeIsDeleted(medicalLife.getMedicalLifeIsDeleted())
                            .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                            .build();
                })
                .collect(toList());

        return medicalLifeListDTOList;
    }
}
