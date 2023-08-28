package provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("address_provider")
@PactFolder("src/test/pacts")
//@PactBroker(url="https://ota.pactflow.io", authentication = @PactBrokerAuth(token = "HbtH0tZq7CU4d18JlKR2kA"))
public class ContractTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    public void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("Customer GET: the address ID matches an existing address")
    public void addressSuppliedByCustomerGETExists() {
    }

    @State("Customer GET: the address ID does not match an existing address")
    public void addressSuppliedByCustomerGETDoesNotExist() {
    }

    @State("Customer GET: the address ID is incorrectly formatted")
    public void addressSuppliedByCustomerGETIsIncorrectlyFormatted() {
    }

    @State("Customer DELETE: the address ID is correctly formatted")
    public void addressSuppliedByCustomerDELETEIsCorrectlyFormatted() {
    }

    @State("Customer DELETE: the address ID is incorrectly formatted")
    public void addressSuppliedByCustomerDELETEIsIncorrectlyFormatted() {
    }
}