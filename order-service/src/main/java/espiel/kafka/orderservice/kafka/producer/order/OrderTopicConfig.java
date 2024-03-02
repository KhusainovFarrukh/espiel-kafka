package espiel.kafka.orderservice.kafka.producer.order;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderTopicConfig {

  @Value("${kafka.producer.active-orders-count.topic}")
  private String topicActiveOrdersCount;

  @Value("${kafka.producer.active-orders-count.replies-topic}")
  private String topicActiveOrdersCountReplies;

  @Bean
  public NewTopic orderTopic() {
    return new NewTopic(topicActiveOrdersCount, 5, (short) 3);
  }

  @Bean
  public NewTopic orderRepliesTopic() {
    return new NewTopic(topicActiveOrdersCountReplies, 5, (short) 3);
  }

}
