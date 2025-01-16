package kr.hhplus.be.server.api.concert.presentation.dto;

import kr.hhplus.be.server.api.concert.application.dto.ConcertSeatResult;

public record ConcertSeatResponse(
	ConcertSeatResult concertSeatResult
) {

	public static ConcertSeatResponse from(ConcertSeatResult result) {
		return new ConcertSeatResponse(result);
	}
}