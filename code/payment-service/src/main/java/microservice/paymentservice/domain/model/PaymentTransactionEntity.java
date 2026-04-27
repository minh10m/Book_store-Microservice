package microservice.paymentservice.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_txn_id_generator")
    @SequenceGenerator(name = "payment_txn_id_generator", sequenceName = "payment_txn_id_seq")
    private Long id;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionID;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "name_transactor", nullable = false)
    private String nameTransactor;

    @Column(name = "paypal_payment_id")
    private String payPalPaymentId;

    @Column(name = "order_number")
    private String orderNumber;

    public Long getId() {
        return id;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNameTransactor() {
        return nameTransactor;
    }

    public void setNameTransactor(String nameTransactor) {
        this.nameTransactor = nameTransactor;
    }

    public String getPayPalPaymentId() {
        return payPalPaymentId;
    }

    public void setPayPalPaymentId(String payPalPaymentId) {
        this.payPalPaymentId = payPalPaymentId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
