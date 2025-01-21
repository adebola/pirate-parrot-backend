package io.factorialsystems.msscpirateparrotorder.exception;


import io.factorialsystems.msscpirateparrotorder.dto.ErrorResponseDTO;
import io.factorialsystems.msscpirateparrotorder.dto.MessageDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
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

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException exception) {
//
//        final List<Map<String, String>> errorList = exception.getFieldErrors().stream()
//                .map(fieldError -> {
//                    Map<String, String> errorMap = new HashMap<>();
//                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
//                    return errorMap;
//                }).toList();
//
//        return ResponseEntity.badRequest().body(errorList);
//    }

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<?> handleJPAViolations(TransactionSystemException tse) {
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();

        if (tse.getCause().getCause() instanceof ConstraintViolationException cve) {
            List<Map<String, String>> errors = cve.getConstraintViolations()
                    .stream()
                    .map(cv -> {
                        Map<String, String> errMap = new HashMap<>();
                        errMap.put(cv.getPropertyPath().toString(), cv.getMessage());
                        return errMap;
                    }).toList();

            return bodyBuilder.body(errors);
        }

        return bodyBuilder.build();
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<MessageDTO> handleNotFoundException(NotFoundException nfe) {
        MessageDTO messageDTO = new MessageDTO(HttpStatus.NOT_FOUND.value(), nfe.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
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
