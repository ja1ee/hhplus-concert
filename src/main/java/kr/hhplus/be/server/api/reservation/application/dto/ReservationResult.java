package kr.hhplus.be.server.api.reservation.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;

public record ReservationResult(
	long id,
	long userId,
	long seatId,
	long seatNo,
	LocalDate concertDate,
	BigDecimal finalPrice,
	boolean isReserved,
	LocalDateTime expiredAt
) {

	public static ReservationResult from(Reservation reservation) {
		return new ReservationResult(
			reservation.getId(),
			reservation.getUserId(),
			reservation.getSeatId(),
			reservation.getSeatNo(),
			reservation.getConcertDate(),
			reservation.getFinalPrice(),
			reservation.isReserved(),
			reservation.getExpiredAt()
		);
	}
}
