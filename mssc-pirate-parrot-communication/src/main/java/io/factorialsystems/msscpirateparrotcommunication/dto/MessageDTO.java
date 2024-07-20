package io.factorialsystems.msscpirateparrotcommunication.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String id;
    private String message;
}
