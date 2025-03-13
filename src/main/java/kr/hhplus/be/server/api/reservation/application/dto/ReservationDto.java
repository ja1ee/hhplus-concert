package kr.hhplus.be.server.api.reservation.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import lombok.Builder;

@Builder
public record ReservationDto(
	long id,
	long userId,
	long seatId,
	int seatNo,
	long concertId,
	LocalDate concertDate,
	BigDecimal finalPrice
) {

	public Reservation convertToEntity() {
		return Reservation.builder().
			id(id).userId(userId).seatId(seatId).seatNo(seatNo).concertId(concertId).concertDate(concertDate)
			.finalPrice(finalPrice).build();
	}

}
