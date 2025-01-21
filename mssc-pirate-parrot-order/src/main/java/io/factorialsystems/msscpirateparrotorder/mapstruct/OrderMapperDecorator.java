package io.factorialsystems.msscpirateparrotorder.mapstruct;

import io.factorialsystems.msscpirateparrotorder.dto.OrderDTO;
import io.factorialsystems.msscpirateparrotorder.dto.OrderItemDTO;
import io.factorialsystems.msscpirateparrotorder.model.Order;
import io.factorialsystems.msscpirateparrotorder.model.OrderItem;
import io.factorialsystems.msscpirateparrotorder.model.OrderShipment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OrderMapperDecorator implements OrderMapper{
    private OrderMapper orderMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public Order toModelOrder(OrderDTO orderDTO) {
        final Order order = orderMapper.toModelOrder(orderDTO);

        if (orderDTO.getDeliveryAddress() != null) {
            OrderShipment orderShipment = OrderShipment.builder()
                    .address(orderDTO.getDeliveryAddress())
                    .build();

            order.setOrderShipment(orderShipment);
        }

        return order;
    }

    @Override
    public OrderItem toModelOrderItem(OrderItemDTO orderItemDTO) {
        return orderMapper.toModelOrderItem(orderItemDTO);
    }
}
