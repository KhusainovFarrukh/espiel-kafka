package espiel.kafka.orderservice.kafka.sentmessage;

import espiel.kafka.orderservice.kafka.sentmessage.model.SentMessageCreateDTO;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SentMessageServiceImpl implements SentMessageService {

  private final SentMessageRepository sentMessageRepository;
  private final SentMessageMapper sentMessageMapper;

  @Override
  public void createSentMessage(SentMessageCreateDTO createDTO) {
    var sentMessage = sentMessageMapper.toEntity(createDTO);
    sentMessage.setStatus(SentMessageStatus.SENT);
    sentMessageRepository.save(sentMessage);
  }

  @Override
  public void updateStatus(
      String correlationId,
      SentMessageStatus status,
      LocalDateTime consumedAt
  ) {
    var sentMessage = sentMessageRepository
        .findByCorrelationId(correlationId)
        .orElseThrow(() -> new RuntimeException("Sent message not found"));
    sentMessage.setStatus(status);
    sentMessage.setConsumedAt(consumedAt);
    sentMessageRepository.save(sentMessage);
  }

}
