package ru.neoflex.neostudy.dossier.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.neoflex.neostudy.dossier.model.utils.ThymeleafAttribute;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final TemplateEngine templateEngine;

    private final JavaMailSender emailSender;

//    public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment) throws FileNotFoundException, MessagingException {
//        MimeMessage mimeMessage = emailSender.createMimeMessage();
//        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
//        messageHelper.setTo(toAddress);
//        messageHelper.setSubject(subject);
//        messageHelper.setText(message);
//        if (attachment != null) {
//            FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
//            messageHelper.addAttachment("Purchase Order", file);
//        }
//        emailSender.send(mimeMessage);
//    }

    public void sendEmail(String toAddress, String subject, ThymeleafAttribute attribute) throws MessagingException {
        Context context = getContext(attribute);

        String process = templateEngine.process("index", context);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setTo(toAddress);
        messageHelper.setSubject(subject);
        messageHelper.setText(process, true);
        messageHelper.addInline("image-1.png", new ClassPathResource("templates/images/image-1.png"));
        messageHelper.addInline("image-2.png", new ClassPathResource("templates/images/image-2.png"));
        messageHelper.addInline("image-3.png", new ClassPathResource("templates/images/image-3.png"));
        messageHelper.addInline("image-4.png", new ClassPathResource("templates/images/image-4.png"));

        emailSender.send(mimeMessage);
    }

    private Context getContext(ThymeleafAttribute attribute) {
        Context context = new Context();
        context.setVariable("title", attribute.getTitle());
        context.setVariable("message", attribute.getMessage());
        context.setVariable("url", attribute.getUrl());
        context.setVariable("buttonName", attribute.getButtonName());
        context.setVariable("isHidden", attribute.isHiddenButton());
        return context;
    }
}