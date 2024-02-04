package espiel.kafka.customerservice.customer;

import espiel.kafka.customerservice.customer.model.CustomerCreateRequestDTO;
import espiel.kafka.customerservice.customer.model.CustomerDetailsResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

  void createCustomer(CustomerCreateRequestDTO createRequestDTO);

  void updateCustomer(Long id, CustomerUpdateRequestDTO updateRequestDTO);

  Page<CustomerResponseDTO> getCustomers(Pageable pageable);

  CustomerDetailsResponseDTO getCustomer(Long id);

  void deleteCustomer(Long id);

}
