package ru.neoflex.neostudy.deal.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Theme {
    FINISH_REGISTRATION("finish-registration"),
    CREATE_DOCUMENTS("create-documents"),
    SEND_DOCUMENTS("send-documents"),
    SEND_SES("send-ses"),
    CREDIT_ISSUED("credit-issued"),
    APPLICATION_DENIED("application-denied");

    private final String topicName;
}
