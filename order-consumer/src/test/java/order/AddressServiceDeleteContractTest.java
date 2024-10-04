package order;

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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "address_provider", pactVersion = PactSpecVersion.V3)
public class AddressServiceDeleteContractTest {

    @Pact(provider = "address_provider", consumer = "order_consumer")
    public RequestResponsePact pactForDeleteCorrectlyFormattedAddressId(PactDslWithProvider builder) {

        return builder.given(
                        "No specific state required")
                .uponReceiving("Deleting an address ID")
                .path(String.format("/address/%s", AddressId.EXISTING_ADDRESS_ID))
                .method("DELETE")
                .willRespondWith()
                .status(204)
                .toPact();
    }

//    @Pact(provider = "address_provider", consumer = "order_consumer")
//    public RequestResponsePact pactForDeleteIncorrectlyFormattedAddressId(PactDslWithProvider builder) {
//
//        return builder.given(
//                        "No specific state required")
//                .uponReceiving("Deleting an invalid address ID")
//                .path(String.format("/address/%s", AddressId.INVALID_ADDRESS_ID))
//                .method("DELETE")
//                .willRespondWith()
//                .status(400)
//                .toPact();
//    }

    @Test
    @PactTestFor(pactMethod = "pactForDeleteCorrectlyFormattedAddressId")
    public void testFor_DELETE_correctlyFormattedAddressId_shouldYieldHttp204(MockServer mockServer) throws IOException {

        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());

        client.deleteAddress(AddressId.EXISTING_ADDRESS_ID);
    }

//    @Test
//    @PactTestFor(pactMethod = "pactForDeleteIncorrectlyFormattedAddressId")
//    public void testFor_DELETE_incorrectlyFormattedAddressId_shouldYieldHttp400(MockServer mockServer) throws IOException {
//
//        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());
//
//        client.deleteAddress(AddressId.INVALID_ADDRESS_ID);
//    }
}
