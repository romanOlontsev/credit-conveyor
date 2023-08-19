package ru.neoflex.neosudy.deal.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.neosudy.deal.exception.BadRequestException;
import ru.neoflex.neosudy.deal.exception.DataNotFoundException;
import ru.neoflex.neosudy.deal.model.response.ApiErrorResponse;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handle(DataNotFoundException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "404", message);
    }

    @ExceptionHandler({BadRequestException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(RuntimeException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, "400", message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(MethodArgumentNotValidException e) {
        String message = e.getFieldErrors()
                          .stream()
                          .map(it -> it.getField() + ": " + it.getDefaultMessage())
                          .collect(Collectors.joining("; "));
        log.error(message);
        return getApiErrorResponse(e, "400", message);
    }

    private ApiErrorResponse getApiErrorResponse(Exception e, String code, String description) {
        ApiErrorResponse exceptionResponse = ApiErrorResponse.builder()
                                                             .code(code)
                                                             .description(description)
                                                             .exceptionName(e.getClass()
                                                                             .getName())
                                                             .exceptionMessage(e.getMessage())
                                                             .build();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            exceptionResponse.addStacktraceItem(stackTraceElement.toString());
        }
        return exceptionResponse;
    }
}