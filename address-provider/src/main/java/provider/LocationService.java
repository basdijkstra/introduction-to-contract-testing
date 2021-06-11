package provider;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

    public Location findLocation(String countryCode, String zipCode) {

        Location location = new Location();
        location.setZipCode(zipCode);
        location.setCountry("United States");
        location.setCountryAbbreviation(countryCode);

        List<Place> places = new ArrayList<>();

        Place place = new Place();
        place.setPlaceName("Beverly Hills");
        place.setState("California");
        place.setStateAbbreviation("CA");

        places.add(place);

        location.setPlaces(places);

        return location;
    }
}
