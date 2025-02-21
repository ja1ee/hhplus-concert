package kr.hhplus.be.server.api.reservation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationOutboxScheduler {

    private final ReservationOutboxRepository outboxRepository;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void processOutboxEvents() {
        outboxRepository.findTop10ByPublishedFalseOrderByCreatedAtAsc().forEach(outbox -> {
            if(outbox.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
                try {
                    ReservationResult result = objectMapper.readValue(outbox.getPayload(), ReservationResult.class);
                    messageProducer.send(outbox.getEventType(), result);
                } catch (JsonProcessingException e) {
                    log.error("Error Processing Outbox Event", e);
                }
            }
        });
    }
}
