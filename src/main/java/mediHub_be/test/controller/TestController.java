package mediHub_be.test.controller;

import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test(){

        log.info("test");
        
        return ResponseEntity.ok(
                ApiResponse.ok("OK")
        );
    }
}
