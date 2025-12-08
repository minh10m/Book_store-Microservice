package microservice.order_service.domain.model;

public enum OrderStatus {
    NEW,
    IN_PROCESS,
    DELIVERED,
    CANCELLED,
    ERROR
}
