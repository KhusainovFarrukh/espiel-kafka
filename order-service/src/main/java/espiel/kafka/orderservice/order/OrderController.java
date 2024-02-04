package espiel.kafka.orderservice.order;

import espiel.kafka.orderservice.order.model.OrderCreateRequestDTO;
import espiel.kafka.orderservice.order.model.OrderDetailsResponseDTO;
import espiel.kafka.orderservice.order.model.OrderResponseDTO;
import espiel.kafka.orderservice.order.model.OrderUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderAPI {

  private final OrderService orderService;

  @Override
  public ResponseEntity<Void> createOrder(
      OrderCreateRequestDTO createRequestDTO
  ) {
    orderService.createOrder(createRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public ResponseEntity<Void> updateOrder(
      Long id,
      OrderUpdateRequestDTO updateRequestDTO
  ) {
    orderService.updateOrder(id, updateRequestDTO);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<OrderDetailsResponseDTO> getOrder(
      Long id
  ) {
    return ResponseEntity.ok(orderService.getOrder(id));
  }

  @Override
  public ResponseEntity<Page<OrderResponseDTO>> getOrders(
      Pageable pageable
  ) {
    return ResponseEntity.ok(orderService.getOrders(pageable));
  }

}
