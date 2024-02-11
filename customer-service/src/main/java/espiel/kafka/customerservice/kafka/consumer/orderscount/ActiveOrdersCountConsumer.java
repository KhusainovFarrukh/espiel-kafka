package espiel.kafka.customerservice.kafka.consumer.orderscount;

import espiel.kafka.customerservice.customer.CustomerService;
import espiel.kafka.customerservice.kafka.consumer.orderscount.model.ActiveOrdersCountMessage;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveOrdersCountConsumer extends AbstractConsumerSeekAware {

  @Value("${kafka.consumer.active-orders-count.consume-from-beginning}")
  private boolean consumeFromBeginning;

  private final CustomerService customerService;

  @KafkaListener(
      topics = "${kafka.consumer.active-orders-count.topic}",
      groupId = "${kafka.consumer.active-orders-count.group-id}",
      batch = "false"
  )
  @SendTo
  public Message<String> consume(ConsumerRecord<String, ActiveOrdersCountMessage> message) {
    customerService.updateActiveOrdersCount(message.value());
    return MessageBuilder.withPayload(
        new String(message
            .headers()
            .headers(KafkaHeaders.CORRELATION_ID)
            .iterator()
            .next()
            .value()
        )
    ).build();
  }

  @KafkaListener(
      topics = "${kafka.consumer.active-orders-count.topic}",
      groupId = "${kafka.consumer.active-orders-count.group-id}",
      batch = "true"
  )
  @SendTo
  public Collection<Message<String>> consume(
      List<ConsumerRecord<String, ActiveOrdersCountMessage>> messages
  ) {
    customerService.updateActiveOrdersCounts(
        messages.stream().map(ConsumerRecord::value).toList()
    );

    return messages.stream()
        .map(message -> {
          var correlationId = getHeaderAsString(message.headers(), KafkaHeaders.CORRELATION_ID);
          var replyTopic = getHeaderAsString(message.headers(), KafkaHeaders.REPLY_TOPIC);

          return MessageBuilder
              .withPayload(correlationId)
              .setHeader(KafkaHeaders.TOPIC, replyTopic)
              .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
              .build();
        })
        .toList();
  }

  @Override
  public void onPartitionsAssigned(
      Map<TopicPartition, Long> assignments,
      ConsumerSeekCallback callback
  ) {
    super.onPartitionsAssigned(assignments, callback);
    if (Boolean.TRUE.equals(consumeFromBeginning)) {
      seekToBeginning();
    }
  }

  private static String getHeaderAsString(
      Headers headers,
      String headerName
  ) {
    return new String(
        headers
            .headers(headerName)
            .iterator()
            .next()
            .value()
    );
  }

}
