package com.example.admin_web_app.clients.orders;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

public interface OrderServiceClient {
    @PostExchange("/orders/api/orders")
    OrderConfirmationDTO createOrder(
            @RequestHeader Map<String, ?> headers, @RequestBody CreateOrderRequest orderRequest);

    @GetExchange("/orders/api/orders")
    List<OrderSummary> getOrders(@RequestHeader Map<String, ?> headers);

    @GetExchange("/orders/api/orders/{orderNumber}")
    OrderDTO getOrder(@RequestHeader Map<String, ?> headers, @PathVariable String orderNumber);

    @GetExchange("/orders/api/orders/admin")
    List<OrderSummary> getAllOrders(@RequestHeader Map<String, ?> headers);

    @PutExchange("/orders/api/orders/admin/{orderNumber}/status")
    void updateOrderStatus(@RequestHeader Map<String, ?> headers, @PathVariable String orderNumber, @RequestBody String status);
}
