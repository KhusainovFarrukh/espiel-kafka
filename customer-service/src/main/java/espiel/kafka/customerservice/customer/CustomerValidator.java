package espiel.kafka.customerservice.customer;

import espiel.kafka.customerservice.customer.model.CustomerCreateRequestDTO;
import espiel.kafka.customerservice.customer.model.CustomerUpdateRequestDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

  private final CustomerRepository customerRepository;

  public void validateCreateRequest(CustomerCreateRequestDTO createRequestDTO) {
    validateEmail(createRequestDTO.email());
  }

  public void validateUpdateRequest(Long id, CustomerUpdateRequestDTO updateRequestDTO) {
    Optional.ofNullable(updateRequestDTO.email())
        .ifPresent(email -> validateEmail(id, email));
  }

  private void validateEmail(String email) {
    if (customerRepository.existsByEmailAndDeletedAtIsNull(email)) {
      throw new RuntimeException("Email already exists: " + email);
    }
  }

  private void validateEmail(Long id, String email) {
    if (customerRepository.existsByIdNotAndEmailAndDeletedAtIsNull(id, email)) {
      throw new RuntimeException("Email already exists: " + email);
    }
  }

}
