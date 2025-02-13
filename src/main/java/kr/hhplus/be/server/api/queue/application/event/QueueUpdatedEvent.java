package kr.hhplus.be.server.api.queue.application.event;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;

public record QueueUpdatedEvent(
        ReservationResult reservationResult
) {

    public static QueueUpdatedEvent from(ReservationResult reservationResult) {
        return new QueueUpdatedEvent(reservationResult);
    }
}
