package com.eighttoten.notification.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseEmitterRepository {
    private Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public List<SseEmitter> findAllStartWithByMemberEmail(String email) {
        return emitterMap.entrySet().stream()
                .filter(entry-> entry.getKey().startsWith(email))
                .map(Entry::getValue)
                .toList();
    }

    public SseEmitter save(String id, SseEmitter emitter) {
        return emitterMap.put(id, emitter);
    }

    public void deleteById(String uniqueEmitterId) {
        emitterMap.remove(uniqueEmitterId);
    }

    public void deleteAll(){
        emitterMap.clear();
    }
}