package microservice.order_service.adapters.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import microservice.order_service.domain.model.Address;
import microservice.order_service.domain.model.Customer;

import java.util.Set;

public record CreateOrderRequest(
        @Valid @NotEmpty(message = "Items cannot be empty") Set<OrderItem> items,
        @Valid Customer customer,
        @Valid Address deliveryAddress) {}