package io.factorialsystems.msscpirateparrotorder.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String id;
    private String productId;
    private String productName;
    private String variant;
    private String variantOption;
    private String uom;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal subTotalPrice;
    private String orderId;
}