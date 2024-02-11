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
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyTypedMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
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
    var message = MessageBuilder
        .withPayload(new ActiveOrdersCountMessage(customerId, 1L))
        .setHeader(KafkaHeaders.TOPIC, topicActiveOrdersCount)
        .build();
    RequestReplyTypedMessageFuture<String, ActiveOrdersCountMessage, String> future =
        kafkaTemplate.sendAndReceive(message, ParameterizedTypeReference.forType(String.class));

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
          var message = MessageBuilder
              .withPayload(new ActiveOrdersCountMessage(customerId, activeOrdersCount))
              .setHeader(KafkaHeaders.TOPIC, topicActiveOrdersCount)
              .build();
          RequestReplyTypedMessageFuture<String, ActiveOrdersCountMessage, String> future =
              kafkaTemplate.sendAndReceive(
                  message, ParameterizedTypeReference.forType(String.class)
              );

          onMessageSent(future);
        });
  }

  private void onMessageSent(
      RequestReplyTypedMessageFuture<String, ActiveOrdersCountMessage, String> future
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
                    getHeaderAsString(
                        result.getProducerRecord().headers(),
                        KafkaHeaders.CORRELATION_ID
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
              var correlationId = getHeaderAsString(
                  reply.getHeaders(),
                  KafkaHeaders.CORRELATION_ID
              );
              log.info(
                  "Message consumed from topic {}: {}",
                  topicActiveOrdersCount, correlationId
              );
              var consumedAt = LocalDateTime.parse(reply.getPayload().toString());
              sentMessageService.updateStatus(
                  correlationId, SentMessageStatus.CONSUMED, consumedAt
              );
            }
        )
    );
  }

  private static String getHeaderAsString(
      Headers headers,
      String headerName
  ) {
    return new String(headers
        .headers(headerName)
        .iterator()
        .next()
        .value()
    );
  }

  private static String getHeaderAsString(
      MessageHeaders headers,
      String headerName
  ) {
    return headers.get(headerName, String.class);
  }

}
