package com.example.web_app.clients.payments;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface PaymentServiceClient {
    @PostExchange("/payments/api/payments/paypal/create")
    CreatePaymentResponse createPayment(@RequestBody CreatePaymentRequest request);

    record CreatePaymentRequest(BigDecimal amount, String nameTransactor, String orderNumber) {}
    record CreatePaymentResponse(String approvalUrl) {}
}
