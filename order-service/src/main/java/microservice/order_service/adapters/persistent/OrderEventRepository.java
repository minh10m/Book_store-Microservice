package microservice.order_service.adapters.persistent;

import microservice.order_service.domain.OrderEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEventEntity, Long> {}
