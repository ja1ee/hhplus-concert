package kr.hhplus.be.server.api.service.concert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kr.hhplus.be.server.api.service.concert.application.ConcertService;
import kr.hhplus.be.server.api.service.concert.application.dto.ConcertScheduleResult;
import kr.hhplus.be.server.api.service.concert.application.dto.ConcertSeatResult;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSchedule;
import kr.hhplus.be.server.api.service.concert.domain.ConcertScheduleRepository;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSeat;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSeatRepository;
import org.junit.Test;


public class ConcertServiceTest {

	private final ConcertScheduleRepository concertScheduleRepository = mock(
		ConcertScheduleRepository.class);
	private final ConcertSeatRepository concertSeatRepository = mock(
		ConcertSeatRepository.class);
	private final ConcertService concertService = new ConcertService(concertScheduleRepository,
		concertSeatRepository);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void 예약가능날짜조회() throws ParseException {
		// given
		long concertId = 1L;
		ConcertSchedule schedule1 = ConcertSchedule.builder().concertId(concertId)
			.concertDate(sdf.parse("2025-01-01")).isOpen(true).build();
		ConcertSchedule schedule2 = ConcertSchedule.builder().concertId(concertId)
			.concertDate(sdf.parse("2025-01-02")).isOpen(true).build();

		List<ConcertSchedule> mockSchedules = Arrays.asList(schedule1, schedule2);
		when(concertScheduleRepository.findAvailableSchedulesByConcertIdAndIsOpenTrue(
			concertId))
			.thenReturn(mockSchedules);
		// when
		ConcertScheduleResult result = concertService.getAvailableSchedules(concertId);
		// then
		assertThat(result.concertSchedules()).hasSize(2)
			.extracting(ConcertSchedule::getConcertDate)
			.contains(sdf.parse("2025-01-01"), sdf.parse("2025-01-02"));
	}

	@Test
	public void 예약가능날짜조회_예약가능날짜없으면_EntityNotFoundException() {
		// given
		long concertId = 1L;
		when(concertScheduleRepository.findAvailableSchedulesByConcertIdAndIsOpenTrue(
			concertId))
			.thenReturn(Collections.emptyList());
		// when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			concertService.getAvailableSchedules(concertId);
		});
		// then
		assertEquals("예약 가능한 일정이 없습니다.", exception.getMessage());
	}


	@Test
	public void 예약가능좌석조회() {
		// given
		long scheduleId = 1L;
		ConcertSeat seat1 = ConcertSeat.builder().scheduleId(scheduleId).seatNo(10).build();
		ConcertSeat seat2 = ConcertSeat.builder().scheduleId(scheduleId).seatNo(11).build();
		List<ConcertSeat> mockSeats = Arrays.asList(seat1, seat2);
		when(concertSeatRepository.findAvailableSeatsByScheduleIdAndIsReservedFalse(
			scheduleId)).thenReturn(mockSeats);
		// when
		ConcertSeatResult result = concertService.getAvailableSeats(scheduleId);
		// then
		assertThat(result.concertSeats()).hasSize(2).extracting(ConcertSeat::getSeatNo)
			.contains(10, 11);
	}

	@Test
	public void 예약가능좌석조회_예약가능좌석없으면_EntityNotFoundException() {
		// given
		ConcertSchedule schedule = new ConcertSchedule();
		when(concertSeatRepository.findAvailableSeatsByScheduleIdAndIsReservedFalse(
			schedule.getId()))
			.thenReturn(Collections.emptyList());
		// when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			concertService.getAvailableSeats(schedule.getId());
		});
		// then
		assertEquals("예약 가능한 좌석이 없습니다.", exception.getMessage());
	}


}
