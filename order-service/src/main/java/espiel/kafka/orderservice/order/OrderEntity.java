package espiel.kafka.orderservice.order;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(
    name = OrderEntity.TABLE_NAME,
    uniqueConstraints = @UniqueConstraint(
        name = "uk_" + OrderEntity.TABLE_NAME + "_code", columnNames = "code"
    )
)
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

  protected static final String TABLE_NAME = "orders";
  private static final String GENERATOR_NAME = TABLE_NAME + "_gen";
  private static final String SEQUENCE_NAME = TABLE_NAME + "_seq";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
  @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
  private Long id;

  @Column(name = "code", nullable = false, unique = true)
  private UUID code;

  @Column(name = "customer_id", nullable = false)
  private Long customerId;

  @Column(name = "status", nullable = false)
  private OrderStatus status;

  @Column(name = "total", nullable = false)
  private BigDecimal total;

  @ElementCollection
  @Column(name = "items", nullable = false)
  private List<String> items;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

}
