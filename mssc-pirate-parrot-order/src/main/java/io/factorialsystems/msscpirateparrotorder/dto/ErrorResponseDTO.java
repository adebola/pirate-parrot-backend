package io.factorialsystems.msscpirateparrotorder.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    private String path;
    private HttpStatus status;
    private Instant dateTime;
    private String message;
}
