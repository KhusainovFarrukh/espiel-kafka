package espiel.kafka.orderservice.order;

import espiel.kafka.orderservice.order.model.OrderCreateRequestDTO;
import espiel.kafka.orderservice.order.model.OrderUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidator {

  private final OrderRepository orderRepository;

  public void validateCreateRequest(OrderCreateRequestDTO createRequestDTO) {

  }

  public void validateUpdateRequest(Long id, OrderUpdateRequestDTO updateRequestDTO) {

  }

}
