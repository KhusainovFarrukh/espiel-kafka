package espiel.kafka.orderservice.kafka.producer.order;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderTopicConfig {

  @Value("${kafka.producer.active-orders-count.topic}")
  private String topicActiveOrdersCount;

  @Bean
  public NewTopic orderTopic() {
    return new NewTopic(topicActiveOrdersCount, 1, (short) 1);
  }

}
