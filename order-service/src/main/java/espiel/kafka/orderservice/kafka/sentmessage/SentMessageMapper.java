package espiel.kafka.orderservice.kafka.sentmessage;

import espiel.kafka.orderservice.kafka.sentmessage.model.SentMessageCreateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SentMessageMapper {

  SentMessageEntity toEntity(SentMessageCreateDTO createDTO);

}
