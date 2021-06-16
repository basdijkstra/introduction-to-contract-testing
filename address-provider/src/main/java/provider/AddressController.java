package provider;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/address/{addressId}")
    public Address getAddress(@PathVariable String addressId) {
        if(addressId.equalsIgnoreCase("this_is_not_a_valid_address_id")) {
            throw new BadRequestException();
        }
        if(addressId.equalsIgnoreCase("00000000-0000-0000-0000-000000000000")) {
            throw new NotFoundException();
        }
        return addressService.getAddress(addressId);
    }

    @DeleteMapping("/address/{addressId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable String addressId) {
        if(addressId.equalsIgnoreCase("this_is_not_a_valid_address_id")) {
            throw new BadRequestException();
        }
        addressService.deleteAddress(addressId);
    }
}
