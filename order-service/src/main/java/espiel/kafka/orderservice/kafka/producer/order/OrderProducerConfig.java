package espiel.kafka.orderservice.kafka.producer.order;

import espiel.kafka.orderservice.kafka.producer.order.model.ActiveOrdersCountMessage;
import java.time.Duration;
import java.util.Map;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class OrderProducerConfig {

  @Value("${kafka.producer.active-orders-count.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${kafka.producer.active-orders-count.username}")
  private String username;

  @Value("${kafka.producer.active-orders-count.password}")
  private String password;

  @Value("${kafka.producer.active-orders-count.replies-topic}")
  private String repliesTopic;

  @Value("${kafka.producer.active-orders-count.replies-group-id}")
  private String repliesGroupId;

  @Value("${kafka.producer.active-orders-count.replies-timeout-days}")
  private Long repliesTimeoutDays;

  private static final String TYPE_KEY_ACTIVE_ORDERS_COUNT = "activeOrdersCount";
  public static final String SASL_MECHANISM_PLAIN = "PLAIN";

  @Bean
  public ReplyingKafkaTemplate<String, ActiveOrdersCountMessage, String> kafkaTemplate(
      ProducerFactory<String, ActiveOrdersCountMessage> producerFactory,
      KafkaMessageListenerContainer<String, String> repliesContainer
  ) {
    var replyingKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, repliesContainer);
    replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofDays(repliesTimeoutDays));
    replyingKafkaTemplate.setBinaryCorrelation(false);
    return replyingKafkaTemplate;
  }

  @Bean
  public ProducerFactory<String, ActiveOrdersCountMessage> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaMessageListenerContainer<String, String> repliesContainer(
      ConsumerFactory<String, String> replyConsumerFactory
  ) {
    var containerProperties = new ContainerProperties(repliesTopic);
    containerProperties.setGroupId(repliesGroupId);
    return new KafkaMessageListenerContainer<>(replyConsumerFactory, containerProperties);
  }

  @Bean
  public ConsumerFactory<String, String> replyConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(replyConsumerConfigs());
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

  private Map<String, Object> replyConsumerConfigs() {
    return Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_PLAINTEXT.name,
        SaslConfigs.SASL_MECHANISM, SASL_MECHANISM_PLAIN,
        SaslConfigs.SASL_JAAS_CONFIG, String.format(
            "%s required username=\"%s\" password=\"%s\";",
            PlainLoginModule.class.getName(), username, password
        ),
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
    );
  }

}
