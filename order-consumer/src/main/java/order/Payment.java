package order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class Payment {

    private UUID id;
    private UUID orderId;
    private String status;
    private int amount;
    private String description;
}