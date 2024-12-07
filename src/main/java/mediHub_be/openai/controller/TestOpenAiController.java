package mediHub_be.openai.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.openai.dto.ChatGPTRequest;
import mediHub_be.openai.dto.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bot")
public class TestOpenAiController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final WebClient webClient;

    public String callOpenAiApi(String prompt) {
        return webClient.post()
                .uri(apiURL)
                .bodyValue(new ChatGPTRequest(model, prompt))
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 동기 호출
    }

//    @GetMapping("/chat")
//    public String chat(@RequestParam(name = "prompt")String prompt){
//        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
//        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
//        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
//    }

}
