package microservice.order_service.domain.model;

public enum OrderEventType {
    ORDER_CREATED,
    ORDER_PAID,
    ORDER_IN_PROCESS,
    ORDER_CANCELLED,
    ORDER_PROCESSING_FAILED
}
