package order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AddressServiceClient {

    private final RestTemplate restTemplate;

    public AddressServiceClient(@Value("${address_provider.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .defaultHeader("Connection", "close")
                .build();
    }

    public Address getAddress(String addressId) {
        return restTemplate.getForObject(String.format("/address/%s", addressId), Address.class);
    }

    public void deleteAddress(String addressId) {
        restTemplate.delete(String.format("/address/%s", addressId));
    }
}