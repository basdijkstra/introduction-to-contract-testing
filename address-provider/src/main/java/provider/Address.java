package provider;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor
public class Address {

    private UUID id;
    private String addressType;
    private String street;
    private int number;
    private String city;
    private int zipCode;
    private String state;
    private String country;
}
