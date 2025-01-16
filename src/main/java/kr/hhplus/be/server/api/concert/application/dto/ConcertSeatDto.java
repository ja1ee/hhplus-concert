package kr.hhplus.be.server.api.concert.application.dto;

import java.math.BigDecimal;
import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import lombok.Builder;

@Builder
public record ConcertSeatDto(
	long id,
	long scheduleId,
	int seatNo,
	BigDecimal price,
	Boolean isReserved
) {

	public ConcertSeat convertToEntity() {
		return ConcertSeat.builder().
			id(id).scheduleId(scheduleId).seatNo(seatNo).price(price).isReserved(isReserved)
			.build();
	}

	public static ConcertSeatDto convertFromEntity(ConcertSeat entity) {
		return new ConcertSeatDto(
			entity.getId(),
			entity.getScheduleId(),
			entity.getSeatNo(),
			entity.getPrice(),
			entity.getIsReserved()
		);
	}

}
