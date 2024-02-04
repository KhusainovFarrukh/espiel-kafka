package espiel.kafka.customerservice.customer;

import espiel.kafka.customerservice.customer.model.CustomerCreateRequestDTO;
import espiel.kafka.customerservice.customer.model.CustomerDetailsResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerUpdateRequestDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/customers")
public interface CustomerAPI {

  /**
   * Creates a new customer with the given data.
   *
   * @param createRequestDTO the customer data to create.
   * @return ResponseEntity with the status of the operation.
   */
  @PostMapping
  ResponseEntity<Void> createCustomer(
      @Valid @RequestBody CustomerCreateRequestDTO createRequestDTO
  );

  /**
   * Updates the customer with the given id.
   *
   * @param id               the customer id to update.
   * @param updateRequestDTO the customer data to update.
   * @return ResponseEntity with the status of the operation.
   */
  @PatchMapping("{id}")
  ResponseEntity<Void> updateCustomer(
      @PathVariable Long id,
      @Valid @RequestBody CustomerUpdateRequestDTO updateRequestDTO
  );

  /**
   * Returns a paginated list of customers.
   *
   * @param pageable the pagination information.
   * @return ResponseEntity with the paginated list of customers.
   */
  @GetMapping
  ResponseEntity<Page<CustomerResponseDTO>> getCustomers(Pageable pageable);

  /**
   * Returns the customer with the given id.
   *
   * @param id the customer id to return.
   * @return ResponseEntity with the customer data.
   */
  @GetMapping("{id}")
  ResponseEntity<CustomerDetailsResponseDTO> getCustomer(@PathVariable Long id);

  /**
   * Deletes the customer with the given id.
   *
   * @param id the customer id to delete.
   * @return ResponseEntity with the status of the operation.
   */
  @DeleteMapping("{id}")
  ResponseEntity<Void> deleteCustomer(@PathVariable Long id);

}
