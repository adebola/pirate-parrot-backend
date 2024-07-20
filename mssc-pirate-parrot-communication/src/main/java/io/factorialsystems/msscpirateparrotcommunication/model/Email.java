package io.factorialsystems.msscpirateparrotcommunication.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@Document("email")
public class Email {
    @Id
    private String id;
    private String to;
    private String from;
    private String subject;
    private String textBody;
    private String htmlBody;
    private Instant createdDate;
    private String sentBy;
    private String externalId;
    private String attachmentFileName;
}
