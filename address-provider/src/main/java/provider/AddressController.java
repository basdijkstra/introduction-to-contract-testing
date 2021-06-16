package provider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{countryCode}/{zipCode}")
    public Location getLocation(@PathVariable String countryCode, @PathVariable String zipCode) {
        if(zipCode.equalsIgnoreCase("banana")) {
            throw new BadRequestException();
        }
        if(zipCode.equalsIgnoreCase("99999")) {
            throw new NotFoundException();
        }
        return locationService.findLocation(countryCode, zipCode);
    }
}
