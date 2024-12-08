package mediHub_be.openai.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.openai.dto.ResponsePubmedDTO;
import mediHub_be.openai.service.OpenAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bot")
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

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<List<ResponsePubmedDTO>>> chat(@RequestParam(name = "prompt") String prompt){

        return ResponseEntity.ok(
                ApiResponse.ok(openAiService.changePubmedKeywords(prompt))
        );
    }
}
