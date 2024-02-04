package espiel.kafka.orderservice.order;

import espiel.kafka.orderservice.order.model.OrderCreateRequestDTO;
import espiel.kafka.orderservice.order.model.OrderDetailsResponseDTO;
import espiel.kafka.orderservice.order.model.OrderResponseDTO;
import espiel.kafka.orderservice.order.model.OrderUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

  void createOrder(OrderCreateRequestDTO createRequestDTO);

  void updateOrder(Long id, OrderUpdateRequestDTO updateRequestDTO);

  OrderDetailsResponseDTO getOrder(Long id);

  Page<OrderResponseDTO> getOrders(Pageable pageable);

}
