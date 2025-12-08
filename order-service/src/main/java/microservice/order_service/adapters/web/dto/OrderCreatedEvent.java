package microservice.order_service.adapters.web.dto;

import microservice.order_service.domain.model.Address;
import microservice.order_service.domain.model.Customer;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCreatedEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {}