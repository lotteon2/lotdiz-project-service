package com.lotdiz.projectservice.messagequeue.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.projectservice.dto.request.CreateMakerRequestDto;
import com.lotdiz.projectservice.dto.request.CreateProjectRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MakerProducer {

    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendCreateMaker(CreateMakerRequestDto createMakerRequestDto) {
        try {
            String jsonString = mapper.writeValueAsString(createMakerRequestDto);
            kafkaTemplate.send("create-maker", jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
