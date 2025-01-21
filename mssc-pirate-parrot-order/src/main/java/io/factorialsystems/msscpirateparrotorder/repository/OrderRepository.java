package io.factorialsystems.msscpirateparrotorder.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotorder.model.LeanOrder;
import io.factorialsystems.msscpirateparrotorder.model.Order;
import io.factorialsystems.msscpirateparrotorder.model.OrderItem;
import io.factorialsystems.msscpirateparrotorder.model.RawOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderRepository {
    List<RawOrder> findById(String id);

    String save(Order order);

    Page<LeanOrder> findAll();
    List<OrderItem> findOrderItemsByOrderId(String id);
}
