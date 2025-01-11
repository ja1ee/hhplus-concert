package kr.hhplus.be.server.api.service.concert.application.dto;

import java.util.List;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSeat;

public record ConcertSeatResult(
	List<ConcertSeat> concertSeats
) {

	public static ConcertSeatResult from(List<ConcertSeat> concertSeats) {
		return new ConcertSeatResult(concertSeats);
	}
}