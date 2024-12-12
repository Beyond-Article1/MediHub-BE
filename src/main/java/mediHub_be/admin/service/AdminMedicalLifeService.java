package mediHub_be.admin.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminMedicalLifeCommentDTO;
import mediHub_be.admin.dto.AdminMedicalLifeDTO;
import mediHub_be.admin.dto.AdminMedicalLifeDetailDTO;
import mediHub_be.admin.dto.AdminMedicalLifePictureDTO;
import mediHub_be.board.entity.Comment;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.CommentRepository;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.medical_life.entity.MedicalLife;
import mediHub_be.medical_life.repository.MedicalLifeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMedicalLifeService {

    private final MedicalLifeRepository medicalLifeRepository;
    private final FlagRepository flagRepository;
    private final PictureRepository pictureRepository;
    private final CommentRepository commentRepository;



    @Transactional(readOnly = true)
    public List<AdminMedicalLifeDTO> getAllMedicalLifePosts() {
        return medicalLifeRepository.findAll()
                .stream()
                .map(medicalLife -> {
                    Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("MEDICAL_LIFE", medicalLife.getMedicalLifeSeq())
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

                    List<AdminMedicalLifePictureDTO> pictures = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())
                            .stream()
                            .map(picture -> AdminMedicalLifePictureDTO.builder()
                                    .pictureSeq(picture.getPictureSeq())
                                    .pictureName(picture.getPictureName())
                                    .pictureUrl(picture.getPictureUrl())
                                    .pictureType(picture.getPictureType())
                                    .isDeleted(false)
                                    .build())
                            .collect(Collectors.toList());

                    return AdminMedicalLifeDTO.builder()
                            .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                            .userName(medicalLife.getUser().getUserName())
                            .deptName(medicalLife.getDept().getDeptName())
                            .partName(medicalLife.getPart().getPartName())
                            .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                            .medicalLifeContent(medicalLife.getMedicalLifeContent())
                            .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                            .isDeleted(medicalLife.getMedicalLifeIsDeleted())
                            .pictures(pictures)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminMedicalLifeDetailDTO getMedicalLifeDetail(Long medicalLifeSeq) {
        MedicalLife medicalLife = medicalLifeRepository.findById(medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE));

        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("MEDICAL_LIFE", medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        List<AdminMedicalLifePictureDTO> pictures = pictureRepository.findByFlag_FlagSeqAndDeletedAtIsNull(flag.getFlagSeq())
                .stream()
                .map(picture -> AdminMedicalLifePictureDTO.builder()
                        .pictureSeq(picture.getPictureSeq())
                        .pictureName(picture.getPictureName())
                        .pictureUrl(picture.getPictureUrl())
                        .pictureType(picture.getPictureType())
                        .isDeleted(false)
                        .build())
                .collect(Collectors.toList());

        return AdminMedicalLifeDetailDTO.builder()
                .medicalLifeSeq(medicalLife.getMedicalLifeSeq())
                .userName(medicalLife.getUser().getUserName())
                .deptName(medicalLife.getDept().getDeptName())
                .partName(medicalLife.getPart().getPartName())
                .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                .medicalLifeContent(medicalLife.getMedicalLifeContent())
                .medicalLifeViewCount(medicalLife.getMedicalLifeViewCount())
                .isDeleted(medicalLife.getMedicalLifeIsDeleted())
                .createdAt(medicalLife.getCreatedAt())
                .pictures(pictures)
                .build();
    }

    // 게시글 삭제
    @Transactional
    public void deleteMedicalLife(Long medicalLifeSeq) {
        MedicalLife medicalLife = medicalLifeRepository.findById(medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE));

        // 게시글 삭제 (소프트 딜리트)
        medicalLife.setDeleted();
        medicalLifeRepository.save(medicalLife);
    }

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<AdminMedicalLifeCommentDTO> getMedicalLifeComments(Long medicalLifeSeq) {
        Flag flag = flagRepository.findByFlagTypeAndFlagEntitySeq("MEDICAL_LIFE", medicalLifeSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FLAG));

        return commentRepository.findByFlag_FlagSeqAndCommentIsDeletedFalse(flag.getFlagSeq())
                .stream()
                .map(comment -> AdminMedicalLifeCommentDTO.builder()
                        .commentSeq(comment.getCommentSeq())
                        .userName(comment.getUser().getUserName())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 댓글 삭제
    @Transactional
    public void deleteMedicalLifeComment(Long commentSeq) {
        Comment comment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        // 댓글 삭제 (소프트 딜리트)
        comment.setDeleted();
        commentRepository.save(comment);
    }

}
