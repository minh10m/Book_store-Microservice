package microservice.order_service.adapters.web.dto;

import java.time.LocalDateTime;
import java.util.Set;
import microservice.order_service.domain.model.Address;
import microservice.order_service.domain.model.Customer;

public record OrderCancelledEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason,
        LocalDateTime createdAt) {}
