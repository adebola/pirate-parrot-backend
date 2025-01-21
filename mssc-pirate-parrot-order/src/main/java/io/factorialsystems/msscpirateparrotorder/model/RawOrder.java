package io.factorialsystems.msscpirateparrotorder.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RawOrder {
    private String id;
    private Instant orderDate;
    private Integer orderStatus;
    private String createdBy;
    private BigDecimal totalPrice;
    private String userId;
    private String userName;
    private String orderShipmentId;
    private String address;
    private Integer shipmentStatus;
    private String orderItemId;
    private String productId;
    private String productName;
    private String productVariant;
    private String productVariantOption;
    private String productUom;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal subTotalPrice;
}