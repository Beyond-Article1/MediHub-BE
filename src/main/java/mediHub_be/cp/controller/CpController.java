package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.ResponseCpDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "cp")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CP", description = "CP 관련 API")
public class CpController {

    private final CpService cpService;

    // https://medihub.info/cp?cpSearchCategorySeq=value&cpSearchCategoryData=value
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponseCpDTO>>> getCpListByCpSearchCategoryAndCpSearchCategoryData(
            @RequestParam(required = false) long cpSearchCategorySeq,
            @RequestParam(required = false) String cpSearchCategoryData) {

        return null;
    }

    // https://medihub.info/cp?cpName=value
    @GetMapping
    public ResponseEntity<ApiResponse<ResponseCpDTO>> getCpByCpName(@RequestParam String cpName) {

        return null;
    }

    // https://medihub.info/cp/{cpVersionSeq}
    @GetMapping(value = "/{cpVersionSeq}")
    public ResponseEntity<ApiResponse<ResponseCpDTO>> getCpByCpVersionSeq(@PathVariable long cpVersionSeq) {

        return null;
    }
}
