package kr.hhplus.be.server.api.reservation.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;

public record ReservationResponse(
	long id,
	long userId,
	long seatId,
	long seatNo,
	LocalDate concertDate,
	BigDecimal finalPrice,
	boolean isReserved,
	LocalDateTime expiredAt
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
