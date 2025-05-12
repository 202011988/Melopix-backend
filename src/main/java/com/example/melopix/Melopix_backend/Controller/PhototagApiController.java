package com.example.melopix.Melopix_backend.Controller;

import com.example.melopix.Melopix_backend.Util.MultipartInputStreamFileResource;
import com.example.melopix.Melopix_backend.VO.PhototagApiVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/phototag")
public class PhototagApiController {

    @Value("${phototag.api.token}")
    private String phototagApiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public PhototagApiVO getDescriptionByPhoto(@RequestParam("file") MultipartFile file) throws IOException {

        String url = "https://server.phototag.ai/api/keywords";

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(phototagApiToken);

        // Prepare body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
        body.add("addMetadata", "false");
        body.add("keywordsOnly", "false");
        body.add("language", "ko");
        body.add("maxKeywords", "10");
        body.add("customContext", "소셜 미디어용 사진 설명");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send request
        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Map.class
        );

        // Parse response
        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");

        PhototagApiVO result = new PhototagApiVO();
        result.setTitle((String) data.get("title"));
        result.setDescription((String) data.get("description"));
        result.setKeywords((List<String>) data.get("keywords"));

        return result;
    }
}
