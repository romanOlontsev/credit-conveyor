package ru.neoflex.neostudy.dossier.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.dossier.model.request.EmailMessage;

@Slf4j
@Service
public class EmailMessageService {

    @KafkaListener(topics = "finish-registration")
    public void listenFinishRegistrationTopic(EmailMessage message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "create-documents")
    public void listenCreateDocumentsTopic(EmailMessage message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "send-documents")
    public void listenSendDocumentsTopic(EmailMessage message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "send-ses")
    public void listenSendSesTopic(EmailMessage message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "credit-issued")
    public void listenCreditIssuedTopic(EmailMessage message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "application-denied")
    public void listenApplicationDeniedTopic(EmailMessage message) {
        log.info("Message received: {}", message);
    }
}
