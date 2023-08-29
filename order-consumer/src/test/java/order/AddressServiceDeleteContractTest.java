package order;

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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "address_provider", pactVersion = PactSpecVersion.V3)
public class AddressServiceDeleteContractTest {

    @Pact(provider = "address_provider", consumer = "order_consumer")
    public RequestResponsePact pactForDeleteCorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                        "Order DELETE: the address ID is correctly formatted")
                .uponReceiving("A request to delete an address")
                .path(String.format("/address/%s", Address.VALID_EXISTING_ADDRESS_ID))
                .method("DELETE")
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Pact(provider = "address_provider", consumer = "order_consumer")
    public RequestResponsePact pactForDeleteIncorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                        "Order DELETE: the address ID is incorrectly formatted")
                .uponReceiving("A request to delete an address")
                .path(String.format("/address/%s", Address.INVALID_ADDRESS_ID))
                .method("DELETE")
                .willRespondWith()
                .status(400)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactForDeleteCorrectlyFormattedAddressId")
    public void testFor_DELETE_correctlyFormattedAddressId_shouldYieldHttp204(MockServer mockServer) throws IOException {

        String endpoint = String.format("%s/address/%s", mockServer.getUrl(), Address.VALID_EXISTING_ADDRESS_ID);

        HttpResponse httpResponse = Request.Delete(endpoint).execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(204)));
    }

    @Test
    @PactTestFor(pactMethod = "pactForDeleteIncorrectlyFormattedAddressId")
    public void testFor_DELETE_incorrectlyFormattedAddressId_shouldYieldHttp400(MockServer mockServer) throws IOException {

        String endpoint = String.format("%s/address/%s", mockServer.getUrl(), Address.INVALID_ADDRESS_ID);

        HttpResponse httpResponse = Request.Delete(endpoint).execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(400)));
    }
}
