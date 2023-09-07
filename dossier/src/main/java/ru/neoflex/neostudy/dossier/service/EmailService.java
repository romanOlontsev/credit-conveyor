package ru.neoflex.neostudy.dossier.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.neoflex.neostudy.dossier.exception.EmailMessageException;
import ru.neoflex.neostudy.dossier.model.utils.ThymeleafAttribute;

import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final TemplateEngine templateEngine;

    private final JavaMailSender emailSender;

    public void sendEmail(String toAddress, String subject, ThymeleafAttribute attribute, String attachment) {
        String process = substituteInTemplate(attribute);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        setEmailMessage(toAddress, subject, attachment, mimeMessage, process);
        emailSender.send(mimeMessage);
    }

    private void setEmailMessage(String toAddress, String subject, String attachment, MimeMessage mimeMessage,
                                 String process) {
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(toAddress);
            messageHelper.setSubject(subject);
            messageHelper.setText(process, true);
            messageHelper.addInline("image-1.png", new ClassPathResource("templates/images/image-1.png"));
            messageHelper.addInline("image-2.png", new ClassPathResource("templates/images/image-2.png"));
            messageHelper.addInline("image-3.png", new ClassPathResource("templates/images/image-3.png"));
            messageHelper.addInline("image-4.png", new ClassPathResource("templates/images/image-4.png"));
            if (attachment != null) {
                FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
                messageHelper.addAttachment("credit-terms.pdf", file);
            }
        } catch (MessagingException | FileNotFoundException e) {
            throw new EmailMessageException("Error set MimeMessageHelper params");
        }
    }

    private String substituteInTemplate(ThymeleafAttribute attribute) {
        Context context = new Context();
        context.setVariable("title", attribute.getTitle());
        context.setVariable("message", attribute.getMessage());
        context.setVariable("url", attribute.getUrl());
        context.setVariable("buttonName", attribute.getButtonName());
        context.setVariable("isHidden", attribute.isHiddenButton());
        return templateEngine.process("email", context);
    }
}