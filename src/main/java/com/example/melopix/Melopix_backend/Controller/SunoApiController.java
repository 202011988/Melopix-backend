package com.example.melopix.Melopix_backend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suno")
public class SunoApiController {

    @Value("${suno.api.token}")
    private String sunoApiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<Map<String, Object>> generateMusic(@RequestParam String description) {
        String url = "https://apibox.erweima.ai/api/v1/generate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(sunoApiToken);

        Map<String, Object> body = Map.of(
                "prompt", description,
                "customMode", false,
                "instrumental", true,
                "model", "V3_5",
                "callBackUrl", "https://melopix.iptime.org/api/callback"
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return ResponseEntity.ok(response.getBody());
    }
}
