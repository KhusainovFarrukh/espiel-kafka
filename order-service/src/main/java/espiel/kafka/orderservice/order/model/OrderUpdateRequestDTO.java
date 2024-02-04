package espiel.kafka.orderservice.order.model;

import espiel.kafka.orderservice.order.OrderStatus;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OrderUpdateRequestDTO(
    Long customerId,

    @Size(min = 1)
    List<String> items,

    OrderStatus status
) {

}
