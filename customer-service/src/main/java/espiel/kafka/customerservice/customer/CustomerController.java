package espiel.kafka.customerservice.customer;

import espiel.kafka.customerservice.customer.model.CustomerCreateRequestDTO;
import espiel.kafka.customerservice.customer.model.CustomerDetailsResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerAPI {

  private final CustomerService customerService;

  @Override
  public ResponseEntity<Void> createCustomer(
      CustomerCreateRequestDTO createRequestDTO
  ) {
    customerService.createCustomer(createRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public ResponseEntity<Void> updateCustomer(
      Long id,
      CustomerUpdateRequestDTO updateRequestDTO
  ) {
    customerService.updateCustomer(id, updateRequestDTO);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Page<CustomerResponseDTO>> getCustomers(Pageable pageable) {
    return ResponseEntity.ok(customerService.getCustomers(pageable));
  }

  @Override
  public ResponseEntity<CustomerDetailsResponseDTO> getCustomer(Long id) {
    return ResponseEntity.ok(customerService.getCustomer(id));
  }

  @Override
  public ResponseEntity<Void> deleteCustomer(Long id) {
    customerService.deleteCustomer(id);
    return ResponseEntity.ok().build();
  }

}
