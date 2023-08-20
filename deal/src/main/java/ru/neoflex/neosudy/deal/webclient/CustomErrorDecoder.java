package ru.neoflex.neosudy.deal.webclient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import ru.neoflex.neosudy.deal.exception.BadRequestException;
import ru.neoflex.neosudy.deal.model.response.ApiErrorResponse;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {

        String message = null;
        Reader reader = null;

        try {
            reader = response.body()
                             .asReader(StandardCharsets.UTF_8);
            String result = getResponseBodyAsString(response.body());
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            ApiErrorResponse exceptionMessage = mapper.readValue(result,
                                                                 ApiErrorResponse.class);
            message = exceptionMessage.getDescription();
        } catch (IOException e) {
            log.error("Failed to get body as reader: ", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                log.error("Failed to close the reader with error: ", e);
            }
        }

        if (response.status() == 400) {
            throw new BadRequestException(message);
        }
        return errorDecoder.decode(s, response);
    }

    private String getResponseBodyAsString(final Response.Body body) {
        try {
            return IOUtils.toString(body.asReader(StandardCharsets.UTF_8));
        } catch (final IOException e) {
            log.error("Failed to read the response body with error: ", e);
        }
        return null;
    }
}
