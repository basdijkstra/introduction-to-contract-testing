package customer;

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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "address_provider.base-url:http://localhost:${RANDOM_PORT}",
        classes = AddressServiceClient.class)
public class AddressServiceDeleteContractTest {

    private static final UUID ID = UUID.fromString("8aed8fad-d554-4af8-abf5-a65830b49a5f");

    @Rule
    public PactProviderRule provider = new PactProviderRule("address_provider", null,
            RandomPort.getInstance().getPort(), this);

    @Autowired
    private AddressServiceClient addressServiceClient;


    @Pact(consumer = "customer_consumer")
    public RequestResponsePact pactForDeleteCorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                "Customer DELETE: the address ID is correctly formatted")
                .uponReceiving("A request to delete an address")
                .path(String.format("/address/%s", ID))
                .method("DELETE")
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Pact(consumer = "customer_consumer")
    public RequestResponsePact pactForDeleteIncorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                "Customer DELETE: the address ID is incorrectly formatted")
                .uponReceiving("A request to delete an address")
                .path("/address/this_is_not_a_valid_address_id")
                .method("DELETE")
                .willRespondWith()
                .status(400)
                .toPact();
    }

    @PactVerification(fragment = "pactForDeleteCorrectlyFormattedAddressId")
    @Test
    public void testFor_DELETE_correctlyFormattedAddressId_shouldYieldHttp204() {

        addressServiceClient.deleteAddress(ID.toString());
    }

    @PactVerification(fragment = "pactForDeleteIncorrectlyFormattedAddressId")
    @Test
    public void testFor_DELETE_incorrectlyFormattedAddressId_shouldYieldHttp400() {

        assertThatThrownBy(
                () -> addressServiceClient.deleteAddress("this_is_not_a_valid_address_id")
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("400 Bad Request");
    }
}
