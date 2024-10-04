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
import org.junit.jupiter.api.Assertions;
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
                .uuid("id", UUID.fromString(AddressId.EXISTING_ADDRESS_ID))
                .stringType("addressType", "billing")
                .stringType("street", "Main Street")
                .integerType("number", 123)
                .stringType("city", "Nothingville")
        ).build();

        return builder.given(
                String.format("Address with ID %s exists", AddressId.EXISTING_ADDRESS_ID))
                .uponReceiving("Retrieving an existing address ID")
                .path(String.format("/address/%s", AddressId.EXISTING_ADDRESS_ID))
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    /**
     * TODO: uncomment the pactForGetNonExistentAddressId() method and complete its implementation by:
     *   - removing the 'return null;' statement from the code
     *   - specifying that a GET to /address/00000000-0000-0000-0000-000000000000 is to be performed
     *   - specifying that this request should return an HTTP 404
     *   - generating a pact segment from these expectations and returning that
     *   You should use a provider state with the exact name 'Address with ID 00000000-0000-0000-0000-000000000000 does not exist'
     *   The implementation is very similar to the one above, but does not need the body() part as we don't expect
     *   the provider to return a response body in this situation.
     */
//    @Pact(provider = "address_provider", consumer = "customer_consumer")
//    public RequestResponsePact pactForGetNonExistentAddressId(PactDslWithProvider builder) {
//
//        return null;
//    }

    @Test
    @PactTestFor(pactMethod = "pactForGetExistingAddressId")
    public void testFor_GET_existingAddressId_shouldYieldExpectedAddressData(MockServer mockServer) {

        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());

        Address address = client.getAddress(AddressId.EXISTING_ADDRESS_ID);

        Assertions.assertEquals(AddressId.EXISTING_ADDRESS_ID, address.getId());
    }

    /**
     * TODO: uncomment the test method after completion of the pactForGetNonExistentAddressId()
     *   method to add this interaction to the contract for the customer_consumer
     */
//    @Test
//    @PactTestFor(pactMethod = "pactForGetNonExistentAddressId")
//    public void testFor_GET_nonExistentAddressId_shouldYieldHttp404(MockServer mockServer) {
//
//        AddressServiceClient client = new AddressServiceClient(mockServer.getUrl());
//
//        Assertions.assertThrows(NotFoundException.class, () -> client.getAddress(AddressId.NON_EXISTING_ADDRESS_ID));
//    }
}
