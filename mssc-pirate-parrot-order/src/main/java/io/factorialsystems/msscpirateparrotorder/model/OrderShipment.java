package io.factorialsystems.msscpirateparrotorder.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderShipment {
    private String id;
    private String address;
    private OrderShipmentStatus shipmentStatus;

}
