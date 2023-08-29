package order;

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

    @Pact(provider = "address_provider", consumer = "order_consumer")
    public RequestResponsePact pactForGetExistingAddressId(PactDslWithProvider builder) {

        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .uuid("id", UUID.fromString(Address.VALID_EXISTING_ADDRESS_ID))
                .stringType("addressType", "billing")
                .stringType("street", "Main Street")
                .integerType("number", 123)
                .stringType("city", "Nothingville")
                .integerType("zipCode", 54321)
                .stringType("state", "Tennessee")
                .stringType("country", "United States")
        ).build();

        return builder.given(
                        "Order GET: the address ID matches an existing address")
                .uponReceiving("A request for address data")
                .path(String.format("/address/%s", Address.VALID_EXISTING_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @Pact(provider = "address_provider", consumer = "order_consumer")
    public RequestResponsePact pactForGetNonExistentAddressId(PactDslWithProvider builder) {

        return builder.given(
                        "Order GET: the address ID does not match an existing address")
                .uponReceiving("A request for address data")
                .path(String.format("/address/%s", Address.VALID_NON_EXISTING_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Pact(provider = "address_provider", consumer = "order_consumer")
    public RequestResponsePact pactForGetIncorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                        "Order GET: the address ID is incorrectly formatted")
                .uponReceiving("A request for address data")
                .path(String.format("/address/%s", Address.INVALID_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(400)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactForGetExistingAddressId")
    public void testFor_GET_existingAddressId_shouldYieldExpectedAddressData(MockServer mockServer) throws IOException {

        String endpoint = String.format("%s/address/%s", mockServer.getUrl(), Address.VALID_EXISTING_ADDRESS_ID);

        HttpResponse httpResponse = Request.Get(endpoint).execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(200)));
    }

    @Test
    @PactTestFor(pactMethod = "pactForGetNonExistentAddressId")
    public void testFor_GET_nonExistentAddressId_shouldYieldHttp404(MockServer mockServer) throws IOException {

        String endpoint = String.format("%s/address/%s", mockServer.getUrl(), Address.VALID_NON_EXISTING_ADDRESS_ID);

        HttpResponse httpResponse = Request.Get(endpoint).execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(404)));
    }

    @Test
    @PactTestFor(pactMethod = "pactForGetIncorrectlyFormattedAddressId")
    public void testFor_GET_incorrectlyFormattedAddressId_shouldYieldHttp400(MockServer mockServer) throws IOException {

        String endpoint = String.format("%s/address/%s", mockServer.getUrl(), Address.INVALID_ADDRESS_ID);

        HttpResponse httpResponse = Request.Get(endpoint).execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(400)));
    }
}
