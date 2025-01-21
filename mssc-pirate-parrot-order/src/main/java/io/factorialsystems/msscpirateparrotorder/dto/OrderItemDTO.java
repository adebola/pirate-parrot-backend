package io.factorialsystems.msscpirateparrotorder.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    @NotEmpty(message = "Product Id must be specified")
    private String productId;

    @NotEmpty(message = "Product Name must be specified")
    private String productName;

    @NotEmpty(message = "Product Variant must be specified")
    private String variant;

    @NotEmpty(message = "Product Variant Option must be specified")
    private String variantOption;

    @NotEmpty(message = "Unit of Measure must be specified")
    private String uom;

    @NotNull(message = "Unit Price must be specified")
    private BigDecimal unitPrice;

    @NotNull(message = "Quantity must be specified")
    private Integer quantity;

    private BigDecimal discount;

    @NotNull(message = "Sub-Total Price for OrderItem must be specified")
    private BigDecimal subTotalPrice;
}