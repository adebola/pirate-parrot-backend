package io.factorialsystems.msscpirateparrotorder.controller;

import io.factorialsystems.msscpirateparrotorder.dto.MessageDTO;
import io.factorialsystems.msscpirateparrotorder.dto.OrderDTO;
import io.factorialsystems.msscpirateparrotorder.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotorder.model.LeanOrder;
import io.factorialsystems.msscpirateparrotorder.model.Order;
import io.factorialsystems.msscpirateparrotorder.model.OrderItem;
import io.factorialsystems.msscpirateparrotorder.service.OrderService;
import io.factorialsystems.msscpirateparrotorder.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PagedDTO<LeanOrder> findAll(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return orderService.findAll(pageNumber, pageSize);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return orderService.saveOrder(orderDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        Optional<Order> order = orderService.findById(id);

        if (order.isEmpty()) {
            final String errorMessage = String.format("Order with Id: %s Not Found", id);
            return ResponseEntity.badRequest().body(new MessageDTO(HttpStatus.BAD_REQUEST.value(), errorMessage));
        }

        return ResponseEntity.ok(order.get());
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<List<OrderItem>> findOrderItemsById(@PathVariable("id") String id) {
        return ResponseEntity.ok(orderService.findOrderItemsByOrderId(id));
    }
}
