package espiel.kafka.customerservice.kafka.consumer.orderscount;

import espiel.kafka.customerservice.kafka.consumer.orderscount.model.ActiveOrdersCountMessage;
import java.util.Map;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class ActiveOrdersCountConsumerConfig {

  @Value("${kafka.consumer.active-orders-count.bootstrap-servers}")
  private String bootstrapServers;

  private static final String TYPE_KEY_ACTIVE_ORDERS_COUNT = "activeOrdersCount";

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ActiveOrdersCountMessage> kafkaListenerContainerFactory(
      KafkaTemplate<String, String> replyKafkaTemplate
  ) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, ActiveOrdersCountMessage>();
    factory.setConsumerFactory(consumerFactory());
    factory.setReplyTemplate(replyKafkaTemplate);
    factory.setBatchListener(true);
    return factory;
  }

  @Bean
  public ConsumerFactory<String, ActiveOrdersCountMessage> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  @Bean
  public KafkaTemplate<String, String> replyKafkaTemplate(
      ProducerFactory<String, String> producerFactory
  ) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public ProducerFactory<String, String> replyProducerFactory() {
    return new DefaultKafkaProducerFactory<>(replyProducerConfigs());
  }

  private Map<String, Object> consumerConfigs() {
    return Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.PLAINTEXT.name,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
        JsonDeserializer.TRUSTED_PACKAGES, "*",
        JsonDeserializer.TYPE_MAPPINGS,
        TYPE_KEY_ACTIVE_ORDERS_COUNT + ":" + ActiveOrdersCountMessage.class.getName()
    );
  }

  private Map<String, Object> replyProducerConfigs() {
    return Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.PLAINTEXT.name,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
    );
  }

}
