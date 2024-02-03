package espiel.kafka.customerservice.customer;

import espiel.kafka.customerservice.customer.model.CustomerCreateRequestDTO;
import espiel.kafka.customerservice.customer.model.CustomerDetailsResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerUpdateRequestDTO;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerValidator customerValidator;
  private final CustomerMapper customerMapper;

  @Override
  public void createCustomer(CustomerCreateRequestDTO createRequestDTO) {
    customerValidator.validateCreateRequest(createRequestDTO);
    
    var customer = customerMapper.toEntity(createRequestDTO);
    customerRepository.save(customer);
  }

  @Override
  public void updateCustomer(Long id, CustomerUpdateRequestDTO updateRequestDTO) {
    customerValidator.validateUpdateRequest(id, updateRequestDTO);
    
    var customer = customerRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    
    customer = customerMapper.update(customer, updateRequestDTO);
    customerRepository.save(customer);
  }

  @Override
  public Page<CustomerResponseDTO> getCustomers(Pageable pageable) {
    return customerRepository
        .findAll(pageable)
        .map(customerMapper::toResponseDTO);
  }

  @Override
  public CustomerDetailsResponseDTO getCustomer(Long id) {
    return customerRepository
        .findById(id)
        .map(customerMapper::toDetailsResponseDTO)
        .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
  }
  
  @Override
  public void deleteCustomer(Long id) {
    var customer = customerRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    
    customer.setDeletedAt(LocalDateTime.now());
    customerRepository.save(customer);
  }

}
