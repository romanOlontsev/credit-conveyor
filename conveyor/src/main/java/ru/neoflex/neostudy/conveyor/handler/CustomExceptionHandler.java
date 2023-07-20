package ru.neoflex.neostudy.conveyor.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.neostudy.conveyor.exception.BadRequestException;
import ru.neoflex.neostudy.conveyor.model.response.ApiErrorResponse;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handle(BadRequestException e) {
        ApiErrorResponse exceptionResponse = getApiErrorResponse(e, "400", "Invalid request parameters");
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