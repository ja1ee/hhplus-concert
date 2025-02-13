package kr.hhplus.be.server.dataplatform;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MockDataPlatformSendService {
    public void sendReservationResult(ReservationResult result) {
        // Mocking the event reservation information processing
        if (result != null) {
            log.info("Mock Event: Sending reservation info for reservationId: {}", result.id());
            log.info("Reservation Details: {}", result);
        } else {
            log.warn("Mock Event: Received null reservation");
        }
    }
}
