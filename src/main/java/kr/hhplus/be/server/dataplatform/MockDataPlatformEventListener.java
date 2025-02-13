package kr.hhplus.be.server.dataplatform;

import kr.hhplus.be.server.api.queue.application.event.QueueUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockDataPlatformEventListener {

    private final MockDataPlatformSendService sendService;

    //@Order(1) 싱글 스레드일 경우 순서 지정 가능
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservationConfirmed(QueueUpdatedEvent event) {
        try {
            sendService.sendReservationResult(event.reservationResult());
        } catch (Exception e) {
            log.error("Reservation result sending failed", e);
        }
}
}
