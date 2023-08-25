package ru.neoflex.neostudy.conveyor.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.neostudy.conveyor.exception.BadRequestException;
import ru.neoflex.neostudy.conveyor.model.response.ApiErrorResponse;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({BadRequestException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse handle(RuntimeException e) {
        String message = e.getMessage();
        log.error(message);
        return getApiErrorResponse(e, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse handle(MethodArgumentNotValidException e) {
        String message = e.getFieldErrors()
                .stream()
                .map(it -> it.getField() + ": " + it.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.error(message);
        return getApiErrorResponse(e, message);
    }

    private ApiErrorResponse getApiErrorResponse(Exception e, String description) {
        ApiErrorResponse exceptionResponse = ApiErrorResponse.builder()
                .code("400")
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