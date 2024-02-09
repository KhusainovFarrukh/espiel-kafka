package espiel.kafka.orderservice.kafka.sentmessage;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentMessageRepository extends JpaRepository<SentMessageEntity, Long> {

  Optional<SentMessageEntity> findByCorrelationId(String correlationId);

}
