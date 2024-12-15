package mediHub_be.cp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.Util.ViewCountManager;
import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.board.service.PictureService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.entity.Cp;
import mediHub_be.cp.repository.CpRepository;
import mediHub_be.cp.repository.CpVersionRepository;
import mediHub_be.security.util.SecurityUtil;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpService {

    // Service
    private final BookmarkService bookmarkService;
    private final PictureService pictureService;
    private final UserService userService;

    // Repository
    private final CpRepository cpRepository;
    private final CpVersionRepository cpVersionRepository;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.service.CpService");       // Logger
    private final ViewCountManager viewCountManager;        // 조회수 매니저

    // FlagType
    private final String CP_VERSION_FLAG = "CP_VERSION";
    private final FlagService flagService;


    /**
     * 주어진 카테고리 시퀀스와 카테고리 데이터를 기준으로 CP 리스트를 조회하는 메서드입니다.
     * 이 메서드는 입력된 카테고리 시퀀스 및 데이터를 사용하여 데이터베이스에서 CP 정보를 검색합니다.
     *
     * @param cpSearchCategorySeqArray  검색할 카테고리 시퀀스의 리스트
     * @param cpSearchCategoryDataArray 검색할 카테고리 데이터의 리스트
     * @return 조회된 CP 정보를 포함하는 ResponseCpDTO의 리스트
     * @throws CustomException 조회 결과가 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public List<ResponseCpDTO> getCpListByCpSearchCategoryAndCpSearchCategoryData(
            List<Long> cpSearchCategorySeqArray,
            List<Long> cpSearchCategoryDataArray) {

        logger.info("CP 검색 카테고리 시퀀스: {}, 카테고리 데이터: {}", cpSearchCategorySeqArray, cpSearchCategoryDataArray);

        // DB 조회
        List<Map<String, Object>> entityList = cpVersionRepository.findByCategorySeqAndCategoryData(
                cpSearchCategorySeqArray,
                cpSearchCategoryDataArray
        );

        if (entityList.isEmpty()) {
            logger.info("조회 결과 없음: 카테고리 시퀀스와 데이터로 찾은 CP가 없습니다.");
            throw new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);
        } else {
            logger.info("조회된 CP 리스트 크기: {}", entityList.size());

            List<ResponseCpDTO> dtoList = entityList.stream()
                    .map(ResponseCpDTO::toDto)
                    .collect(Collectors.toList());

            // 북마크 확인
            checkBookmark(dtoList);

            return dtoList;
        }
    }

    /**
     * 주어진 CP DTO 리스트에 대해 현재 사용자의 북마크 여부를 설정합니다.
     * <p>
     * 이 메서드는 각 DTO에 대해 사용자가 해당 CP 버전을 북마크했는지를 확인하고,
     * 그 결과를 각 DTO의 `bookmarked` 필드에 설정합니다.
     *
     * @param dtoList 북마크 여부를 설정할 CP DTO 리스트
     */
    @Transactional(readOnly = true)
    public void checkBookmark(List<ResponseCpDTO> dtoList) {
        dtoList.forEach(this::checkBookmark);
    }

    /**
     * 주어진 CP DTO에 대해 북마크 여부를 확인하고 설정합니다.
     * <p>
     * 이 메서드는 지정된 CP 버전 시퀀스를 기반으로 사용자의 북마크 여부를 확인하고,
     * 결과를 DTO에 설정합니다.
     *
     * @param dto 확인할 CP DTO
     */
    @Transactional(readOnly = true)
    public void checkBookmark(ResponseCpDTO dto) {
        // 북마크 여부 확인
        logger.info("{}번 Cp가 북마크 여부를 확인합니다.", dto.getCpVersionSeq());

        try {
            boolean isBookmarked = bookmarkService.isBookmarked(CP_VERSION_FLAG, dto.getCpVersionSeq(), SecurityUtil.getCurrentUserId());
            // 설정
            dto.setBookmarked(isBookmarked);
            logger.info("{}번 Cp의 북마크 여부가 {}로 설정되었습니다.", dto.getCpVersionSeq(), isBookmarked);
        } catch (Exception e) {
            logger.error("{}번 Cp의 북마크 여부 확인 중 오류 발생: {}", dto.getCpVersionSeq(), e.getMessage());
            dto.setBookmarked(false);
        }
    }

    /**
     * 주어진 CP 이름을 기준으로 데이터베이스에서 CP 리스트를 조회하는 메서드입니다.
     * 이 메서드는 대소문자를 구분하지 않고 부분 일치를 통해 CP 이름을 검색합니다.
     *
     * @param cpName 검색할 CP 이름
     * @return CP 리스트가 포함된 ResponseCpDTO의 리스트
     * @throws CustomException 조회 결과가 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public List<ResponseCpDTO> getCpListByCpName(String cpName) {
        logger.info("CP 이름으로 검색: {}", cpName);

        // DB 조회
        List<Map<String, Object>> entityList = cpVersionRepository.findByCpNameContainingIgnoreCase(cpName);

        if (entityList.isEmpty()) {
            logger.info("조회 결과 없음: CP 이름 '{}'에 대한 결과가 없습니다.", cpName);
            throw new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);
        } else {
            logger.info("조회된 CP 리스트 크기: {}", entityList.size());
            // Map을 ResponseCpDTO로 변환
            List<ResponseCpDTO> dtoList = entityList.stream()
                    .map(ResponseCpDTO::toDto)
                    .collect(Collectors.toList());

            checkBookmark(dtoList);

            return dtoList;
        }
    }

    /**
     * 주어진 CP 버전 시퀀스를 기준으로 데이터베이스에서 CP 정보를 조회하는 메서드입니다.
     * 이 메서드는 특정 CP 버전 시퀀스에 해당하는 CP 정보를 검색하여 반환합니다.
     *
     * @param cpVersionSeq 조회할 CP 버전 시퀀스
     * @return 조회된 CP 정보를 포함하는 ResponseCpDTO 객체
     * @throws CustomException 조회 결과가 없을 경우 발생합니다.
     */
    @Transactional
    public ResponseCpDTO getCpByCpVersionSeq(
            long cpVersionSeq,
            HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("CP 버전 시퀀스로 검색: {}", cpVersionSeq);

        // DB 조회
        Cp entity = cpRepository.findByCpVersionSeq(cpVersionSeq)
                .orElseThrow(() -> {
                    logger.warn("조회된 CP가 없습니다: CP 버전 시퀀스={}", cpVersionSeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP);
                });
        logger.info("조회된 CP: {}", entity);

        boolean shouldIncrease = viewCountManager.shouldIncreaseViewCount(entity.getCpSeq(), request, response);
        if (shouldIncrease) {
            logger.info("뷰 카운트를 증가시킵니다: CP 시퀀스={}", entity.getCpSeq());
            entity.increaseCpViewCount();
            cpRepository.save(entity);
            logger.info("뷰 카운트 증가 완료: CP 시퀀스={}", entity.getCpSeq());
        } else {
            logger.info("뷰 카운트를 증가시키지 않습니다: CP 시퀀스={} - 사유: 조건 미충족", entity.getCpSeq());
        }

        ResponseCpDTO dto = cpVersionRepository.findByCpVersionSeq(cpVersionSeq)
                .orElseThrow(() -> {
                    logger.warn("조회된 CP 버전 정보가 없습니다: CP 버전 시퀀스={}", cpVersionSeq);
                    return new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);
                });

        logger.info("조회된 CP 버전 정보: {}", dto);

        checkBookmark(dto);

        return dto;
    }

    /**
     * CP 리스트를 조회하는 메서드입니다.
     * 이 메서드는 데이터베이스에서 CP 버전 정보를 조회하고,
     * 결과를 ResponseCpDTO 리스트로 반환합니다.
     *
     * @return CP 리스트가 포함된 ResponseCpDTO의 리스트
     * @throws CustomException 조회 결과가 없을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public List<ResponseCpDTO> getCpList() {
        logger.info("전체 CP 리스트 조회 요청을 받았습니다.");

        List<ResponseCpDTO> dtoList = cpVersionRepository.findCp();

        if (dtoList == null || dtoList.isEmpty()) {
            logger.warn("조회 결과 없음: CP 리스트가 비어있습니다.");
            throw new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);
        }

        logger.info("조회된 CP 리스트 크기: {}", dtoList.size());

        checkBookmark(dtoList);

        return dtoList;
    }

    /**
     * 주어진 CP 버전 시퀀스에 대해 북마크를 토글합니다.
     * <p>
     * 이 메서드는 주어진 CP 버전 시퀀스에 대해 플래그를 생성한 후,
     * 현재 사용자의 북마크 상태를 변경합니다.
     *
     * @param cpVersionSeq 북마크를 설정할 CP 버전 시퀀스
     * @return 북마크 상태가 변경된 결과 (true: 북마크됨, false: 북마크 해제됨)
     */
    public boolean cpBookmark(long cpVersionSeq) {
        // 플래그 생성
        flagService.createFlag(CP_VERSION_FLAG, cpVersionSeq);
        logger.info("CP 버전 {}의 플래그를 생성했습니다.", cpVersionSeq);

        // 북마크 토글
        boolean newBookmarkState = bookmarkService.toggleBookmark(CP_VERSION_FLAG, cpVersionSeq, SecurityUtil.getCurrentUserId());
        logger.info("CP 버전 {}의 북마크 상태가 {} 되었습니다.", cpVersionSeq, newBookmarkState ? "북마크됨" : "북마크 해제됨");

        return newBookmarkState;
    }

    /**
     * 현재 사용자의 북마크된 CP 버전 목록을 가져옵니다.
     * <p>
     * 이 메서드는 현재 사용자의 시퀀스를 사용하여 사용자 정보를 조회한 후,
     * 해당 사용자의 북마크 목록을 가져옵니다. 각 북마크에 대해 CP 버전을 조회하며,
     * 결과를 `ResponseCpDTO` 리스트로 반환합니다.
     *
     * @return 북마크된 CP 버전의 리스트
     * @throws CustomException CP 버전이 존재하지 않을 경우 발생합니다.
     */
    public List<ResponseCpDTO> getBookmarkedCp() {
        User user = userService.findUser(SecurityUtil.getCurrentUserSeq());
        logger.info("사용자 {}의 북마크된 CP 버전을 조회합니다.", user.getUserId());

        List<BookmarkDTO> bookmarkDtoList = bookmarkService.findByUserAndFlagType(user, CP_VERSION_FLAG);
        logger.info("사용자 {}의 북마크 목록을 {}개 찾았습니다.", user.getUserId(), bookmarkDtoList.size());

        List<ResponseCpDTO> responseCpDTOList = bookmarkDtoList.stream()
                .map(bookmarkDto ->
                        cpVersionRepository.findByCpVersionSeq(bookmarkDto.getFlag().getFlagSeq())
                                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CP_VERSION))
                )
                .toList();

        logger.info("사용자 {}의 북마크된 CP 버전 조회가 완료되었습니다.", user.getUserId());
        return responseCpDTOList;
    }
}
