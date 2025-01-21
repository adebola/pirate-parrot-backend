package io.factorialsystems.msscpirateparrotorder.mapstruct;

import io.factorialsystems.msscpirateparrotorder.dto.OrderDTO;
import io.factorialsystems.msscpirateparrotorder.dto.OrderItemDTO;
import io.factorialsystems.msscpirateparrotorder.model.Order;
import io.factorialsystems.msscpirateparrotorder.model.OrderItem;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
@DecoratedWith(OrderMapperDecorator.class)
public interface OrderMapper {

    @Mappings({
            @Mapping(source = "totalPrice", target = "totalPrice"),
            @Mapping(source = "orderItems", target = "orderItems"),
    })
    Order toModelOrder(OrderDTO orderDTO);


    @Mappings({
            @Mapping(source = "productId", target = "productId"),
            @Mapping(source = "productName", target = "productName"),
            @Mapping(source = "variant", target = "variant"),
            @Mapping(source = "variantOption", target = "variantOption"),
            @Mapping(source = "uom", target = "uom"),
            @Mapping(source = "unitPrice", target = "unitPrice"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "discount", target = "discount"),
            @Mapping(source = "subTotalPrice", target = "subTotalPrice"),
    })
    OrderItem toModelOrderItem(OrderItemDTO orderItemDTO);
}