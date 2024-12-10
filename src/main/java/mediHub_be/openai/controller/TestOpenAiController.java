package mediHub_be.openai.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.openai.dto.ResponsePubmedDTO;
import mediHub_be.openai.service.OpenAiService;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/journal")
public class TestOpenAiController {

    private final OpenAiService openAiService;

//    private final WebClient webClient;

//    private final RestTemplate template;
//
//    public TestOpenAiController(@Qualifier("openAiRestTemplate") RestTemplate template) {
//        this.template = template;
//    }

//    public String callOpenAiApi(String prompt) {
//        return webClient.post()
//                .uri(apiURL)
//                .bodyValue(new ChatGPTRequest(model, prompt))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();  // 동기 호출
//    }

//    @GetMapping("/chat")
//    public String chat(@RequestParam(name = "prompt")String prompt){
//        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
//        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
//        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
//    }

    // 논문 검색
    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<List<ResponsePubmedDTO>>> chat(@RequestParam(name = "prompt") String prompt){

        return ResponseEntity.ok(
                ApiResponse.ok(openAiService.getPubmedKeywords(prompt))
        );
    }

    // 논문 자세히 보기 (조회 이력에 남음 및 해당 논문 Entity 생성됨)
    @PostMapping("/{journalPmid}")
    public ResponseEntity<ApiResponse<?>> journal(@PathVariable("journalPmid") String journalPmid,
                                                  @RequestBody ResponsePubmedDTO requestDTO){

        String currentUserId = SecurityUtil.getCurrentUserId();

        return ResponseEntity.ok(
                ApiResponse.ok(openAiService.summarizeAbstractByPmid(currentUserId, journalPmid, requestDTO))
        );
    }
}
