package kr.hhplus.be.server.api.reservation.application.event;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;

public record ReservationConfirmedEvent(
        ReservationResult reservationResult
) {

    public static ReservationConfirmedEvent from(ReservationResult reservationResult) {
        return new ReservationConfirmedEvent(
                reservationResult
        );
    }
}
