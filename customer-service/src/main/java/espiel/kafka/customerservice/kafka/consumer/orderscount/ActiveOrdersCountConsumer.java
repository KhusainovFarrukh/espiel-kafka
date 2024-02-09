package espiel.kafka.customerservice.kafka.consumer.orderscount;

import espiel.kafka.customerservice.customer.CustomerService;
import espiel.kafka.customerservice.kafka.consumer.orderscount.model.ActiveOrdersCountMessage;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveOrdersCountConsumer extends AbstractConsumerSeekAware {

  @Value("${kafka.consumer.active-orders-count.consume-from-beginning}")
  private boolean consumeFromBeginning;

  private final CustomerService customerService;

  @KafkaListener(
      topics = "${kafka.consumer.active-orders-count.topic}",
      groupId = "${kafka.consumer.active-orders-count.group-id}"
  )
  @SendTo
  public String consume(ConsumerRecord<String, ActiveOrdersCountMessage> message) {
    customerService.updateActiveOrdersCount(message.value());
    return Arrays.toString(
        message.headers().headers(KafkaHeaders.CORRELATION_ID).iterator().next().value()
    );
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

}
