package io.factorialsystems.msscpirateparrotcommunication.exception;


import io.factorialsystems.msscpirateparrotcommunication.dto.ErrorResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException rex, WebRequest webRequest) {
        final String message = String.format("caused by %s", rex.getMessage());


        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .dateTime(Instant.now())
                        .message(message)
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        final List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        allErrors.forEach(a -> {
            validationErrors.put(((FieldError)a).getField(), a.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(validationErrors);
    }
}
