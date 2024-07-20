package io.factorialsystems.msscpirateparrotcommunication.dto;

import io.factorialsystems.msscpirateparrotcommunication.model.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
@ToString
public class EmailDTO implements Serializable {
    @NotEmpty
    private String to;

    @NotEmpty
    private String subject;

    private String textBody;
    private String htmlBody;

    @NotEmpty
    private String secret;

    @NotEmpty
    private String sentBy;

    public Email toEmail() {
        return Email.builder()
                .to(to)
                .subject(subject)
                .htmlBody(htmlBody)
                .textBody(textBody)
                .sentBy(sentBy)
                .createdDate(Instant.now())
                .build();
    }
}
