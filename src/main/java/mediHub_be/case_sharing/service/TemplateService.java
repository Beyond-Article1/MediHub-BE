package mediHub_be.case_sharing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.case_sharing.dto.TemplateDetailDTO;
import mediHub_be.case_sharing.dto.TemplateListDTO;
import mediHub_be.case_sharing.dto.TemplateRequestDTO;
import mediHub_be.case_sharing.entity.OpenScope;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.case_sharing.repository.TemplateRepository;
import mediHub_be.part.entity.Part;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final FlagRepository flagRepository;

    @Transactional(readOnly = true)
    public List<TemplateListDTO> getAllTemplates(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        return templateRepository.findByDeletedAtIsNull().stream() // 삭제되지 않은 템플릿만 조회
                .filter(template -> {
                    // 공개 범위에 따른 필터링
                    return switch (template.getOpenScope()) {
                        case PRIVATE ->
                            // PRIVATE 템플릿은 작성자 본인만 조회 가능
                                template.getUser().equals(user);
                        case CLASS_OPEN ->
                            // CLASS_OPEN 템플릿은 같은 부서(dept)의 회원만 조회 가능
                                template.getPart() != null
                                        && user.getPart() != null
                                        && template.getPart().equals(user.getPart());
                        case PUBLIC ->
                            // PUBLIC 템플릿은 모두 조회 가능
                                true;
                        default -> false;
                    };
                })
                .map(template -> {
                    // Flag 조회
                    Optional<Flag> flagOptional = flagRepository.findByFlagBoardFlagAndFlagPostSeq("template_preview", template.getTemplateSeq());
                    String previewImageUrl = null;

                    if (flagOptional.isPresent()) {
                        // Picture 조회
                        Optional<Picture> pictureOptional = pictureRepository.findByFlag_FlagSeq(flagOptional.get().getFlagSeq());
                        if (pictureOptional.isPresent()) {
                            previewImageUrl = pictureOptional.get().getPictureUrl();
                        }
                    }

                    // TemplateListDTO 생성
                    return TemplateListDTO.builder()
                            .templateSeq(template.getTemplateSeq())
                            .templateTitle(template.getTemplateTitle())
                            .previewImageUrl(previewImageUrl) // 미리보기 이미지 URL
                            .build();
                })
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public TemplateDetailDTO getTemplateDetail(Long templateSeq) {
        Template template = templateRepository.findByTemplateSeqAndDeletedAtIsNull(templateSeq) // 삭제되지 않은 템플릿만 조회
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다."));

        Part part = template.getPart();

        return TemplateDetailDTO.builder()
                .templateSeq(template.getTemplateSeq())
                .templateTitle(template.getTemplateTitle())
                .templateContent(template.getTemplateContent())
                .openScope(template.getOpenScope().name())
                .partName(part != null ? part.getPartName() : null)
                .build();
    }


    @Transactional(readOnly = true)
    public List<TemplateListDTO> getTemplatesByFilter(String userId, String filter) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        List<Template> filteredTemplates = switch (filter.toLowerCase()) {
            case "my" -> // 내가 작성한 템플릿
                    templateRepository.findByUser_UserSeqAndDeletedAtIsNull(user.getUserSeq());
            case "shared" -> // 과에서 공유된 템플릿
                    templateRepository.findByPart_PartSeqAndOpenScopeAndDeletedAtIsNull(user.getPart().getPartSeq(), OpenScope.CLASS_OPEN);
            case "public" -> // 전체 공개 템플릿
                    templateRepository.findByOpenScopeAndDeletedAtIsNull(OpenScope.PUBLIC);
            default -> throw new IllegalArgumentException("잘못된 필터 값입니다. (my, shared, public 중 선택)");
        };

        // TemplateListDTO 변환
        return filteredTemplates.stream()
                .map(template -> {
                    // Flag 조회
                    Optional<Flag> flagOptional = flagRepository.findByFlagBoardFlagAndFlagPostSeq("template_preview", template.getTemplateSeq());
                    String previewImageUrl = null;

                    if (flagOptional.isPresent()) {
                        // Picture 조회
                        Optional<Picture> pictureOptional = pictureRepository.findByFlag_FlagSeq(flagOptional.get().getFlagSeq());
                        if (pictureOptional.isPresent()) {
                            previewImageUrl = pictureOptional.get().getPictureUrl();
                        }
                    }
                    return TemplateListDTO.builder()
                            .templateSeq(template.getTemplateSeq())
                            .templateTitle(template.getTemplateTitle())
                            .previewImageUrl(previewImageUrl) // 미리보기 이미지 URL 포함
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public Long createTemplate(String userId, TemplateRequestDTO requestDTO) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        Part part = user.getPart();
        if (part == null) {
            throw new IllegalArgumentException("회원의 부서 정보가 없습니다.");
        }

        Template template = Template.builder()
                .user(user)
                .part(user.getPart())
                .templateTitle(requestDTO.getTemplateTitle())
                .templateContent(requestDTO.getTemplateContent())
                .openScope(OpenScope.valueOf(requestDTO.getOpenScope().toUpperCase()))
                .build();

        templateRepository.save(template);

        // Flag와 Picture 저장 (미리보기 이미지)
        if (requestDTO.getPreviewImageUrl() != null) {
            Flag flag = Flag.builder()
                    .flagBoardFlag("template_preview")
                    .flagPostSeq(template.getTemplateSeq())
                    .build();

            flagRepository.save(flag);

            Picture picture = Picture.builder()
                    .flag(flag)
                    .pictureName(template.getTemplateTitle() + "_preview")
                    .pictureUrl(requestDTO.getPreviewImageUrl())
                    .build();

            pictureRepository.save(picture);
        }

        return template.getTemplateSeq();
    }

    @Transactional
    public void updateTemplate(String userId, Long templateSeq, TemplateRequestDTO requestDTO) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        Template template = templateRepository.findById(templateSeq)
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다."));

        if (!template.getUser().equals(user)) {
            throw new IllegalArgumentException("본인이 작성한 템플릿만 수정할 수 있습니다.");
        }

        template.updateTemplate(requestDTO.getTemplateTitle(),
                requestDTO.getTemplateContent(),
                OpenScope.valueOf(requestDTO.getOpenScope().toUpperCase()));
        if (requestDTO.getPreviewImageUrl() != null) {
            // 5.1 기존 Flag 조회
            Flag flag = flagRepository.findByFlagBoardFlagAndFlagPostSeq("template_preview", templateSeq)
                    .orElseThrow(() -> new IllegalArgumentException("해당 템플릿에 연결된 Flag가 없습니다."));

            // 5.2 기존 Picture 삭제
            pictureRepository.deleteByFlag(flag);

            // 5.3 새 Picture 생성 및 저장
            Picture newPicture = Picture.builder()
                    .flag(flag)
                    .pictureName(template.getTemplateTitle() + "_preview")
                    .pictureUrl(requestDTO.getPreviewImageUrl())
                    .build();
            pictureRepository.save(newPicture);
        }
    }

    @Transactional
    public void deleteTemplate(String userId, Long templateSeq) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        Template template = templateRepository.findById(templateSeq)
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다."));

        if (!template.getUser().equals(user)) {
            throw new IllegalArgumentException("본인이 작성한 템플릿만 삭제할 수 있습니다.");
        }

        template.markAsDeleted();
        templateRepository.save(template);

        // Flag 조회
        Optional<Flag> flagOptional = flagRepository.findByFlagBoardFlagAndFlagPostSeq("template_preview", template.getTemplateSeq());

        // Flag가 존재하면 연결된 Picture 조회 및 삭제 처리
        flagOptional.ifPresent(flag -> {
            Optional<Picture> pictureOptional = pictureRepository.findByFlag_FlagSeq(flag.getFlagSeq());

            // Picture 존재 시 삭제 처리
            pictureOptional.ifPresent(picture -> {
                picture.markAsDeleted();
                pictureRepository.save(picture);
            });
        });
    }
}

