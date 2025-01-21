package io.factorialsystems.msscpirateparrotorder.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private String createdBy;
    private Instant orderDate;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private String userId;
    private String userName;
    private OrderShipment orderShipment;
    private List<OrderItem> orderItems;
}
