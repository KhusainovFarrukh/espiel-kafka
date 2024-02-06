package espiel.kafka.orderservice.kafka.producer.order;

import espiel.kafka.orderservice.kafka.producer.order.model.ActiveOrdersCountMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class OrderProducerConfig {

  @Bean
  public KafkaTemplate<String, ActiveOrdersCountMessage> kafkaTemplate(
      ProducerFactory<String, ActiveOrdersCountMessage> producerFactory
  ) {
    return new KafkaTemplate<>(producerFactory);
  }

}
