package microservice.order_service.application;

import microservice.order_service.adapters.web.dto.OrderCancelledEvent;
import microservice.order_service.adapters.web.dto.OrderCreatedEvent;
import microservice.order_service.adapters.web.dto.OrderPaidEvent;
import microservice.order_service.adapters.web.dto.OrderErrorEvent;
import microservice.order_service.adapters.web.dto.OrderInProcessEvent;
import microservice.order_service.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class OrderEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void publish(OrderCreatedEvent event) {
        this.send(properties.newOrdersQueue(), event);
    }

    public void publish(OrderPaidEvent event) {
        this.send(properties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderInProcessEvent event) {
        log.info("Order is now IN_PROCESS: {}", event.orderNumber());
    }

    public void publish(OrderCancelledEvent event) {
        this.send(properties.cancelledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event) {
        this.send(properties.errorOrdersQueue(), event);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }
}
