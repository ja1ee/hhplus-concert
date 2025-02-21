package kr.hhplus.be.server.dataplatform;

import kr.hhplus.be.server.api.reservation.application.event.ReservationConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockDataPlatformEventConsumer {

    private final MockDataPlatformSendService sendService;

    @KafkaListener(topics = "sending-reservation-request", groupId = "reservation-group")
    public void sendReservationInfo(ReservationConfirmedEvent event) {
        try {
            sendService.sendReservationResult(event.reservationResult());
        } catch (Exception e) {
            log.error("Error processing Kafka message", e);
        }
    }
}
