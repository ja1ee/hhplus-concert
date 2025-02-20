package kr.hhplus.be.server.dataplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockDataPlatformEventConsumer {

    private final ObjectMapper objectMapper;
    private final MockDataPlatformSendService sendService;

    @KafkaListener(topics = "sending-reservation-request", groupId = "reservation-group")
    public void sendReservationInfo(byte[] eventPayload) {
        try {
            ReservationResult result = objectMapper.readValue(eventPayload, ReservationResult.class);
            sendService.sendReservationResult(result);
        } catch (Exception e) {
            log.error("Error processing Kafka message", e);
        }
    }
}
