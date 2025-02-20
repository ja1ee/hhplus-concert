package kr.hhplus.be.server.api.reservation.presentation.consumer;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.application.event.ReservationConfirmedEvent;
import kr.hhplus.be.server.api.reservation.domain.entity.ReservationOutbox;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationOutboxRepository;
import kr.hhplus.be.server.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static kr.hhplus.be.server.common.exception.ErrorCode.NOT_FOUND_RESERVATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationConsumer {

    private final ReservationOutboxRepository outboxRepository;

    @KafkaListener(topics = "sending-reservation-request", groupId = "reservation-group")
    public void publishRecord(ReservationConfirmedEvent eventPayload) {

        ReservationResult result = eventPayload.reservationResult();
        long reservationId = result.id();

        ReservationOutbox reservationOutbox = findOutboxById(reservationId);
        reservationOutbox.published();

        outboxRepository.save(reservationOutbox);
    }

    private ReservationOutbox findOutboxById(long reservationId) {
        return outboxRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_RESERVATION));
    }

}
