package kr.hhplus.be.server.api.concert.application.dto;

import java.math.BigDecimal;
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
