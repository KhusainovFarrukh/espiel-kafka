package espiel.kafka.orderservice.kafka.producer.order;

import espiel.kafka.orderservice.kafka.producer.order.model.ActiveOrdersCountMessage;
import espiel.kafka.orderservice.order.OrderStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {

  @Value("${kafka.producer.active-orders-count.topic}")
  private String topicActiveOrdersCount;

  private final KafkaTemplate<String, ActiveOrdersCountMessage> kafkaTemplate;

  public void sendOnCreate(Long customerId) {
    kafkaTemplate.send(
        topicActiveOrdersCount,
        new ActiveOrdersCountMessage(customerId, 1L)
    );
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
          kafkaTemplate.send(
              topicActiveOrdersCount,
              new ActiveOrdersCountMessage(customerId, activeOrdersCount)
          );
        });
  }

}
