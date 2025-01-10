package kr.hhplus.be.server.api.service.concert.application;

import jakarta.persistence.EntityNotFoundException;
import kr.hhplus.be.server.api.service.concert.application.dto.ConcertScheduleResult;
import kr.hhplus.be.server.api.service.concert.application.dto.ConcertSeatResult;
import kr.hhplus.be.server.api.service.concert.domain.ConcertScheduleRepository;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSeat;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertService {

	private final ConcertScheduleRepository concertScheduleRepository;
	private final ConcertSeatRepository concertSeatRepository;

	@Transactional(readOnly = true)
	public ConcertScheduleResult getAvailableSchedules(long concertId) {
		ConcertScheduleResult result = ConcertScheduleResult.from(
			concertScheduleRepository.findAvailableSchedulesByConcertIdAndIsOpenTrue(concertId));
		if (result.concertSchedules().isEmpty()) {
			throw new EntityNotFoundException("예약 가능한 일정이 없습니다.");
		}
		return result;
	}

	@Transactional(readOnly = true)
	public ConcertSeatResult getAvailableSeats(long scheduleId) {
		ConcertSeatResult result = ConcertSeatResult.from(
			concertSeatRepository.findAvailableSeatsByScheduleIdAndIsReservedFalse(
				scheduleId));
		if (result.concertSeats().isEmpty()) {
			throw new EntityNotFoundException("예약 가능한 좌석이 없습니다.");
		}
		return result;
	}

	public void reserveSeat(ConcertSeat seat) {
		seat.setIsReserved(true);
		//concertSeatRepository.save(seat);
	}
}
