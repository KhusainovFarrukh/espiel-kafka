package espiel.kafka.customerservice.customer;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  boolean existsByEmailAndDeletedAtIsNull(String email);

  boolean existsByIdNotAndEmailAndDeletedAtIsNull(Long id, String email);

  Optional<CustomerEntity> findByIdAndDeletedAtIsNull(Long id);

  Page<CustomerEntity> findAllByDeletedAtIsNull(Pageable pageable);

}
