package mediHub_be.case_sharing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.board.service.PictureService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final FlagRepository flagRepository;
    private final PictureService pictureService;
    private final AmazonS3Service amazonS3Service;

    private static final String TEMPLATE_FLAG = "template";
    private static final String TEMPLATE_PREVIEW_FLAG = "template_preview";

    // 회원이 볼 수 있는 템플릿 전체 조회
    @Transactional(readOnly = true)
    public List<TemplateListDTO> getAllTemplates(String userId) {
        User user = getUser(userId);

        return templateRepository.findByDeletedAtIsNull().stream()
                .filter(template -> isTemplateAccessible(template, user))
                .map(this::toTemplateListDTO)
                .collect(Collectors.toList());
    }

    // 조건별 템플릿 조회
    @Transactional(readOnly = true)
    public List<TemplateListDTO> getTemplatesByFilter(String userId, String filter) {
        User user = getUser(userId);

        List<Template> filteredTemplates = switch (filter.toLowerCase()) {
            case "my" -> templateRepository.findByUser_UserSeqAndDeletedAtIsNull(user.getUserSeq());
            case "shared" -> templateRepository.findByPart_PartSeqAndOpenScopeAndDeletedAtIsNull(
                    user.getPart().getPartSeq(), OpenScope.CLASS_OPEN);
            case "public" -> templateRepository.findByOpenScopeAndDeletedAtIsNull(OpenScope.PUBLIC);
            default -> throw new IllegalArgumentException("잘못된 필터 값입니다. (my, shared, public 중 선택)");
        };

        return filteredTemplates.stream()
                .map(this::toTemplateListDTO)
                .collect(Collectors.toList());
    }

    // 특정 템플릿 상세 조회
    @Transactional(readOnly = true)
    public TemplateDetailDTO getTemplateDetail(Long templateSeq) {
        Template template = getTemplate(templateSeq);
        Part part = template.getPart();

        return TemplateDetailDTO.builder()
                .templateSeq(template.getTemplateSeq())
                .templateTitle(template.getTemplateTitle())
                .templateContent(template.getTemplateContent())
                .openScope(template.getOpenScope().name())
                .partName(part != null ? part.getPartName() : null)
                .build();
    }

    // 템플릿 등록
    @Transactional
    public Long createTemplate(String userId, List<MultipartFile> images, MultipartFile previewImage, TemplateRequestDTO requestDTO) {
        User user = getUser(userId);
        validateUserPart(user);

        Template template = Template.builder()
                .user(user)
                .part(user.getPart())
                .templateTitle(requestDTO.getTemplateTitle())
                .templateContent(requestDTO.getTemplateContent())
                .openScope(OpenScope.valueOf(requestDTO.getOpenScope()))
                .build();

        templateRepository.save(template);

        // 미리보기 이미지 저장
        if (previewImage != null) {
            Flag previewFlag = saveFlag(template.getTemplateSeq(), TEMPLATE_PREVIEW_FLAG);
            pictureService.uploadPicture(previewImage, previewFlag);
        }

        // 본문 이미지 저장 및 내용 치환
        if (images != null && !images.isEmpty()) {
            saveFlag(template.getTemplateSeq(), TEMPLATE_FLAG);
            String updatedContent = pictureService.replacePlaceHolderWithUrls(
                    requestDTO.getTemplateContent(),
                    images,
                    TEMPLATE_FLAG,
                    template.getTemplateSeq()
            );
            template.updateTemplate(requestDTO.getTemplateTitle(), updatedContent, OpenScope.valueOf(requestDTO.getOpenScope()));
            templateRepository.save(template);
        }

        return template.getTemplateSeq();
    }

    // 템플릿 수정
    @Transactional
    public void updateTemplate(String userId, Long templateSeq, MultipartFile previewImage, List<MultipartFile> contentImages, TemplateRequestDTO requestDTO) {
        User user = getUser(userId);
        Template template = getTemplate(templateSeq);
        validateTemplateOwnership(template, user);

        // 미리보기 이미지 업데이트
        updatePreviewImage(templateSeq, previewImage);

        // 본문 내 이미지 업데이트
        updateContentImages(template, contentImages, requestDTO);

        // 템플릿 정보 업데이트
        template.updateTemplate(requestDTO.getTemplateTitle(), requestDTO.getTemplateContent(), OpenScope.valueOf(requestDTO.getOpenScope()));
        templateRepository.save(template);
    }

    // 템플릿 삭제
    @Transactional
    public void deleteTemplate(String userId, Long templateSeq) {
        User user = getUser(userId);
        Template template = getTemplate(templateSeq);
        validateTemplateOwnership(template, user);

        deleteImagesByFlagType(templateSeq, TEMPLATE_FLAG);
        deleteImagesByFlagType(templateSeq, TEMPLATE_PREVIEW_FLAG);

        template.markAsDeleted();
        templateRepository.save(template);
    }

    // 공통 로직
    private void updatePreviewImage(Long templateSeq, MultipartFile previewImage) {
        if (previewImage != null && !previewImage.isEmpty()) {
            Flag previewFlag = flagRepository.findByFlagTypeAndFlagEntitySeq(TEMPLATE_PREVIEW_FLAG, templateSeq)
                    .orElseGet(() -> saveFlag(templateSeq, TEMPLATE_PREVIEW_FLAG));

            deleteImagesByFlagType(templateSeq, TEMPLATE_PREVIEW_FLAG);
            pictureService.uploadPicture(previewImage, previewFlag);
        }
    }

    private void updateContentImages(Template template, List<MultipartFile> contentImages, TemplateRequestDTO requestDTO) {
        if (contentImages != null && !contentImages.isEmpty()) {
            deleteImagesByFlagType(template.getTemplateSeq(), TEMPLATE_FLAG);

            String updatedContent = pictureService.replacePlaceHolderWithUrls(
                    requestDTO.getTemplateContent(),
                    contentImages,
                    TEMPLATE_FLAG,
                    template.getTemplateSeq()
            );

            template.updateTemplate(requestDTO.getTemplateTitle(), updatedContent, OpenScope.valueOf(requestDTO.getOpenScope()));
        }
    }

    private void deleteImagesByFlagType(Long entitySeq, String flagType) {
        List<Picture> pictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(flagType, entitySeq);
        pictures.forEach(picture -> {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
            pictureRepository.delete(picture);
        });
    }

    private Flag saveFlag(Long entitySeq, String flagType) {
        Flag flag = Flag.builder()
                .flagType(flagType)
                .flagEntitySeq(entitySeq)
                .build();
        flagRepository.save(flag);
        return flag;
    }

    private User getUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));
    }

    private Template getTemplate(Long templateSeq) {
        return templateRepository.findByTemplateSeqAndDeletedAtIsNull(templateSeq)
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다."));
    }

    private void validateUserPart(User user) {
        if (user.getPart() == null) {
            throw new IllegalArgumentException("회원의 부서 정보가 없습니다.");
        }
    }

    private void validateTemplateOwnership(Template template, User user) {
        if (!template.getUser().equals(user)) {
            throw new IllegalArgumentException("본인이 작성한 템플릿만 수정할 수 있습니다.");
        }
    }

    private boolean isTemplateAccessible(Template template, User user) {
        return switch (template.getOpenScope()) {
            case PRIVATE -> template.getUser().equals(user);
            case CLASS_OPEN -> user.getPart() != null && user.getPart().equals(template.getPart());
            case PUBLIC -> true;
        };
    }

    private TemplateListDTO toTemplateListDTO(Template template) {
        return TemplateListDTO.builder()
                .templateSeq(template.getTemplateSeq())
                .templateTitle(template.getTemplateTitle())
                .build();
    }
}
