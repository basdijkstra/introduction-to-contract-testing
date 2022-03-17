package provider;

import order.Payment;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment/{orderId}")
    public Payment getPayment(@PathVariable String orderId) {
        if(orderId.equalsIgnoreCase("this_is_not_a_valid_payment_id")) {
            throw new BadRequestException();
        }
        if(orderId.equalsIgnoreCase("00000000-0000-0000-0000-000000000000")) {
            throw new NotFoundException();
        }
        return paymentService.getPayment(orderId);
    }
}
