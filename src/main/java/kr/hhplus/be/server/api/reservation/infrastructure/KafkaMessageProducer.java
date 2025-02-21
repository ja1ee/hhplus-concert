package kr.hhplus.be.server.api.reservation.infrastructure;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.application.service.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageProducer implements MessageProducer {
    private final KafkaTemplate<String, ReservationResult> kafkaTemplate;

    @Override
    public void send(String topic, ReservationResult message) {
            kafkaTemplate.send(topic, message);
    }
}
