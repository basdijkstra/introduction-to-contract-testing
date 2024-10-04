package customer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Address {

    private String id;
    private String addressType;
    private String street;
    private int number;
    private String city;
    private int zipCode;
    private String state;
    private String country;
}
