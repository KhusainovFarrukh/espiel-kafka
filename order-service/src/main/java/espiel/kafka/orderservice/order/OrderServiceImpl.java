package espiel.kafka.orderservice.order;

import espiel.kafka.orderservice.kafka.producer.order.OrderProducer;
import espiel.kafka.orderservice.order.model.OrderCreateRequestDTO;
import espiel.kafka.orderservice.order.model.OrderDetailsResponseDTO;
import espiel.kafka.orderservice.order.model.OrderResponseDTO;
import espiel.kafka.orderservice.order.model.OrderUpdateRequestDTO;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final OrderValidator orderValidator;
  private final OrderProducer orderProducer;

  private final Random random = new Random();

  @Override
  public void createOrder(
      OrderCreateRequestDTO createRequestDTO
  ) {
    orderValidator.validateCreateRequest(createRequestDTO);

    var order = orderMapper.toEntity(createRequestDTO);
    order.setStatus(OrderStatus.ACTIVE);
    order.setCode(UUID.randomUUID());
    order.setTotal(BigDecimal.valueOf(random.nextDouble()));

    orderRepository.save(order);
    orderProducer.sendOnCreate(order.getCustomerId());
  }

  @Override
  public void updateOrder(Long id, OrderUpdateRequestDTO updateRequestDTO) {
    orderValidator.validateUpdateRequest(id, updateRequestDTO);

    var order = findOrder(id);

    orderProducer.sendOnUpdate(
        order.getCustomerId(),
        order.getStatus(),
        updateRequestDTO.status()
    );

    order = orderMapper.update(order, updateRequestDTO);
    orderRepository.save(order);
  }

  @Override
  public OrderDetailsResponseDTO getOrder(Long id) {
    var order = findOrder(id);
    return orderMapper.toDetailsResponseDTO(order);
  }

  @Override
  public Page<OrderResponseDTO> getOrders(Pageable pageable) {
    return orderRepository
        .findAllByDeletedAtIsNull(pageable)
        .map(orderMapper::toResponseDTO);
  }

  private OrderEntity findOrder(Long id) {
    return orderRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new RuntimeException("Order not found: " + id));
  }

}
