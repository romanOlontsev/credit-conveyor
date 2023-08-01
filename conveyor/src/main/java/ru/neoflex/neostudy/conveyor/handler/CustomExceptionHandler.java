package ru.neoflex.neostudy.conveyor.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<ApiErrorResponse> handle(RuntimeException e) {
        String message = e.getMessage();
        ApiErrorResponse exceptionResponse = getApiErrorResponse(e, "400", message);
        log.error(message);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handle(MethodArgumentNotValidException e) {
        String message = e.getFieldErrors()
                .stream()
                .map(it -> it.getField() + ": " + it.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.error(message);
        ApiErrorResponse exceptionResponse = getApiErrorResponse(e, "400", message);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
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