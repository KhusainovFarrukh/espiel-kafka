package espiel.kafka.orderservice.kafka.producer.order;

import static espiel.kafka.orderservice.kafka.producer.order.OrderProducer.TOPIC_ACTIVE_ORDERS_COUNT;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderTopicConfig {

  @Bean
  public NewTopic orderTopic() {
    return new NewTopic(TOPIC_ACTIVE_ORDERS_COUNT, 1, (short) 1);
  }

}
