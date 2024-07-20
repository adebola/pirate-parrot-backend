package io.factorialsystems.msscpirateparrotcommunication.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Document("sms")
public class SMS {
    @Id
    private String id;
    private String to;
    private String from;
    private String message;
    private Instant time;
}
