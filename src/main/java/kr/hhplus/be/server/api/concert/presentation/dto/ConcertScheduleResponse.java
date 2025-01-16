package kr.hhplus.be.server.api.concert.presentation.dto;

import kr.hhplus.be.server.api.concert.application.dto.ConcertScheduleResult;

public record ConcertScheduleResponse(
	ConcertScheduleResult concertScheduleResult
) {

	public static ConcertScheduleResponse from(ConcertScheduleResult result) {
		return new ConcertScheduleResponse(result);
	}
}