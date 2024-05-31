package io.factorialsystems.msscpirateparrotproduct.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UomDTO {
    @Null(message = "Unit of Measure Id cannot be set, it is generated automatically")
    private String id;

    @NotEmpty(message = "Unit of Measure name must be specified")
    private String name;
}
