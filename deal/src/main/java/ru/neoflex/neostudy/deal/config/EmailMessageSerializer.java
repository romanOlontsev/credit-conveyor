package ru.neoflex.neostudy.deal.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import ru.neoflex.neostudy.deal.model.response.EmailMessage;

public class EmailMessageSerializer implements Serializer<EmailMessage> {

    @Override
    public byte[] serialize(String s, EmailMessage emailMessage) {
        if (emailMessage == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(emailMessage);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error when serializing EmailMessage: " + emailMessage + " to byte[]");
        }
    }
}
