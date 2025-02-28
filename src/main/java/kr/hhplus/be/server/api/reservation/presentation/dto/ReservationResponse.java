package kr.hhplus.be.server.api.reservation.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationStatus;

public record ReservationResponse(
	Long id,
	Long userId,
	Long seatId,
	int seatNo,
	Long concertId,
	LocalDate concertDate,
	BigDecimal finalPrice,
	ReservationStatus status,
	LocalDateTime expiredAt
) {

	public static ReservationResponse from(ReservationResult result) {
		return new ReservationResponse(
			result.id(),
			result.userId(),
			result.seatId(),
			result.seatNo(),
			result.concertId(),
			result.concertDate(),
			result.finalPrice(),
			result.status(),
			result.expiredAt()
		);
	}
}
