package mediHub_be.case_sharing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.amazonS3.service.AmazonS3Service;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.PictureRepository;
import mediHub_be.board.service.KeywordService;
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
    private final PictureService pictureService;
    private final AmazonS3Service amazonS3Service;

    private final static String TEMPLATE_FLAG = "template";
    private final static String TEMPLATE_PREVIEW_FLAG = "template_preview";

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
                    Optional<Flag> flagOptional = flagRepository.findByFlagTypeAndFlagEntitySeq("template_preview", template.getTemplateSeq());
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
                    Optional<Flag> flagOptional = flagRepository.findByFlagTypeAndFlagEntitySeq("template_preview", template.getTemplateSeq());
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
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public Long createTemplate(String userId, List<MultipartFile> images, MultipartFile previewImage, TemplateRequestDTO requestDTO) {
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
                .openScope(OpenScope.valueOf(requestDTO.getOpenScope()))
                .build();

        templateRepository.save(template);
        if (previewImage != null) {
            Flag previewFlag = saveFlag(template.getTemplateSeq(), TEMPLATE_PREVIEW_FLAG);
            pictureService.uploadPicture(previewImage, previewFlag);
        }
        // 본문 이미지 태그 치환 및 저장
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

    @Transactional
    public void updateTemplate(String userId, Long templateSeq, MultipartFile previewImage, List<MultipartFile> contentImages, TemplateRequestDTO requestDTO) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요한 서비스입니다."));

        Template template = templateRepository.findById(templateSeq)
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다."));

        if (!template.getUser().equals(user)) {
            throw new IllegalArgumentException("본인이 작성한 템플릿만 수정할 수 있습니다.");
        }

        // 1. 미리보기 이미지 업데이트
        if (previewImage != null && !previewImage.isEmpty()) {
            Flag previewFlag = flagRepository.findByFlagTypeAndFlagEntitySeq(TEMPLATE_PREVIEW_FLAG, templateSeq)
                    .orElseGet(() -> saveFlag(templateSeq, TEMPLATE_PREVIEW_FLAG));

            // 기존 미리보기 이미지 삭제
            List<Picture> previewPictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(TEMPLATE_PREVIEW_FLAG, templateSeq);
            previewPictures.forEach(picture -> {
                amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
                pictureRepository.delete(picture);
            });

            // 새로운 미리보기 이미지 업로드
            log.info("preview플래그 " + previewFlag.getFlagSeq());
            pictureService.uploadPicture(previewImage, previewFlag);
        }

        // 2. 본문 내 이미지 업데이트
        if (contentImages != null && !contentImages.isEmpty()) {
            // 기존 본문 이미지 삭제
            List<Picture> existingPictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(TEMPLATE_FLAG, templateSeq);
            existingPictures.forEach(picture -> {
                amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
                pictureRepository.delete(picture);
            });

            // 새 이미지 업로드 및 본문 치환
            String updatedContent = pictureService.replacePlaceHolderWithUrls(
                    requestDTO.getTemplateContent(),
                    contentImages,
                    TEMPLATE_FLAG,
                    templateSeq
            );

            // 템플릿 내용 업데이트
            template.updateTemplate(requestDTO.getTemplateTitle(), updatedContent, OpenScope.valueOf(requestDTO.getOpenScope().toUpperCase()));
        } else {
            // 본문 내 이미지가 없는 경우에도 제목 및 내용 업데이트
            template.updateTemplate(requestDTO.getTemplateTitle(), requestDTO.getTemplateContent(), OpenScope.valueOf(requestDTO.getOpenScope().toUpperCase()));

            List<Picture> existingPictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(TEMPLATE_FLAG, templateSeq);
            existingPictures.forEach(picture -> {
                amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
                pictureRepository.delete(picture);
            });

        }

        // 3. 템플릿 저장
        templateRepository.save(template);
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

        // 2. 본문 내 이미지 삭제
        List<Picture> contentPictures = pictureRepository.findByFlagFlagTypeAndFlagFlagEntitySeq(TEMPLATE_FLAG, templateSeq);
        contentPictures.forEach(picture -> {
            amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
            pictureRepository.delete(picture);
        });

        // 3. 미리보기 이미지 삭제
        flagRepository.findByFlagTypeAndFlagEntitySeq(TEMPLATE_PREVIEW_FLAG, templateSeq)
                .ifPresent(previewFlag -> {
                    pictureRepository.findByFlag_FlagSeq(previewFlag.getFlagSeq())
                            .ifPresent(picture -> {
                                amazonS3Service.deleteImageFromS3(picture.getPictureUrl());
                                pictureRepository.delete(picture);
                            });
                    flagRepository.delete(previewFlag);
                });

        // 4. 템플릿 삭제 처리
        template.markAsDeleted();
        templateRepository.save(template);
    }

    private Flag saveFlag(Long entitySeq, String flagType) {
        Flag flag = Flag.builder()
                .flagType(flagType)
                .flagEntitySeq(entitySeq)
                .build();
        flagRepository.save(flag);
        return flag;
    }
}

