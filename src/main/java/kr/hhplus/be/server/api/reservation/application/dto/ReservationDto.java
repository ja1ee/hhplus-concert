package kr.hhplus.be.server.api.reservation.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import kr.hhplus.be.server.api.concert.application.dto.ConcertSeatDto;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import lombok.Builder;

@Builder
public record ReservationDto(
	long id,
	long userId,
	long seatId,
	long seatNo,
	LocalDate concertDate,
	BigDecimal finalPrice
) {

	public Reservation convertToEntity() {
		return Reservation.builder().
			id(id).userId(userId).seatId(seatId).seatNo(seatNo).concertDate(concertDate)
			.finalPrice(finalPrice).build();
	}

}
