package espiel.kafka.orderservice.kafka.sentmessage.model;

import java.time.LocalDateTime;

public record SentMessageCreateDTO(
    String topic,
    LocalDateTime timestamp,
    Long offset,
    Integer partition,
    String message,
    String correlationId
) {

}
