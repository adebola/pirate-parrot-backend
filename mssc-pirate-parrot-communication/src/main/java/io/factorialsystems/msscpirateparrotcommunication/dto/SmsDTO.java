package io.factorialsystems.msscpirateparrotcommunication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
public class SmsDTO implements Serializable {
    private String to;
    private String from;
    private String message;
}
