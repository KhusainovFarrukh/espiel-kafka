package espiel.kafka.orderservice.kafka.producer.order;

import espiel.kafka.orderservice.kafka.producer.order.model.ActiveOrdersCountMessage;
import espiel.kafka.orderservice.kafka.sentmessage.SentMessageService;
import espiel.kafka.orderservice.kafka.sentmessage.SentMessageStatus;
import espiel.kafka.orderservice.kafka.sentmessage.model.SentMessageCreateDTO;
import espiel.kafka.orderservice.order.OrderStatus;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

  @Value("${kafka.producer.active-orders-count.topic}")
  private String topicActiveOrdersCount;

  private final ReplyingKafkaTemplate<String, ActiveOrdersCountMessage, String> kafkaTemplate;
  private final SentMessageService sentMessageService;

  public void sendOnCreate(Long customerId) {
    var producerRecord = new ProducerRecord<String, ActiveOrdersCountMessage>(
        topicActiveOrdersCount,
        new ActiveOrdersCountMessage(customerId, 1L)
    );
    var future = kafkaTemplate.sendAndReceive(producerRecord);

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
          var producerRecord = new ProducerRecord<String, ActiveOrdersCountMessage>(
              topicActiveOrdersCount,
              new ActiveOrdersCountMessage(customerId, activeOrdersCount)
          );
          var future = kafkaTemplate.sendAndReceive(producerRecord);

          onMessageSent(future);
        });
  }

  private void onMessageSent(
      RequestReplyFuture<String, ActiveOrdersCountMessage, String> future
  ) {
    future.getSendFuture().whenComplete((result, exception) -> Optional.ofNullable(exception)
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
                    result.getProducerRecord().value().toString(),
                    new String(
                        result
                            .getProducerRecord()
                            .headers()
                            .headers(KafkaHeaders.CORRELATION_ID)
                            .iterator()
                            .next()
                            .value()
                    )
                )
            )
        )
    );

    future.whenComplete((reply, exception) -> Optional.ofNullable(exception)
        .ifPresentOrElse(
            e -> log.error(
                "Error consuming message from topic {}: {}",
                topicActiveOrdersCount, e.getMessage()
            ),
            () -> {
              log.info(
                  "Message consumed from topic {}: {}",
                  topicActiveOrdersCount, reply.value()
              );
              sentMessageService.updateStatus(reply.value(), SentMessageStatus.CONSUMED);
            }
        )
    );
  }

}
