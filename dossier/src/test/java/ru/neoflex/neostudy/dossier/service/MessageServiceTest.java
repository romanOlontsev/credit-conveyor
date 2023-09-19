package ru.neoflex.neostudy.dossier.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.neoflex.neostudy.dossier.exception.EmailMessageException;
import ru.neoflex.neostudy.dossier.model.dto.ApplicationDTO;
import ru.neoflex.neostudy.dossier.model.request.EmailMessage;
import ru.neoflex.neostudy.dossier.model.types.Theme;
import ru.neoflex.neostudy.dossier.utils.PdfConverter;
import ru.neoflex.neostudy.dossier.webclient.DealClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private EmailService emailService;

    @Mock
    private PdfConverter converter;

    @Mock
    private DealClient dealClient;

    @Test
    void listenFinishRegistrationTopic_shouldSendEmailOnce() {
        EmailMessage message = EmailMessage.builder()
                                           .address("test")
                                           .applicationId(1L)
                                           .theme(Theme.SEND_SES)
                                           .build();

        doNothing().when(emailService)
                   .sendEmail(any(), any(), any(), any());

        messageService.listenFinishRegistrationTopic(message);
        verify(emailService, times(1)).sendEmail(any(), any(), any(), any());
    }

    @Test
    void listenCreateDocumentsTopic_shouldSendEmailOnce() {
        EmailMessage message = EmailMessage.builder()
                                           .address("test")
                                           .applicationId(1L)
                                           .theme(Theme.SEND_SES)
                                           .build();

        doNothing().when(emailService)
                   .sendEmail(any(), any(), any(), any());

        messageService.listenCreateDocumentsTopic(message);
        verify(emailService, times(1)).sendEmail(any(), any(), any(), any());
    }

    @Test
    @SneakyThrows
    void listenSendDocumentsTopic_shouldSendEmailOnce_conversionSuccessful() {
        ReflectionTestUtils.setField(messageService, "documentPath", "%s");
        EmailMessage message = EmailMessage.builder()
                                           .address("test")
                                           .applicationId(1L)
                                           .theme(Theme.SEND_SES)
                                           .build();
        ApplicationDTO mock = Mockito.mock(ApplicationDTO.class);


        doNothing().when(emailService)
                   .sendEmail(any(), any(), any(), any());
        doNothing().when(dealClient)
                   .updateApplicationStatus(any());
        when(dealClient.getApplicationById(any())).thenReturn(mock);
        doNothing().when(converter)
                   .execute(any(), any(), any());

        messageService.listenSendDocumentsTopic(message);
        assertAll(
                () -> verify(emailService, times(1)).sendEmail(any(), any(), any(), any()),
                () -> verify(dealClient, times(1)).updateApplicationStatus(any()),
                () -> verify(dealClient, times(1)).getApplicationById(any()),
                () -> verify(converter, times(1)).execute(any(), any(), any())
        );

    }

    @Test
    @SneakyThrows
    void listenSendDocumentsTopic_shouldSendEmailOnce_conversionFailed() {
        EmailMessage message = EmailMessage.builder()
                                           .address("test")
                                           .applicationId(1L)
                                           .theme(Theme.SEND_SES)
                                           .build();
        ApplicationDTO mock = Mockito.mock(ApplicationDTO.class);

        doNothing().when(dealClient)
                   .updateApplicationStatus(any());
        when(dealClient.getApplicationById(any())).thenReturn(mock);
        doThrow(IOException.class).when(converter)
                                  .execute(any(), any(), any());


        String exceptionMessage = "Error generating pdf documents for applicationId=" + message.getApplicationId();
        assertAll(
                () -> assertThatThrownBy(() ->
                        messageService.listenSendDocumentsTopic(message)).isInstanceOf(EmailMessageException.class)
                                                                         .hasMessage(exceptionMessage)
        );
    }

    @Test
    void listenSendSESTopic_shouldSendEmailOnce() {
        EmailMessage message = EmailMessage.builder()
                                           .address("test")
                                           .applicationId(1L)
                                           .theme(Theme.SEND_SES)
                                           .build();

        doNothing().when(emailService)
                   .sendEmail(any(), any(), any(), any());

        messageService.listenSendSESTopic(message);
        verify(emailService, times(1)).sendEmail(any(), any(), any(), any());
    }

    @Test
    void listenCreditIssuedTopic_shouldSendEmailOnce() {
        EmailMessage message = EmailMessage.builder()
                                           .address("test")
                                           .applicationId(1L)
                                           .theme(Theme.SEND_SES)
                                           .build();

        doNothing().when(emailService)
                   .sendEmail(any(), any(), any(), any());

        messageService.listenCreditIssuedTopic(message);
        verify(emailService, times(1)).sendEmail(any(), any(), any(), any());
    }
}