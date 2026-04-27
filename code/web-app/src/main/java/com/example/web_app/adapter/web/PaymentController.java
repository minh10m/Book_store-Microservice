package com.example.web_app.adapter.web;

import com.example.web_app.clients.orders.OrderDTO;
import com.example.web_app.clients.orders.OrderServiceClient;
import com.example.web_app.clients.payments.PaymentServiceClient;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final OrderServiceClient orderServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    public PaymentController(OrderServiceClient orderServiceClient, PaymentServiceClient paymentServiceClient) {
        this.orderServiceClient = orderServiceClient;
        this.paymentServiceClient = paymentServiceClient;
    }

    @GetMapping("/orders/{orderNumber}/payment")
    public String paymentPage(@PathVariable String orderNumber, Model model, @AuthenticationPrincipal OidcUser user) {
        log.info("Fetching order details for payment: {}", orderNumber);
        OrderDTO order = orderServiceClient.getOrder(
                Map.of("Authorization", "Bearer " + user.getIdToken().getTokenValue()), orderNumber);
        model.addAttribute("order", order);
        return "payment";
    }

    @PostMapping("/api/payments/paypal/create")
    @ResponseBody
    public PaymentServiceClient.CreatePaymentResponse createPayment(
            @RequestBody PaymentServiceClient.CreatePaymentRequest request) {
        try {
            log.info(
                    "Proxying payment creation for order: {} with amount: {}", request.orderNumber(), request.amount());
            return paymentServiceClient.createPayment(request);
        } catch (Exception e) {
            log.error("Failed to proxy payment creation: {}", e.getMessage(), e);
            throw e;
        }
    }
}
