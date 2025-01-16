package kr.hhplus.be.server.api.concert.application.dto;

import java.util.List;
import kr.hhplus.be.server.api.concert.domain.entity.ConcertSchedule;

public record ConcertScheduleResult(
	List<ConcertSchedule> concertSchedules
) {

	public static ConcertScheduleResult from(List<ConcertSchedule> concertSchedules) {
		return new ConcertScheduleResult(concertSchedules);
	}
}