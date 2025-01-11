package kr.hhplus.be.server.api.service.reservation.presentation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import kr.hhplus.be.server.api.service.reservation.application.dto.ReservationResult;

public record ReservationResponse(
	long id,
	long userId,
	long seatId,
	long seatNo,
	LocalDate concertDate,
	BigDecimal finalPrice,
	boolean isReserved,
	Instant expiredAt
) {

	public static ReservationResponse from(ReservationResult result) {
		return new ReservationResponse(
			result.id(),
			result.userId(),
			result.seatId(),
			result.seatNo(),
			result.concertDate(),
			result.finalPrice(),
			result.isReserved(),
			result.expiredAt()
		);
	}
}
