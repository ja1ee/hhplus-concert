package kr.hhplus.be.server.api.reservation.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import kr.hhplus.be.server.api.concert.application.dto.ConcertSeatDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;

public record ReservationRequest(
	@Schema(description = "예약 ID", example = "1")
	@NotNull
	long id,

	@Schema(description = "사용자 고유 ID", example = "12345")
	long userId,

	@Schema(description = "예약 신청한 좌석", example = "10")
	ConcertSeatDto seatDto,

	@Schema(description = "콘서트 ID", example = "1")
	long concertId,

	@Schema(description = "콘서트 날짜", example = "2025-01-01")
	LocalDate concertDate,

	@Schema(description = "예매 시점 가격", example = "50000")
	BigDecimal finalPrice
) {

	public ReservationDto toDto() {
		return new ReservationDto(
			id,
			userId,
			seatDto.id(),
			seatDto.seatNo(),
			concertId,
			concertDate,
			finalPrice
		);
	}
}