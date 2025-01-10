package kr.hhplus.be.server.api.service.concert.application.dto;

import java.util.List;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSchedule;

public record ConcertScheduleResult(
	List<ConcertSchedule> concertSchedules
) {

	public static ConcertScheduleResult from(List<ConcertSchedule> concertSchedules) {
		return new ConcertScheduleResult(concertSchedules);
	}
}