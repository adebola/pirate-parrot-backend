package io.factorialsystems.msscpirateparrotorder.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @NotNull(message = "Order Price must be specified")
    private BigDecimal totalPrice;

    private String deliveryAddress;

    @NotNull(message = "Order Shipment")
    private List<OrderItemDTO> orderItems;
}
