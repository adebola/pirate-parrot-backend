package io.factorialsystems.msscpirateparrotorder.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscpirateparrotorder.dto.OrderDTO;
import io.factorialsystems.msscpirateparrotorder.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotorder.mapstruct.OrderMapper;
import io.factorialsystems.msscpirateparrotorder.model.*;
import io.factorialsystems.msscpirateparrotorder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    public PagedDTO<LeanOrder> findAll(Integer pageNumber, Integer pageSize) {
        log.info("Find All Orders PageNumber : {}, PageSize {}", pageNumber, pageSize);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createPagedDto(orderRepository.findAll());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String saveOrder(OrderDTO o) {
        return orderRepository.save(orderMapper.toModelOrder(o));
    }

    public Optional<Order> findById(String id) {
        List<RawOrder> ros = orderRepository.findById(id);

        if (ros == null || ros.isEmpty()) {
            final String errorMessage = String.format("Cannot Find Order %s", id);
            log.error(errorMessage);

            return Optional.empty();
        }

        RawOrder ro = ros.getFirst();

        return Optional.of(
                Order.builder()
                        .id(ro.getId())
                        .createdBy(ro.getCreatedBy())
                        .orderDate(ro.getOrderDate())
                        .userId(ro.getUserId())
                        .userName(ro.getUserName())
                        .totalPrice(ro.getTotalPrice())
                        .orderStatus(OrderStatus.fromInteger(ro.getOrderStatus()))
                        .totalPrice(ro.getTotalPrice())
                        .orderShipment(toOrderShipment(ro))
                        .orderItems(ros.stream().map(this::toOrderItem).toList())
                        .build()
        );
    }

    public List<OrderItem> findOrderItemsByOrderId(String id) {
        return orderRepository.findOrderItemsByOrderId(id);
    }

    private OrderShipment toOrderShipment(RawOrder ro) {
        return OrderShipment.builder()
                .id(ro.getOrderShipmentId())
                .address(ro.getAddress())
                .shipmentStatus(OrderShipmentStatus.fromInteger(ro.getShipmentStatus()))
                .build();
    }

    private OrderItem toOrderItem(RawOrder ro) {

        return OrderItem.builder()
                .id(ro.getOrderItemId())
                .subTotalPrice(ro.getSubTotalPrice())
                .unitPrice(ro.getUnitPrice())
                .quantity(ro.getQuantity())
                .productId(ro.getProductId())
                .variant(ro.getProductVariant())
                .variantOption(ro.getProductVariantOption())
                .uom(ro.getProductUom())
                .orderId(ro.getId())
                .discount(ro.getDiscount())
                .build();
    }

    private PagedDTO<LeanOrder> createPagedDto(Page<LeanOrder> orders) {
        PagedDTO<LeanOrder> pagedDto = new PagedDTO<>();
        pagedDto.setTotalSize((int) orders.getTotal());
        pagedDto.setPageNumber(orders.getPageNum());
        pagedDto.setPageSize(orders.getPageSize());
        pagedDto.setPages(orders.getPages());
        pagedDto.setList(orders.getResult());

        return pagedDto;
    }
}
