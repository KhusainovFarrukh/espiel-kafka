package espiel.kafka.orderservice.kafka.producer.order;

import espiel.kafka.orderservice.kafka.producer.order.model.ActiveOrdersCountMessage;
import espiel.kafka.orderservice.kafka.sentmessage.SentMessageService;
import espiel.kafka.orderservice.kafka.sentmessage.model.SentMessageCreateDTO;
import espiel.kafka.orderservice.order.OrderStatus;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

  @Value("${kafka.producer.active-orders-count.topic}")
  private String topicActiveOrdersCount;

  private final KafkaTemplate<String, ActiveOrdersCountMessage> kafkaTemplate;
  private final SentMessageService sentMessageService;

  public void sendOnCreate(Long customerId) {
    var future = kafkaTemplate.send(
        topicActiveOrdersCount,
        new ActiveOrdersCountMessage(customerId, 1L)
    );

    onMessageSent(future);
  }

  public void sendOnUpdate(Long customerId, OrderStatus oldStatus, OrderStatus newStatus) {
    Optional
        .ofNullable(newStatus)
        .filter(status -> !status.equals(oldStatus))
        .ifPresent(status -> {
          long activeOrdersCount;
          if (status == OrderStatus.ACTIVE) {
            activeOrdersCount = 1L;
          } else {
            activeOrdersCount = -1L;
          }
          var future = kafkaTemplate.send(
              topicActiveOrdersCount,
              new ActiveOrdersCountMessage(customerId, activeOrdersCount)
          );

          onMessageSent(future);
        });
  }

  private void onMessageSent(
      CompletableFuture<SendResult<String, ActiveOrdersCountMessage>> future
  ) {
    future.whenComplete((result, exception) -> Optional.ofNullable(exception)
        .ifPresentOrElse(
            e -> log.error(
                "Error sending message to topic {}: {}",
                topicActiveOrdersCount, e.getMessage()
            ),
            () -> sentMessageService.createSentMessage(
                new SentMessageCreateDTO(
                    result.getRecordMetadata().topic(),
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(result.getRecordMetadata().timestamp()),
                        ZoneId.systemDefault()
                    ),
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition(),
                    result.getProducerRecord().value().toString()
                )
            )
        ));
  }

}
