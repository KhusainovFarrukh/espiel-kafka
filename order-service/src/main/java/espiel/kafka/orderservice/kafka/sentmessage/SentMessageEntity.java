package espiel.kafka.orderservice.kafka.sentmessage;

import static espiel.kafka.orderservice.kafka.sentmessage.SentMessageEntity.TABLE_NAME;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Table(
    name = TABLE_NAME,
    uniqueConstraints = @UniqueConstraint(
        name = "uk_" + TABLE_NAME + "_correlation_id", columnNames = "correlation_id"
    )
)
@EntityListeners(AuditingEntityListener.class)
public class SentMessageEntity {

  protected static final String TABLE_NAME = "sent_messages";
  private static final String GENERATOR_NAME = TABLE_NAME + "_gen";
  private static final String SEQUENCE_NAME = TABLE_NAME + "_seq";

  @Id
  @GeneratedValue(generator = GENERATOR_NAME, strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME)
  private Long id;

  @Column(name = "topic", nullable = false)
  private String topic;

  @Column(name = "timestamp", nullable = false)
  private LocalDateTime timestamp;

  @Column(name = "kafka_offset", nullable = false)
  private Long offset;

  @Column(name = "partition", nullable = false)
  private Integer partition;

  @Column(name = "message", nullable = false)
  private String message;

  @Column(name = "correlation_id", nullable = false, unique = true)
  private String correlationId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private SentMessageStatus status;

  @Column(name = "consumed_at")
  private LocalDateTime consumedAt;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

}
