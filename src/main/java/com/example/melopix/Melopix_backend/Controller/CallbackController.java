package com.example.melopix.Melopix_backend.Controller;

import com.example.melopix.Melopix_backend.Service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/callback")
public class CallbackController {

    private final SseService sseService;

    @PostMapping
    public ResponseEntity<Void> receiveCallback(@RequestBody Map<String, Object> payload) {
        System.out.println("콜백 수신: " + payload);
        String taskId = (String) ((Map<String, Object>) payload.get("data")).get("task_id");
        sseService.sendEvent(taskId, payload);
        return ResponseEntity.ok().build();
    }
}
