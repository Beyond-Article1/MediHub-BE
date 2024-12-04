package mediHub_be.src.test.cp.controller;

import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.controller.CpController;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.service.CpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CpService cpService; // MockBean 설정

    @Test
    @DisplayName("CP명을 입력하여 CP 조회 테스트")
    public void getCpListByCpNameTest() throws Exception {

        // Given: 테스트 결과 CP DTO 준비
        ResponseCpDTO cp1 = ResponseCpDTO.builder()
                .cpName("백내장")
                .cpDescription("백내장 설명")
                .cpViewCount(523L)
                .cpVersion("1.0.0")
                .cpVersionDescription("최초 업로드")
                .createdAt(LocalDateTime.of(2023, 12, 4, 10, 30))
                .userName("임광택")
                .userId("19615041")
                .partName("안과")
                .build();

        ResponseCpDTO cp2 = ResponseCpDTO.builder()
                .cpName("각막염")
                .cpDescription("각막염 설명")
                .cpViewCount(492L)
                .cpVersion("1.1.0")
                .cpVersionDescription("2일차 처방약 변경")
                .createdAt(LocalDateTime.of(2023, 12, 4, 10, 30))
                .userName("정성현")
                .userId("19615043")
                .partName("안과")
                .build();

        List<ResponseCpDTO> cpList = List.of(cp1, cp2);

        // Given: cpService의 getCpListByCpName 메서드가 호출될 때, ApiResponse를 반환하도록 설정
        ApiResponse<List<ResponseCpDTO>> responseApi = ApiResponse.ok(cpList);
        given(cpService.getCpListByCpName("백내장")).willReturn(responseApi.data());

        // When: CP 이름을 입력하여 요청을 보냄
        String inputCpName = "백내장";
        mockMvc.perform(get("/cp?cpName=" + inputCpName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true)) // success 검증
                .andExpect(jsonPath("$.data[0].cpName").value("백내장"))
                .andExpect(jsonPath("$.data[0].cpDescription").value("백내장 설명"))
                .andExpect(jsonPath("$.data[0].cpViewCount").value(523))
                .andExpect(jsonPath("$.data[0].cpVersion").value("1.0.0"))
                .andExpect(jsonPath("$.data[0].cpVersionDescription").value("최초 업로드"))
                .andExpect(jsonPath("$.data[0].userName").value("임광택"))
                .andExpect(jsonPath("$.data[0].userId").value("19615041"))
                .andExpect(jsonPath("$.data[0].partName").value("안과"))
                .andDo(print());

        // Then: cpService의 getCpListByCpName 메서드가 올바르게 호출되었는지 검증
        verify(cpService).getCpListByCpName(inputCpName);
    }
}
