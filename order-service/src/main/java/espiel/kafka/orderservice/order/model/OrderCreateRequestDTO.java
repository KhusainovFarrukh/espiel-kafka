package espiel.kafka.orderservice.order.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderCreateRequestDTO(
    @NotNull
    Long customerId,

    @NotEmpty
    List<String> items
) {

}
