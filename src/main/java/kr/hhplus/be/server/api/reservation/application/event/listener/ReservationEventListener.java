package kr.hhplus.be.server.api.reservation.application.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.api.reservation.application.event.ReservationConfirmedEvent;
import kr.hhplus.be.server.api.reservation.application.service.MessageProducer;
import kr.hhplus.be.server.api.reservation.domain.entity.ReservationOutbox;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReservationEventListener {

    private final ReservationOutboxRepository reservationOutboxRepository;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    String topicName = "sending-reservation-request";

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(ReservationConfirmedEvent event) {
        ReservationOutbox outbox = new ReservationOutbox(topicName, event.reservationResult(), LocalDateTime.now(), objectMapper);
        reservationOutboxRepository.save(outbox);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendReservationInfo(ReservationConfirmedEvent event) {
        messageProducer.send(topicName, event.reservationResult());
    }
}
