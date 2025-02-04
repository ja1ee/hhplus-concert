package kr.hhplus.be.server.api.concert.domain.service;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.aop.lock.RedisLock;
import kr.hhplus.be.server.api.concert.application.dto.ConcertScheduleResult;
import kr.hhplus.be.server.api.concert.application.dto.ConcertSeatResult;
import kr.hhplus.be.server.api.concert.domain.entity.ConcertSchedule;
import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertScheduleRepository;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

	private final ConcertScheduleRepository concertScheduleRepository;
	private final ConcertSeatRepository concertSeatRepository;

	@Transactional(readOnly = true)
	public ConcertScheduleResult getAvailableSchedules(long concertId) {
		List<ConcertSchedule> schedules = concertScheduleRepository.findAvailableSchedulesByConcertIdAndIsOpenTrue(concertId);

		if (schedules.isEmpty()) {
			throw new CustomException(ErrorCode.NOT_FOUND_SCHEDULE);
		}

		return ConcertScheduleResult.from(schedules);
	}

	@Transactional(readOnly = true)
	public ConcertSeatResult getAvailableSeats(long scheduleId) {
		List<ConcertSeat> seats = concertSeatRepository.findAvailableSeatsByScheduleIdAndIsReservedFalse(scheduleId);

		if (seats.isEmpty()) {
			throw new CustomException(ErrorCode.NOT_FOUND_SEAT);
		}

		return ConcertSeatResult.from(seats);
	}

	@RedisLock(prefix = "seat:", key = "#seatId")
	public void reserveSeat(long seatId) {
		ConcertSeat seat = concertSeatRepository.findById(seatId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEAT));

		if (seat.getIsReserved()) {
			throw new CustomException(ErrorCode.ALREADY_RESERVED_SEAT);
		}
		seat.reserve();
		concertSeatRepository.save(seat);
	}

	public void expireSeat(long seatId) {
		ConcertSeat seat = concertSeatRepository.findById(seatId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEAT));

		seat.expire();
		concertSeatRepository.save(seat);
	}
}
