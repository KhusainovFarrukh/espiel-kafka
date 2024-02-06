package espiel.kafka.customerservice.kafka.consumer.orderscount;

import espiel.kafka.customerservice.customer.CustomerService;
import espiel.kafka.customerservice.kafka.consumer.orderscount.model.ActiveOrdersCountMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveOrdersCountConsumer {

  private final CustomerService customerService;

  @KafkaListener(
      topics = "${kafka.consumer.active-orders-count.topic}",
      groupId = "${kafka.consumer.active-orders-count.group-id}"
  )
  public void consume(ActiveOrdersCountMessage message) {
    customerService.updateActiveOrdersCount(message);
  }

}
