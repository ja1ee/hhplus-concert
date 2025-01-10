package kr.hhplus.be.server.api.service.reservation.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import kr.hhplus.be.server.api.service.concert.application.dto.ConcertSeatDto;
import kr.hhplus.be.server.api.service.reservation.domain.Reservation;
import lombok.Builder;

@Builder
public record ReservationDto(
	long userId,
	ConcertSeatDto seatDto,
	LocalDate concertDate,
	BigDecimal finalPrice
) {

	public Reservation convertToEntity() {
		return Reservation.builder().
			userId(userId).seatNo(seatDto.seatNo()).concertDate(concertDate)
			.finalPrice(finalPrice).build();
	}

}
