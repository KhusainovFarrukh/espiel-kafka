package espiel.kafka.orderservice.kafka.sentmessage;

import espiel.kafka.orderservice.kafka.sentmessage.model.SentMessageCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SentMessageServiceImpl implements SentMessageService {

  private final SentMessageRepository sentMessageRepository;
  private final SentMessageMapper sentMessageMapper;

  @Override
  public void createSentMessage(SentMessageCreateDTO createDTO) {
    sentMessageRepository.save(sentMessageMapper.toEntity(createDTO));
  }

}
