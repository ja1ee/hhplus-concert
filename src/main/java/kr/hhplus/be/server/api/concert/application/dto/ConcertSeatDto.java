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
}
