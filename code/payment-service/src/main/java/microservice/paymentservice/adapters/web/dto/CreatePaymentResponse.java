package microservice.paymentservice.adapters.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePaymentResponse(
        String transactionID,
        LocalDateTime createdAt,
        String status,
        BigDecimal amount,
        String nameTransactor,
        String approvalUrl) {}
