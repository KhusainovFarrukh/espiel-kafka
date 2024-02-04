package espiel.kafka.orderservice.order.model;

import espiel.kafka.orderservice.order.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
    Long id,
    UUID code,
    Long customerId,
    OrderStatus status,
    BigDecimal total,
    List<String> items,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
