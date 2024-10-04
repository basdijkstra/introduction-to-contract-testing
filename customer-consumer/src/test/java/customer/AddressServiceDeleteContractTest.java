package customer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.io.IOException;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "address_provider", pactVersion = PactSpecVersion.V3)
public class AddressServiceDeleteContractTest {

    @Pact(provider = "address_provider", consumer = "customer_consumer")
    public RequestResponsePact pactForDeleteCorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                        "No specific state required")
                .uponReceiving("Deleting a valid address ID")
                .path(String.format("/address/%s", AddressId.EXISTING_ADDRESS_ID))
                .method("DELETE")
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactForDeleteCorrectlyFormattedAddressId")
    public void testFor_DELETE_correctlyFormattedAddressId_shouldYieldHttp204(MockServer mockServer) throws IOException {

        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());

        client.deleteAddress(AddressId.EXISTING_ADDRESS_ID);
    }
}
