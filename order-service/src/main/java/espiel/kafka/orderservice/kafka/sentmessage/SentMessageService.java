package espiel.kafka.orderservice.kafka.sentmessage;

import espiel.kafka.orderservice.kafka.sentmessage.model.SentMessageCreateDTO;

public interface SentMessageService {

  void createSentMessage(SentMessageCreateDTO createDTO);

}
