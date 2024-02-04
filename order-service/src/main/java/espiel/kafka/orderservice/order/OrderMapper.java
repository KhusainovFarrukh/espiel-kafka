package espiel.kafka.orderservice.order;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import espiel.kafka.orderservice.order.model.OrderCreateRequestDTO;
import espiel.kafka.orderservice.order.model.OrderDetailsResponseDTO;
import espiel.kafka.orderservice.order.model.OrderResponseDTO;
import espiel.kafka.orderservice.order.model.OrderUpdateRequestDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OrderMapper {

  OrderResponseDTO toResponseDTO(OrderEntity orderEntity);

  OrderEntity toEntity(OrderCreateRequestDTO createRequestDTO);

  @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
  OrderEntity update(
      @MappingTarget OrderEntity orderEntity, OrderUpdateRequestDTO updateRequestDTO
  );

  OrderDetailsResponseDTO toDetailsResponseDTO(OrderEntity orderEntity);

}
