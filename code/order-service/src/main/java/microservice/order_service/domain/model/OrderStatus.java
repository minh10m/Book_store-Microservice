package microservice.order_service.domain.model;

public enum OrderStatus {
    NEW,
    IN_PROCESS,
    PAID,
    DELIVERED,
    CANCELLED,
    ERROR
}
