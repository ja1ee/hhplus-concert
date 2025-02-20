package kr.hhplus.be.server.api.queue.application.event.listener;

import kr.hhplus.be.server.api.queue.application.service.QueueService;
import kr.hhplus.be.server.api.reservation.application.event.ReservationConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class QueueEventListener {

    private final QueueService queueService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationConfirmedHandler(ReservationConfirmedEvent event) {
        try {
            queueService.removeFromRunQueue(event.reservationResult());
        } catch (Exception e) {
            log.error("Deleting Token failed", e);
        }
    }
}
