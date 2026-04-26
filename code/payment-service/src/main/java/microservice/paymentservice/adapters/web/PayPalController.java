package microservice.paymentservice.adapters.web;

import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import microservice.paymentservice.adapters.web.dto.CreatePaymentRequest;
import microservice.paymentservice.adapters.web.dto.CreatePaymentResponse;
import microservice.paymentservice.application.PayPalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/paypal")
public class PayPalController {
    private static final String SUCCESS_URL = "http://localhost:8989/payments/api/payments/paypal/success";
    private static final String CANCEL_URL = "http://localhost:8989/payments/api/payments/paypal/cancel";
    private static final String WEB_APP_PRODUCTS_URL = "http://localhost:8080/products";

    private final PayPalService payPalService;

    public PayPalController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePaymentResponse createPayment(@Valid @RequestBody CreatePaymentRequest request)
            throws PayPalRESTException {
        return payPalService.createPayment(request, SUCCESS_URL, CANCEL_URL);
    }

    @GetMapping("/success")
    public void success(
            @RequestParam("transactionID") String transactionID,
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            HttpServletResponse response)
            throws PayPalRESTException, IOException {
        payPalService.handleSuccess(transactionID, paymentId, payerId);
        response.sendRedirect(WEB_APP_PRODUCTS_URL);
    }

    @GetMapping("/cancel")
    public void cancel(@RequestParam("transactionID") String transactionID, HttpServletResponse response)
            throws IOException {
        payPalService.handleCancel(transactionID);
        response.sendRedirect(WEB_APP_PRODUCTS_URL);
    }
}
