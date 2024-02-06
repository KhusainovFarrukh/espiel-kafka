package espiel.kafka.customerservice.kafka.consumer.orderscount;

import espiel.kafka.customerservice.customer.CustomerService;
import espiel.kafka.customerservice.kafka.consumer.orderscount.model.ActiveOrdersCountMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveOrdersCountConsumer {

  public static final String TOPIC_ACTIVE_ORDERS_COUNT = "active-orders-count";
  public static final String GROUP_ID = "customer-service-gr1";

  private final CustomerService customerService;

  @KafkaListener(topics = TOPIC_ACTIVE_ORDERS_COUNT, groupId = GROUP_ID)
  public void consume(ActiveOrdersCountMessage message) {
    customerService.updateActiveOrdersCount(message);
  }

}
