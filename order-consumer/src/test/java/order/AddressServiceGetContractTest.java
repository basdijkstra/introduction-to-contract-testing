package order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "address_provider.base-url:http://localhost:${RANDOM_PORT}",
        classes = AddressServiceClient.class)
public class AddressServiceGetContractTest {

    private static final UUID ID = UUID.fromString("8aed8fad-d554-4af8-abf5-a65830b49a5f");
    private static final String ADDRESS_TYPE = "billing";
    private static final String STREET = "Main Street";
    private static final int NUMBER = 123;
    private static final String CITY = "Nothingville";
    private static final int ZIP_CODE = 54321;
    private static final String STATE = "Tennessee";
    private static final String COUNTRY = "United States";

    @Rule
    public PactProviderRule provider = new PactProviderRule("address_provider", null,
            RandomPort.getInstance().getPort(), this);

    @Autowired
    private AddressServiceClient addressServiceClient;


    @Pact(consumer = "order_consumer")
    public RequestResponsePact pactForGetExistingAddressId(PactDslWithProvider builder) {

        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .uuid("id", ID)
                .stringType("addressType", ADDRESS_TYPE)
                .stringType("street", STREET)
                .integerType("number", NUMBER)
                .stringType("city", CITY)
                .integerType("zipCode", ZIP_CODE)
                .stringType("state", STATE)
                .stringType("country", COUNTRY)
        ).build();

        return builder.given(
                "Order GET: the address ID matches an existing address")
                .uponReceiving("A request for address data")
                .path(String.format("/address/%s", ID))
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @Pact(consumer = "order_consumer")
    public RequestResponsePact pactForGetNonExistentAddressId(PactDslWithProvider builder) {

        return builder.given(
                "Order GET: the address ID does not match an existing address")
                .uponReceiving("A request for address data")
                .path("/address/00000000-0000-0000-0000-000000000000")
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Pact(consumer = "order_consumer")
    public RequestResponsePact pactForGetIncorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                "Order GET: the address ID is incorrectly formatted")
                .uponReceiving("A request for address data")
                .path("/address/this_is_not_a_valid_address_id")
                .method("GET")
                .willRespondWith()
                .status(400)
                .toPact();
    }

    @PactVerification(fragment = "pactForGetExistingAddressId")
    @Test
    public void testFor_GET_existingAddressId_shouldYieldExpectedAddressData() {

        final Address address = addressServiceClient.getAddress(ID.toString());

        assertThat(address.getId()).isEqualTo(ID);
        assertThat(address.getAddressType()).isEqualTo(ADDRESS_TYPE);
        assertThat(address.getStreet()).isEqualTo(STREET);
        assertThat(address.getNumber()).isEqualTo(NUMBER);
        assertThat(address.getCity()).isEqualTo(CITY);
        assertThat(address.getZipCode()).isEqualTo(ZIP_CODE);
        assertThat(address.getState()).isEqualTo(STATE);
        assertThat(address.getCountry()).isEqualTo(COUNTRY);
    }

    @PactVerification(fragment = "pactForGetNonExistentAddressId")
    @Test
    public void testFor_GET_nonExistentAddressId_shouldYieldHttp404() {

        assertThatThrownBy(
                () -> addressServiceClient.getAddress("00000000-0000-0000-0000-000000000000")
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("404 Not Found");
    }

    @PactVerification(fragment = "pactForGetIncorrectlyFormattedAddressId")
    @Test
    public void testFor_GET_incorrectlyFormattedAddressId_shouldYieldHttp400() {

        assertThatThrownBy(
                () -> addressServiceClient.getAddress("this_is_not_a_valid_address_id")
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("400 Bad Request");
    }
}
