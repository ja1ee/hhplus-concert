package kr.hhplus.be.server.api.service.concert.presentation.dto;

import kr.hhplus.be.server.api.service.concert.application.dto.ConcertScheduleResult;

public record ConcertScheduleResponse(
	ConcertScheduleResult concertScheduleResult
) {

	public static ConcertScheduleResponse from(ConcertScheduleResult result) {
		return new ConcertScheduleResponse(result);
	}
}