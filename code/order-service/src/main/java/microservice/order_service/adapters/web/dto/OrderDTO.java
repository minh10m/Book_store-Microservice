package microservice.order_service.adapters.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import microservice.order_service.domain.model.Address;
import microservice.order_service.domain.model.Customer;
import microservice.order_service.domain.model.OrderStatus;

public record OrderDTO(
        String orderNumber,
        String user,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        OrderStatus status,
        String comments,
        LocalDateTime createdAt) {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
