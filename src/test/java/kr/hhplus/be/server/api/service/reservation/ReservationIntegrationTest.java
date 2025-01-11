package kr.hhplus.be.server.api.service.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import kr.hhplus.be.server.api.exception.ReservedSeatException;
import kr.hhplus.be.server.api.service.concert.application.dto.ConcertSeatDto;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSeat;
import kr.hhplus.be.server.api.service.concert.domain.ConcertSeatRepository;
import kr.hhplus.be.server.api.service.reservation.application.ReservationFacade;
import kr.hhplus.be.server.api.service.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.service.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.service.reservation.domain.Reservation;
import kr.hhplus.be.server.api.service.user.application.UserService;
import kr.hhplus.be.server.api.service.user.domain.User;
import kr.hhplus.be.server.api.service.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ReservationIntegrationTest {

	@Autowired
	ReservationFacade reservationFacade;
	@Autowired
	UserRepository userRepository;

	@BeforeEach
	@Transactional
	public void setUp() {
		userRepository.save(new User(1L, BigDecimal.valueOf(100_000)));
	}

	@Test
	@Transactional
	public void 좌석예약요청_성공() {
		// given
		ConcertSeatDto seatDto = new ConcertSeatDto(4L, 2L, 21, BigDecimal.valueOf(65000), false);

		ReservationDto dto = new ReservationDto(1L, seatDto, LocalDate.now(),
			BigDecimal.valueOf(55000));

		// when
		ReservationResult result = reservationFacade.makeReservation(dto);

		// then
		assertThat(result.isReserved()).isEqualTo(true);
	}

	@Test
	@Transactional
	public void 좌석예약요청_실패_이미예약된좌석() {
		// given
		ConcertSeatDto seatDto = new ConcertSeatDto(4L, 2L, 20, BigDecimal.valueOf(65000), false);
		ReservationDto dto = new ReservationDto(1L, seatDto, LocalDate.now(),
			BigDecimal.valueOf(55000));

		reservationFacade.makeReservation(dto);

		// when
		ReservedSeatException exception = assertThrows(ReservedSeatException.class, () -> {
			reservationFacade.makeReservation(dto);
		});

		// then
		assertEquals("이미 예약된 좌석입니다.", exception.getMessage());
	}

//	@Test
//	@Transactional
//	public void 좌석결제_성공() {
//		ConcertSeatDto seatDto = new ConcertSeatDto(4L, 2L, 21, BigDecimal.valueOf(65000), false);
//
//		ReservationDto dto = new ReservationDto(1L, seatDto, LocalDate.now(),
//			BigDecimal.valueOf(55000));
//		reservationFacade.makeReservation(dto);
//		ReservationResult result = reservationFacade.payAmountAndConfirmReservation(dto);
//		assertThat(result).isNotNull();
//	}


}
