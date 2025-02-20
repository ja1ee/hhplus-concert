package kr.hhplus.be.server.api.user.application.event;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;

public record PaymentProcessedEvent(
        ReservationResult reservationResult
) {

    public static PaymentProcessedEvent from(ReservationResult reservationResult) {
            return new PaymentProcessedEvent(reservationResult);
    }
}
