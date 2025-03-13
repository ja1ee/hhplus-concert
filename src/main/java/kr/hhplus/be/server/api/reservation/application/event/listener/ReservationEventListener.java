package kr.hhplus.be.server.api.reservation.application.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.api.concert.application.event.ReserveSeatFailedEvent;
import kr.hhplus.be.server.api.concert.application.event.ReserveSeatSucceedEvent;
import kr.hhplus.be.server.api.reservation.application.event.ReservationConfirmedEvent;
import kr.hhplus.be.server.api.reservation.application.service.MessageProducer;
import kr.hhplus.be.server.api.reservation.application.service.ReservationService;
import kr.hhplus.be.server.api.reservation.domain.entity.ReservationOutbox;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationOutboxRepository;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReservationEventListener {

    private final ReservationService reservationService;
    private final ReservationOutboxRepository reservationOutboxRepository;
    private final ReservationRepository reservationRepository;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    String topicName = "sending-reservation-request";

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(ReservationConfirmedEvent event) {
        ReservationOutbox outbox = new ReservationOutbox(topicName, event.reservationResult(), LocalDateTime.now(), objectMapper);
        reservationOutboxRepository.save(outbox);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendReservationInfo(ReservationConfirmedEvent event) {
        messageProducer.send(topicName, event.reservationResult());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSeatSucceedEvent(ReserveSeatSucceedEvent event) {
        reservationService.successReservation(event);
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSeatFailedEvent(ReserveSeatFailedEvent event) {
        reservationService.failReservation(event);
    }
}
