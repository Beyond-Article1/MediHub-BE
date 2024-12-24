package mediHub_be.src.test.cp.service;

import mediHub_be.cp.dto.CpOpinionLocationDTO;
import mediHub_be.cp.dto.RequestCpOpinionLocationDTO;
import mediHub_be.cp.dto.ResponseCpOpinionLocationDTO;
import mediHub_be.cp.entity.CpOpinionLocation;
import mediHub_be.cp.repository.CpOpinionLocationRepository;
import mediHub_be.cp.service.CpOpinionLocationService;
import mediHub_be.security.util.SecurityUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CpOpinionLocationServiceTest {

    @Autowired
    CpOpinionLocationService cpOpinionLocationService;

    @MockBean
    CpOpinionLocationRepository cpOpinionLocationRepository;

    private static List<ResponseCpOpinionLocationDTO> mockCpOpinionLocationDtoList;
    private static RequestCpOpinionLocationDTO requestBody;

    private static CpOpinionLocation entity;

    @BeforeAll
    public static void setUp() {
        ResponseCpOpinionLocationDTO dto1 = ResponseCpOpinionLocationDTO.builder()
                .cpOpinionLocationSeq(1L)
                .cpVersionSeq(1L)
                .cpOpinionLocationPageNum(1L)
                .cpOpinionLocationX(123.4)
                .cpOpinionLocationY(55.4)
                .build();

        ResponseCpOpinionLocationDTO dto2 = ResponseCpOpinionLocationDTO.builder()
                .cpOpinionLocationSeq(2L)
                .cpVersionSeq(1L)
                .cpOpinionLocationPageNum(1L)
                .cpOpinionLocationX(412.4)
                .cpOpinionLocationY(70.4)
                .build();

        mockCpOpinionLocationDtoList = List.of(dto1, dto2);

        requestBody = RequestCpOpinionLocationDTO.builder()
                .cpOpinionLocationPageNum(1L)
                .cpOpinionLocationX(123.4)
                .cpOpinionLocationY(55.4)
                .build();

        entity = CpOpinionLocation.builder()
                .cpOpinionLocationSeq(1L)
                .cpVersionSeq(1L)
                .cpOpinionLocationPageNum(requestBody.getCpOpinionLocationPageNum())
                .cpOpinionLocationX(requestBody.getCpOpinionLocationX())
                .cpOpinionLocationY(requestBody.getCpOpinionLocationY())
                .build();
    }

    @Test
    @DisplayName("CP 버전 시퀀스 -> CP 의견 위치 조회")
    void findCpOpinionLocationListByCpVersionSeq() {
        // Given
        CpOpinionLocation entity1 = CpOpinionLocation.builder()
                .cpOpinionLocationSeq(1L)
                .cpVersionSeq(1L)
                .cpOpinionLocationPageNum(1L)
                .cpOpinionLocationX(123.4)
                .cpOpinionLocationY(55.4)
                .build();

        CpOpinionLocation entity2 = CpOpinionLocation.builder()
                .cpOpinionLocationSeq(2L)
                .cpVersionSeq(1L)
                .cpOpinionLocationPageNum(1L)
                .cpOpinionLocationX(412.4)
                .cpOpinionLocationY(70.4)
                .build();

        List<CpOpinionLocation> entityList = List.of(entity1, entity2);

        Mockito.when(cpOpinionLocationRepository.findByCpVersionSeq(anyLong()))
                .thenReturn(entityList);

        // When
        List<CpOpinionLocationDTO> result = cpOpinionLocationService.findCpOpinionLocationListByCpVersionSeq(1L);

        // Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2); // 리스트 크기 검증

        // 첫 번째 DTO 검증
        CpOpinionLocationDTO firstResult = result.get(0);
        Assertions.assertThat(firstResult.getCpOpinionLocationSeq()).isEqualTo(1L);
        Assertions.assertThat(firstResult.getCpVersionSeq()).isEqualTo(1L);
        Assertions.assertThat(firstResult.getCpOpinionLocationPageNum()).isEqualTo(1L);
        Assertions.assertThat(firstResult.getCpOpinionLocationX()).isEqualTo(123.4);
        Assertions.assertThat(firstResult.getCpOpinionLocationY()).isEqualTo(55.4);

        // 두 번째 DTO 검증
        CpOpinionLocationDTO secondResult = result.get(1);
        Assertions.assertThat(secondResult.getCpOpinionLocationSeq()).isEqualTo(2L);
        Assertions.assertThat(secondResult.getCpVersionSeq()).isEqualTo(1L);
        Assertions.assertThat(secondResult.getCpOpinionLocationPageNum()).isEqualTo(1L);
        Assertions.assertThat(secondResult.getCpOpinionLocationX()).isEqualTo(412.4);
        Assertions.assertThat(secondResult.getCpOpinionLocationY()).isEqualTo(70.4);
    }

    @Test
    @DisplayName("CP 의견 위치 생성 테스트")
    void createCpOpinionLocation() {
        // Given
        Mockito.when(cpOpinionLocationRepository.save(Mockito.any(CpOpinionLocation.class)))
                .thenReturn(entity);

        // When
        CpOpinionLocationDTO result = cpOpinionLocationService.createCpOpinionLocation(1L, requestBody);

        // Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getCpOpinionLocationSeq()).isEqualTo(1L);
        Assertions.assertThat(result.getCpVersionSeq()).isEqualTo(1L);
        Assertions.assertThat(result.getCpOpinionLocationPageNum()).isEqualTo(1L);
        Assertions.assertThat(result.getCpOpinionLocationX()).isEqualTo(123.4);
        Assertions.assertThat(result.getCpOpinionLocationY()).isEqualTo(55.4);
    }

    @Test
    @DisplayName("CP 의견 위치 삭제 테스트")
    void deleteCpOpinionLocation() {
        // Given
        long cpVersionSeq = 1L;
        long cpOpinionLocationSeq = 1L;

        // 로그인 유저 정보 설정
        Long currentUserSeq = 1L; // 로그인된 사용자 ID
        String currentUserAuth = "ADMIN"; // 사용자 권한 (예: USER, ADMIN 등)

        // SecurityUtil의 메서드를 Mock하여 로그인 유저 정보를 설정
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserSeq).thenReturn(currentUserSeq);
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserAuthorities).thenReturn(currentUserAuth);

            // Mockito 설정: cpOpinionLocationRepository가 Mock 객체인지 확인
            assertNotNull(cpOpinionLocationRepository); // cpOpinionLocationRepository가 초기화되어 있는지 확인

            // result를 Mock 객체로 설정
            CpOpinionLocation result = Mockito.mock(CpOpinionLocation.class);

            // Mockito 설정: findById가 Mock 엔티티를 반환하도록 설정
            Mockito.when(cpOpinionLocationRepository.findById(cpOpinionLocationSeq))
                    .thenReturn(Optional.of(result)); // Mock entity 반환
            Mockito.when(cpOpinionLocationRepository.findByCpOpinionLocation_CpOpinion_CpOpinionLocationSeq(cpOpinionLocationSeq))
                    .thenReturn(Optional.of(ResponseCpOpinionLocationDTO.builder()
                            .cpOpinionLocationSeq(cpOpinionLocationSeq)
                            .cpVersionSeq(cpVersionSeq)
                            .cpOpinionLocationPageNum(1)
                            .cpOpinionLocationX(123.4)
                            .cpOpinionLocationY(55.4)
                            .userSeq(currentUserSeq)
                            .build()));

            Mockito.doNothing().when(result).delete(); // delete 메서드가 호출되면 아무 동작도 하지 않음

            // When
            cpOpinionLocationService.deleteCpOpinionLocation(cpVersionSeq, cpOpinionLocationSeq);

            // Then
            Mockito.verify(cpOpinionLocationRepository).findById(cpOpinionLocationSeq); // findById 호출 검증
            Mockito.verify(result).delete(); // delete 메서드 호출 검증
            Mockito.verify(cpOpinionLocationRepository).save(result); // save 메서드 호출 검증
        }
    }
}
