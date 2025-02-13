package kr.hhplus.be.server.api.concert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

import kr.hhplus.be.server.api.concert.application.service.ConcertService;
import kr.hhplus.be.server.api.concert.application.dto.ConcertScheduleResult;
import kr.hhplus.be.server.api.concert.application.dto.ConcertSeatResult;
import kr.hhplus.be.server.api.concert.domain.entity.ConcertSchedule;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertScheduleRepository;
import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.common.exception.ErrorCode;
import org.junit.Test;

//@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {

	private final ConcertScheduleRepository concertScheduleRepository = mock(
		ConcertScheduleRepository.class);
	private final ConcertSeatRepository concertSeatRepository = mock(
		ConcertSeatRepository.class);
	private final ConcertService concertService = new ConcertService(concertScheduleRepository,
		concertSeatRepository);

	@Test
	public void 예약가능날짜조회() throws ParseException {
		// given
		long concertId = 1L;
		List<ConcertSchedule> mockSchedules = Arrays.asList(
				createSchedule(concertId, "2025-01-01", true),
				createSchedule(concertId, "2025-01-02", true)
		);
		when(concertScheduleRepository.findAvailableSchedulesByConcertIdAndIsOpenTrue(concertId))
			.thenReturn(mockSchedules);

		// when
		ConcertScheduleResult sut = concertService.getAvailableSchedules(concertId);

		// then
		assertThat(sut.concertSchedules())
				.hasSize(2)
				.extracting(ConcertSchedule::getConcertDate)
				.contains(LocalDate.parse("2025-01-01"), LocalDate.parse("2025-01-02"));
		assertThat(sut.concertSchedules())
				.extracting(ConcertSchedule::getIsOpen)
				.contains(true, true);
	}

	@Test
	public void 예약가능날짜조회_예약가능날짜없으면_NOT_FOUND_SCHEDULE() {
		// given
		when(concertScheduleRepository.findAvailableSchedulesByConcertIdAndIsOpenTrue(1L))
			.thenReturn(List.of());

		// when & then
		assertThatThrownBy(() -> concertService.getAvailableSchedules(1L)).hasMessage(ErrorCode.NOT_FOUND_SCHEDULE.getReason());
	}

	@Test
	public void 예약가능좌석조회() {
		// given
		long scheduleId = 1L;
		List<ConcertSeat> mockSeats = Arrays.asList(createSeat(scheduleId, 10), createSeat(scheduleId, 11));
		when(concertSeatRepository.findAvailableSeatsByScheduleIdAndIsReservedFalse(
			scheduleId)).thenReturn(mockSeats);

		// when
		ConcertSeatResult result = concertService.getAvailableSeats(scheduleId);

		// then
		assertThat(result.concertSeats()).hasSize(2).extracting(ConcertSeat::getScheduleId)
			.contains(1L, 1L);
		assertThat(result.concertSeats()).extracting(ConcertSeat::getSeatNo)
			.contains(10, 11);
	}

	@Test
	public void 예약가능좌석조회_예약가능좌석없으면_NOT_FOUND_SEAT() {
		// given
		ConcertSchedule schedule = ConcertSchedule.builder().concertId(1L).build();
		when(concertSeatRepository.findAvailableSeatsByScheduleIdAndIsReservedFalse(
			schedule.getId()))
			.thenReturn(null);

		// when
		assertThatThrownBy(() -> concertService.getAvailableSeats(1L)).hasMessage(ErrorCode.NOT_FOUND_SEAT.getReason());
	}

	private ConcertSchedule createSchedule(long concertId, String date, boolean isOpen) {
		return ConcertSchedule.builder()
				.concertId(concertId)
				.concertDate(LocalDate.parse(date))
				.isOpen(isOpen)
				.build();
	}

	private ConcertSeat createSeat(long scheduleId, int seatNo) {
		return ConcertSeat.builder()
				.scheduleId(scheduleId)
				.seatNo(seatNo)
				.build();
	}

}
