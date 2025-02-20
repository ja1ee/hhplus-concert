package kr.hhplus.be.server.api.reservation.presentation.consumer;

import kr.hhplus.be.server.api.reservation.application.event.ReservationConfirmedEvent;
import kr.hhplus.be.server.api.reservation.application.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationConsumer {

    private final ReservationService reservationService;

    @KafkaListener(topics = "sending-reservation-request", groupId = "reservation-group")
    public void publishRecord(ReservationConfirmedEvent eventPayload) {
        reservationService.publishRecord(eventPayload);
    }
}
