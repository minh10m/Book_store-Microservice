package microservice.order_service.domain.model;

public record OrderSummary(String orderNumber, OrderStatus status) {}