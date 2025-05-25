package com.example.melopix.Melopix_backend.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String taskId) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(taskId, emitter);

        emitter.onCompletion(() -> emitters.remove(taskId));
        emitter.onTimeout(() -> emitters.remove(taskId));
        emitter.onError((e) -> emitters.remove(taskId));

        return emitter;
    }

    public void sendEvent(String taskId, Map<String, Object> payload) {
        SseEmitter emitter = emitters.get(taskId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("music-result")
                        .data(payload));

                if ("complete".equals(payload.get("type"))) {
                    emitter.complete();
                    emitters.remove(taskId);
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(taskId);
            }
        }
    }

}
