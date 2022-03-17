package order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Payment {

    private String id;
    private String orderId;
    private String status;
    private int amount;
    private String description;
}
