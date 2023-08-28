package ru.neoflex.neostudy.dossier.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.dossier.exception.EmailMessageException;
import ru.neoflex.neostudy.dossier.model.request.EmailMessage;
import ru.neoflex.neostudy.dossier.model.utils.ThymeleafAttribute;
import ru.neoflex.neostudy.dossier.utils.PdfConverter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailMessageService {

    private final EmailService emailService;
    private final PdfConverter pdfConverter;

    @Value("${gateway.client.base-url}")
    private String baseUrl;

    @KafkaListener(topics = "finish-registration")
    public void listenFinishRegistrationTopic(EmailMessage emailMessage) {
        ThymeleafAttribute attribute = new ThymeleafAttribute();
        String subject = "Please complete the loan application";
        attribute.setTitle("Please complete the loan application");
        attribute.setMessage("You have chosen one of the conditions offered by us. Please fill out the form to " +
                                     "calculate the total cost of the loan. Link to the form below.");
        attribute.setButtonName("Fill in the form");
        attribute.setUrl(baseUrl + "/application/registration/" + emailMessage.getApplicationId());
        sendEmail(emailMessage, attribute, subject);
    }

    @KafkaListener(topics = "create-documents")
    public void listenCreateDocumentsTopic(EmailMessage emailMessage) {
        ThymeleafAttribute attribute = new ThymeleafAttribute();
        String subject = "Preparation of documents for obtaining a loan";
        attribute.setTitle("Please confirm the preparation of documents");
        attribute.setMessage("Your loan calculation has been completed. To confirm the paperwork, please follow " +
                                     "the link below. In response, you will receive an email with completed " +
                                     "documents.");
        attribute.setButtonName("Prepare documents");
        attribute.setUrl(baseUrl + "/document/" + emailMessage.getApplicationId());
        sendEmail(emailMessage, attribute, subject);
    }

    @KafkaListener(topics = "send-documents")
    public void listenSendDocumentsTopic(EmailMessage emailMessage) {
        ThymeleafAttribute attribute = new ThymeleafAttribute();
        String subject = "Your documents for issuing a loan";
        attribute.setTitle("Documents received");
        attribute.setMessage("Please check their correctness and follow the link to get a simple electronic " +
                                     "signature.");
        attribute.setButtonName("Get SES code");
        attribute.setUrl(baseUrl + "/document/" + emailMessage.getApplicationId() + "/sign");
        sendEmail(emailMessage, attribute, subject);
    }

    @KafkaListener(topics = "send-ses")
    public void listenSendSESTopic(EmailMessage emailMessage) {
        ThymeleafAttribute attribute = new ThymeleafAttribute();
        String subject = "Your documents and SES code have been created";
        attribute.setTitle("Your documents and SES code are ready");
        attribute.setMessage("Please sign documents with ses code. As soon as your application is accepted, " +
                                     "you will be sent an email confirming the issuance of the loan.");
        attribute.setButtonName("Sign documents ");
        attribute.setUrl(baseUrl + "/document/" + emailMessage.getApplicationId() + "/sign/code");
        sendEmail(emailMessage, attribute, subject);
    }

    @KafkaListener(topics = "credit-issued")
    public void listenCreditIssuedTopic(EmailMessage emailMessage) {
        ThymeleafAttribute attribute = new ThymeleafAttribute();
        String subject = "Your loan has been successfully approved";
        attribute.setTitle("Our congratulations!");
        attribute.setMessage("Your loan has been successfully approved. The money will be credited to the " +
                                     "specified account within 24 hours.");
        attribute.setHiddenButton(true);
        sendEmail(emailMessage, attribute, subject);
    }

    @KafkaListener(topics = "application-denied")
    public void listenApplicationDeniedTopic(EmailMessage emailMessage) throws IOException {
        pdfConverter.execute();
        log.info("ok");
    }

    private void sendEmail(EmailMessage emailMessage, ThymeleafAttribute attribute, String subject) {
        try {
            emailService.sendEmail(emailMessage.getAddress(), subject, attribute);
            log.info("Message received: {}", emailMessage);
        } catch (MessagingException e) {
            String message = "Email: " + emailMessage + " was not sent";
            log.error(message, e);
            throw new EmailMessageException(message);
        }
    }
}
