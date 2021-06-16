package provider;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    public Address getAddress(String addressId) {

        Address address = new Address();

        address.setId(UUID.fromString(addressId));
        address.setAddressType("billing");
        address.setStreet("Main Street");
        address.setNumber(123);
        address.setCity("Nothingville");
        address.setZipCode(54321);
        address.setState("Tennessee");
        address.setCountry("United States");

        return address;
    }

    public void deleteAddress(String addressId) {

    }
}
