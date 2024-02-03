package espiel.kafka.customerservice.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  boolean existsByEmailAndDeletedAtIsNull(String email);

  boolean existsByIdNotAndEmailAndDeletedAtIsNull(Long id, String email);

}
