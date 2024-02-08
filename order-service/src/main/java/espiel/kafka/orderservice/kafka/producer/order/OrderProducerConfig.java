package espiel.kafka.orderservice.kafka.producer.order;

import espiel.kafka.orderservice.kafka.producer.order.model.ActiveOrdersCountMessage;
import java.util.Map;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class OrderProducerConfig {

  @Value("${kafka.producer.active-orders-count.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${kafka.producer.active-orders-count.username}")
  private String username;

  @Value("${kafka.producer.active-orders-count.password}")
  private String password;

  private static final String TYPE_KEY_ACTIVE_ORDERS_COUNT = "activeOrdersCount";
  public static final String SASL_MECHANISM_PLAIN = "PLAIN";

  @Bean
  public KafkaTemplate<String, ActiveOrdersCountMessage> kafkaTemplate(
      ProducerFactory<String, ActiveOrdersCountMessage> producerFactory
  ) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public ProducerFactory<String, ActiveOrdersCountMessage> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  private Map<String, Object> producerConfigs() {
    return Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_PLAINTEXT.name,
        SaslConfigs.SASL_MECHANISM, SASL_MECHANISM_PLAIN,
        SaslConfigs.SASL_JAAS_CONFIG, String.format(
            "%s required username=\"%s\" password=\"%s\";",
            PlainLoginModule.class.getName(), username, password
        ),
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
        JsonSerializer.TYPE_MAPPINGS,
        TYPE_KEY_ACTIVE_ORDERS_COUNT + ":" + ActiveOrdersCountMessage.class.getName()
    );
  }

}
