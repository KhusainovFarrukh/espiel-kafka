package espiel.kafka.customerservice.customer;

import espiel.kafka.customerservice.customer.model.CustomerCreateRequestDTO;
import espiel.kafka.customerservice.customer.model.CustomerDetailsResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerUpdateRequestDTO;
import espiel.kafka.customerservice.kafka.consumer.orderscount.model.ActiveOrdersCountMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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

    var customer = findCustomer(id);

    customer = customerMapper.update(customer, updateRequestDTO);
    customerRepository.save(customer);
  }

  @Override
  public Page<CustomerResponseDTO> getCustomers(Pageable pageable) {
    return customerRepository
        .findAllByDeletedAtIsNull(pageable)
        .map(customerMapper::toResponseDTO);
  }

  @Override
  public CustomerDetailsResponseDTO getCustomer(Long id) {
    var customer = findCustomer(id);
    return customerMapper.toDetailsResponseDTO(customer);
  }

  @Override
  public void deleteCustomer(Long id) {
    var customer = findCustomer(id);

    customer.setDeletedAt(LocalDateTime.now());
    customerRepository.save(customer);
  }

  @Override
  public void updateActiveOrdersCount(ActiveOrdersCountMessage message) {
    var customer = findCustomer(message.customerId());
    customer.setActiveOrdersCount(
        customer.getActiveOrdersCount() + message.newOrdersCount().intValue()
    );
    customerRepository.save(customer);
  }

  @Override
  public void updateActiveOrdersCounts(List<ActiveOrdersCountMessage> messages) {
    var countsByCustomerId = messages.stream().collect(Collectors.groupingBy(
        ActiveOrdersCountMessage::customerId,
        Collectors.summingLong(ActiveOrdersCountMessage::newOrdersCount)
    ));

    var customers = customerRepository.findAllById(countsByCustomerId.keySet());

    customers.forEach(customer -> {
      var newCount = countsByCustomerId.get(customer.getId());
      customer.setActiveOrdersCount(customer.getActiveOrdersCount() + newCount.intValue());
    });

    customerRepository.saveAll(customers);
  }

  private CustomerEntity findCustomer(Long id) {
    return customerRepository
        .findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
  }

}
