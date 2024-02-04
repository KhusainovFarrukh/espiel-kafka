package espiel.kafka.orderservice.order;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  Optional<OrderEntity> findByIdAndDeletedAtIsNull(Long id);

  Page<OrderEntity> findAllByDeletedAtIsNull(Pageable pageable);

}
