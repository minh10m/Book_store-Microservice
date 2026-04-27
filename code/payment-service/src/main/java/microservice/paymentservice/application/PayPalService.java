package microservice.paymentservice.application;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import microservice.paymentservice.adapters.persistent.PaymentTransactionRepository;
import microservice.paymentservice.adapters.web.dto.CreatePaymentRequest;
import microservice.paymentservice.adapters.web.dto.CreatePaymentResponse;
import microservice.paymentservice.domain.model.PaymentStatus;
import microservice.paymentservice.domain.model.PaymentTransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PayPalService {
    @Autowired
    private APIContext apiContext;

    private final PaymentTransactionRepository paymentTransactionRepository;

    public PayPalService(APIContext apiContext, PaymentTransactionRepository paymentTransactionRepository) {
        this.apiContext = apiContext;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    public CreatePaymentResponse createPayment(
            CreatePaymentRequest request, String successBaseUrl, String cancelBaseUrl) throws PayPalRESTException {

        PaymentTransactionEntity transaction = new PaymentTransactionEntity();
        transaction.setTransactionID(UUID.randomUUID().toString());
        transaction.setStatus(PaymentStatus.PENDING);
        transaction.setAmount(request.amount());
        transaction.setNameTransactor(request.nameTransactor());
        transaction = paymentTransactionRepository.save(transaction);

        String successUrl = successBaseUrl + "?transactionID=" + transaction.getTransactionID();
        String cancelUrl = cancelBaseUrl + "?transactionID=" + transaction.getTransactionID();

        Payment payment = buildPayment(request.amount(), cancelUrl, successUrl);
        Payment createdPayment = payment.create(apiContext);
        transaction.setPayPalPaymentId(createdPayment.getId());
        paymentTransactionRepository.save(transaction);

        String approvalUrl = extractApprovalUrl(createdPayment);

        return new CreatePaymentResponse(
                transaction.getTransactionID(),
                transaction.getCreatedAt(),
                transaction.getStatus().name(),
                transaction.getAmount(),
                transaction.getNameTransactor(),
                approvalUrl);
    }

    public void handleSuccess(String transactionID, String paymentId, String payerId) throws PayPalRESTException {
        PaymentTransactionEntity transaction =
                paymentTransactionRepository.findByTransactionID(transactionID).orElseThrow();

        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executed = payment.execute(apiContext, paymentExecution);
        if ("approved".equalsIgnoreCase(executed.getState())) {
            transaction.setStatus(PaymentStatus.SUCCESS);
        } else {
            transaction.setStatus(PaymentStatus.FAILED);
        }
        paymentTransactionRepository.save(transaction);
    }

    public void handleCancel(String transactionID) {
        PaymentTransactionEntity transaction =
                paymentTransactionRepository.findByTransactionID(transactionID).orElseThrow();
        transaction.setStatus(PaymentStatus.CANCELLED);
        paymentTransactionRepository.save(transaction);
    }

    private Payment buildPayment(BigDecimal total, String cancelUrl, String successUrl) {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription("BookStore Payment");
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);
        return payment;
    }

    private String extractApprovalUrl(Payment payment) {
        for (Links link : payment.getLinks()) {
            if ("approval_url".equals(link.getRel())) {
                return link.getHref();
            }
        }
        throw new IllegalStateException("PayPal approval url not found");
    }
}
