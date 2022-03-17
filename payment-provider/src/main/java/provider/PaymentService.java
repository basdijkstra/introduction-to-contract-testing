package provider;

import order.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public Payment getPayment(String orderId) {

        Payment payment = new Payment();

        payment.setId("8383a7c3-f831-4f4d-a0a9-015165148af5");
        payment.setOrderId(orderId);
        payment.setStatus("payment_complete");
        payment.setAmount(42);
        payment.setDescription(String.format("Payment for order %s", orderId));

        return payment;
    }
}
