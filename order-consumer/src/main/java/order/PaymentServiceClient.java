package order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentServiceClient {

    private final RestTemplate restTemplate;

    public PaymentServiceClient(@Value("${address_provider.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .defaultHeader("Connection", "close")
                .build();
    }

    public Payment getPaymentForOrder(String orderId) {
        return restTemplate.getForObject(String.format("/payment/%s", orderId), Payment.class);
    }
}