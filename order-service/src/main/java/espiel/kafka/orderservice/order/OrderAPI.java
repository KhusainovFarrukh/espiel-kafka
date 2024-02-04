package espiel.kafka.orderservice.order;

import espiel.kafka.orderservice.order.model.OrderCreateRequestDTO;
import espiel.kafka.orderservice.order.model.OrderDetailsResponseDTO;
import espiel.kafka.orderservice.order.model.OrderResponseDTO;
import espiel.kafka.orderservice.order.model.OrderUpdateRequestDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/orders")
public interface OrderAPI {

  /**
   * Creates a new order with the given data.
   *
   * @param createRequestDTO the order data to create.
   * @return ResponseEntity with the status of the operation.
   */
  @PostMapping
  ResponseEntity<Void> createOrder(
      @Valid @RequestBody OrderCreateRequestDTO createRequestDTO
  );

  /**
   * Updates the order with the given id.
   *
   * @param id               the order id to update.
   * @param updateRequestDTO the order data to update.
   * @return ResponseEntity with the status of the operation.
   */
  @PatchMapping("{id}")
  ResponseEntity<Void> updateOrder(
      @PathVariable Long id,
      @Valid @RequestBody OrderUpdateRequestDTO updateRequestDTO
  );

  /**
   * Returns the order with the given id.
   *
   * @param id the order id to return.
   * @return ResponseEntity with the order data.
   */
  @GetMapping("{id}")
  ResponseEntity<OrderDetailsResponseDTO> getOrder(
      @PathVariable Long id
  );

  /**
   * Returns a paginated list of orders.
   *
   * @param pageable the pagination information.
   * @return ResponseEntity with the paginated list of orders.
   */
  @GetMapping
  ResponseEntity<Page<OrderResponseDTO>> getOrders(Pageable pageable);

}
