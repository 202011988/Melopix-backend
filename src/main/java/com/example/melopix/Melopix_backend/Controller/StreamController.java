package com.example.melopix.Melopix_backend.Controller;

import com.example.melopix.Melopix_backend.Service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stream")
public class StreamController {

    private final SseService sseService;

    @GetMapping("/music")
    public SseEmitter streamMusic(@RequestParam String taskId) {
        return sseService.subscribe(taskId);
    }
}
