package provider;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.junitsupport.target.Target;
import au.com.dius.pact.provider.junitsupport.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringRestPactRunner.class)
@Provider("address_provider")
@PactFolder("src/test/pacts")
//@PactBroker(url="https://ota.pactflow.io", authentication = @PactBrokerAuth(token = "HbtH0tZq7CU4d18JlKR2kA"))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContractTest {

    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    // The 'as-is' service is used for all provider states, so no additional setup is needed

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

    @State("Order GET: the address ID matches an existing address")
    public void addressSuppliedByOrderExists() {
    }

    @State("Order GET: the address ID does not match an existing address")
    public void addressSuppliedByOrderDoesNotExist() {
    }

    @State("Order GET: the address ID is incorrectly formatted")
    public void addressSuppliedByOrderIsIncorrectlyFormatted() {
    }

    @State("Order DELETE: the address ID is correctly formatted")
    public void addressSuppliedByOrderDELETEIsCorrectlyFormatted() {
    }

    @State("Order DELETE: the address ID is incorrectly formatted")
    public void addressSuppliedByOrderDELETEIsIncorrectlyFormatted() {
    }
}