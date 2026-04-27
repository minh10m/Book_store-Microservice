package microservice.paymentservice.adapters.persistent;

import java.util.Optional;
import microservice.paymentservice.domain.model.PaymentTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, Long> {
    Optional<PaymentTransactionEntity> findByTransactionID(String transactionID);
}
