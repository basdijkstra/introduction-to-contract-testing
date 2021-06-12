package order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Component
public class LocationServiceClient {

    private final RestTemplate restTemplate;

    public LocationServiceClient(@Value("${zip_provider.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplateBuilder().rootUri(baseUrl).build();
    }

    public Location getLocation(String countryCode, String zipCode) {
        final Location location = restTemplate.getForObject(String.format("/%s/%s",countryCode, zipCode), Location.class);
        Assert.hasText(location.getZipCode(), "zipCode is blank.");
        return location;
    }
}