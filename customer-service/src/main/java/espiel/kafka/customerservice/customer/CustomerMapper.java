package espiel.kafka.customerservice.customer;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import espiel.kafka.customerservice.customer.model.CustomerCreateRequestDTO;
import espiel.kafka.customerservice.customer.model.CustomerDetailsResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerResponseDTO;
import espiel.kafka.customerservice.customer.model.CustomerUpdateRequestDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CustomerMapper {

  CustomerEntity toEntity(CustomerCreateRequestDTO createRequestDTO);

  CustomerResponseDTO toResponseDTO(CustomerEntity entity);

  CustomerDetailsResponseDTO toDetailsResponseDTO(CustomerEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
  CustomerEntity update(@MappingTarget CustomerEntity entity, CustomerUpdateRequestDTO updateRequestDTO);

}
