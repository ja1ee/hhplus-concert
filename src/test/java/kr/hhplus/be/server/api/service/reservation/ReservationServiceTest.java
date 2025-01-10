package kr.hhplus.be.server.api.service.reservation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import kr.hhplus.be.server.api.service.concert.application.dto.ConcertSeatDto;
import kr.hhplus.be.server.api.service.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.service.reservation.domain.Reservation;
import kr.hhplus.be.server.api.service.reservation.domain.ReservationRepository;
import kr.hhplus.be.server.api.service.reservation.domain.ReservationService;
import kr.hhplus.be.server.api.exception.ReservedSeatException;
import org.junit.Test;


public class ReservationServiceTest {

	// 동시성 이슈 없이 좌석예약 성공하는지 확인

	private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
	private final ReservationService reservationService = new ReservationService(
		reservationRepository);


	@Test
	public void 좌석예약_성공() throws ParseException {
		// given
		ConcertSeatDto seatDto = new ConcertSeatDto(1L, 1L, 10, BigDecimal.valueOf(50000), false);
		ReservationDto dto = new ReservationDto(1L, seatDto, LocalDate.now(),
			BigDecimal.valueOf(50000));

		Reservation mockReservation = dto.convertToEntity();

		when(reservationRepository.findBySeatNoAndIsReservedTrue(seatDto.id())).thenReturn(
			null);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);

		// when
		Reservation result = reservationService.makeReservation(dto);

		// then
		assertNotNull(result);
	}

	@Test
	public void 좌석예약_이미예약된좌석일시_ReservedSeatException() throws ParseException {
		// given
		ConcertSeatDto seatDto = new ConcertSeatDto(1L, 1L, 10, BigDecimal.valueOf(50000), false);
		ReservationDto dto = new ReservationDto(1L, seatDto, LocalDate.now(),
			BigDecimal.valueOf(50000));

		Reservation mockReservation = dto.convertToEntity();

		when(reservationRepository.findBySeatNoAndIsReservedTrue(seatDto.id())).thenReturn(
			mockReservation);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(null);

		// when
		ReservedSeatException exception = assertThrows(ReservedSeatException.class, () -> {
			reservationService.makeReservation(dto);
		});

		// then
		assertEquals("이미 예약된 좌석입니다.", exception.getMessage());
		verify(reservationRepository, times(1)).findBySeatNoAndIsReservedTrue(seatDto.id());
		verify(reservationRepository, times(0)).save(any(Reservation.class));
	}

}