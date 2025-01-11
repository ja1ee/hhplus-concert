package kr.hhplus.be.server.api.service.reservation.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import kr.hhplus.be.server.api.service.reservation.domain.Reservation;

public record ReservationResult(
	long id,
	long userId,
	long seatId,
	long seatNo,
	LocalDate concertDate,
	BigDecimal finalPrice,
	boolean isReserved,
	Instant expiredAt
) {

	public static ReservationResult from(Reservation reservation) {
		return new ReservationResult(
			reservation.getId(),
			reservation.getUserId(),
			reservation.getSeatId(),
			reservation.getSeatNo(),
			reservation.getConcertDate(),
			reservation.getFinalPrice(),
			reservation.getIsReserved(),
			reservation.getExpiredAt()
		);
	}
}
