package ru.neoflex.neostudy.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.deal.model.response.EmailMessage;
import ru.neoflex.neostudy.deal.model.types.Theme;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void sendEmailMessage(Theme topic, EmailMessage message) {
        String topicName = topic.getTopicName();
        CompletableFuture<SendResult<String, EmailMessage>> future = kafkaTemplate.send(topicName, message);
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("{} topic message sent: {} with offset={}", topicName, message, result.getRecordMetadata()
                                                                                          .offset());
            } else {
                log.error("Unable to send message: {} on a {} topic", message, topicName);
            }
        });
    }
}
