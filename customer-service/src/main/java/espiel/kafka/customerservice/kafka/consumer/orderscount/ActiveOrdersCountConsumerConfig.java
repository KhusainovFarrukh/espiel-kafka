package espiel.kafka.customerservice.kafka.consumer.orderscount;

import espiel.kafka.customerservice.kafka.consumer.orderscount.model.ActiveOrdersCountMessage;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class ActiveOrdersCountConsumerConfig {

  @Value("${kafka.consumer.active-orders-count.bootstrap-servers}")
  private String bootstrapServers;

  private static final String TYPE_KEY_ACTIVE_ORDERS_COUNT = "activeOrdersCount";

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ActiveOrdersCountMessage> kafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, ActiveOrdersCountMessage>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, ActiveOrdersCountMessage> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  private Map<String, Object> consumerConfigs() {
    return Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
        JsonDeserializer.TRUSTED_PACKAGES, "*",
        JsonDeserializer.TYPE_MAPPINGS,
        TYPE_KEY_ACTIVE_ORDERS_COUNT + ":" + ActiveOrdersCountMessage.class.getName()
    );
  }

}
