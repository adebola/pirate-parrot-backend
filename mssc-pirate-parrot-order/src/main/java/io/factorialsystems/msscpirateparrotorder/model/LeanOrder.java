package io.factorialsystems.msscpirateparrotorder.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LeanOrder {
    private String id;
    private String productName;
    private Instant orderDate;
    private String createdBy;
    private Integer orderStatus;
    private BigDecimal totalPrice;
    private String userId;
    private String userName;
    private String variant;
    private String variantOption;
    private String uom;
    private String orderShipmentId;
    private String orderShipmentAddress;
    private Integer orderShipmentStatus;
}
