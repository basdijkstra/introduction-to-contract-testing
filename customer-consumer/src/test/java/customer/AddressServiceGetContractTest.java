package customer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.UUID;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "address_provider", pactVersion = PactSpecVersion.V3)
public class AddressServiceGetContractTest {

    @Pact(provider = "address_provider", consumer = "customer_consumer")
    public RequestResponsePact pactForGetExistingAddressId(PactDslWithProvider builder) {

        /**
         * TODO: Add the following expectations for the provider response to the existing ones:
         *   - The response should contain a field 'zipCode' with an integer value
         *   - The response should contain a field 'state' with a string value
         *   - EXTRA: The response should contain a field 'country' with a value that can only be 'United States' or 'Canada'
         *  (for the last one, have a look at https://docs.pact.io/implementation_guides/jvm/consumer#dsl-matching-methods for a hint)
         */
        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .uuid("id", UUID.fromString(AddressId.VALID_EXISTING_ADDRESS_ID))
                .stringType("addressType", "billing")
                .stringType("street", "Main Street")
                .integerType("number", 123)
                .stringType("city", "Nothingville")
        ).build();

        return builder.given(
                String.format("Address with ID %s exists", AddressId.VALID_EXISTING_ADDRESS_ID))
                .uponReceiving("Retrieving a valid existing address ID")
                .path(String.format("/address/%s", AddressId.VALID_EXISTING_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @Pact(provider = "address_provider", consumer = "customer_consumer")
    public RequestResponsePact pactForGetNonExistentAddressId(PactDslWithProvider builder) {

        return builder.given(
                String.format("Address with ID %s does not exist", AddressId.VALID_NON_EXISTING_ADDRESS_ID))
                .uponReceiving("Retrieving a valid non-existing address ID")
                .path(String.format("/address/%s", AddressId.VALID_NON_EXISTING_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Pact(provider = "address_provider", consumer = "customer_consumer")
    public RequestResponsePact pactForGetIncorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                "No specific state required")
                .uponReceiving("Retrieving an invalid address ID")
                .path(String.format("/address/%s", AddressId.INVALID_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(400)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactForGetExistingAddressId")
    public void testFor_GET_existingAddressId_shouldYieldExpectedAddressData(MockServer mockServer) throws IOException {

        String endpoint = String.format("%s/address/%s", mockServer.getUrl(), AddressId.VALID_EXISTING_ADDRESS_ID);

        HttpResponse httpResponse = Request.Get(endpoint).execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(200)));
    }

    @Test
    @PactTestFor(pactMethod = "pactForGetNonExistentAddressId")
    public void testFor_GET_nonExistentAddressId_shouldYieldHttp404(MockServer mockServer) throws IOException {

        String endpoint = String.format("%s/address/%s", mockServer.getUrl(), AddressId.VALID_NON_EXISTING_ADDRESS_ID);

        HttpResponse httpResponse = Request.Get(endpoint).execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(404)));
    }

    @Test
    @PactTestFor(pactMethod = "pactForGetIncorrectlyFormattedAddressId")
    public void testFor_GET_incorrectlyFormattedAddressId_shouldYieldHttp400(MockServer mockServer) throws IOException {

        String endpoint = String.format("%s/address/%s", mockServer.getUrl(), AddressId.INVALID_ADDRESS_ID);

        HttpResponse httpResponse = Request.Get(endpoint).execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(400)));
    }
}
