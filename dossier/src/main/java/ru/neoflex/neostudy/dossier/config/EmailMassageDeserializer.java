package ru.neoflex.neostudy.dossier.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import ru.neoflex.neostudy.dossier.model.request.EmailMessage;

import java.nio.charset.StandardCharsets;

public class EmailMassageDeserializer implements Deserializer<EmailMessage> {
    @Override
    public EmailMessage deserialize(String s, byte[] bytes) {
        try {
            if (bytes == null){
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), EmailMessage.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to EmailMessage");
        }
    }
}
