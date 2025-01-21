package io.factorialsystems.msscpirateparrotorder.repository;

import io.factorialsystems.msscpirateparrotorder.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void findById() {
        final String id = "b5d3bb04-233e-11ef-800a-182d9c88d286";

        List<RawOrder> ros = orderRepository.findById(id);
        RawOrder ro = ros.get(0);

        Order order = Order.builder()
                .id(ro.getId())
                .orderDate(ro.getOrderDate())
                .userId(ro.getUserId())
                .userName(ro.getUserName())
                .totalPrice(ro.getTotalPrice())
                .orderStatus(OrderStatus.fromInteger(ro.getOrderStatus()))
                .orderShipment(
                        OrderShipment.builder()
                                .id(ro.getOrderShipmentId())
                                .address(ro.getAddress())
                                .shipmentStatus(OrderShipmentStatus.fromInteger(ro.getShipmentStatus()))
                                .build()
                )
                .orderItems(
                        ros.stream().map(m -> {
                            return OrderItem.builder()
                                    .id(m.getOrderItemId())
                                    .subTotalPrice(m.getSubTotalPrice())
                                    .unitPrice(m.getUnitPrice())
                                    .quantity(m.getQuantity())
                                    .productId(m.getProductId())
                                    .orderId(m.getId())
                                    .discount(m.getDiscount())
                                    .build();
                        }).collect(Collectors.toList())
                )
                .build();

        log.info("Order {}", order);
    }

    @Test
    void save() {
        Order o = Order.builder()
                .userId(UUID.randomUUID().toString())
                .userName("testUser")
                .totalPrice(new BigDecimal(12334))
                .createdBy("test")
                .orderShipment(
                        OrderShipment.builder()
                                .address("25 Valerian")
                                .build()
                )
                .orderItems(
                        List.of(
                                OrderItem.builder()
                                        .subTotalPrice(new BigDecimal(12345678))
                                        .unitPrice(new BigDecimal(1234))
                                        .uom("dosage")
                                        .productName("name")
                                        .variant("variant")
                                        .variantOption("variant-option")
                                        .quantity(1)
                                        .productId(UUID.randomUUID().toString())
                                        .discount(BigDecimal.ZERO)
                                        .build(),
                                OrderItem.builder()
                                        .subTotalPrice(new BigDecimal(1111))
                                        .unitPrice(new BigDecimal(234))
                                        .uom("dosage")
                                        .productName("name")
                                        .variant("variant")
                                        .variantOption("variant-option")
                                        .quantity(2)
                                        .productId(UUID.randomUUID().toString())
                                        .discount(BigDecimal.ZERO)
                                        .build()
                                )
                )
                .build();

        final String orderId = orderRepository.save(o);

        log.info("orderId is {}", orderId);

        List<RawOrder> ros = orderRepository.findById(orderId);
        assertThat(ros.isEmpty()).isFalse();
        log.info("Row Size {}", ros.size());
        log.info("Rows {}", ros);
    }

    @Test
    void findAll() {
        final List<LeanOrder> all = orderRepository.findAll();
        assertThat(all.isEmpty()).isFalse();
        log.info("Size {}", all.size());
        log.info("1st Element {}", all.getFirst());
    }

    @Test
    void findOrderItemsByOrderId() {
        final String id = "b5d3bb04-233e-11ef-800a-182d9c88d286";

        final List<OrderItem> orderItems= orderRepository.findOrderItemsByOrderId(id);
        assertThat(orderItems.isEmpty()).isFalse();

        log.info("OrderItems {}", orderItems.size());

    }
}