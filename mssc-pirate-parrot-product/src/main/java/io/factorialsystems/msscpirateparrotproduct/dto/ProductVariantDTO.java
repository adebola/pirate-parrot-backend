package io.factorialsystems.msscpirateparrotproduct.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {
    @Null(message = "Product Variant Id cannot be set, it is generated automatically")
    private String id;

    @NotEmpty(message = "Product Variant name must be specified")
    private String name;
}
