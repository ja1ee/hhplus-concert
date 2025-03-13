package kr.hhplus.be.server.api.reservation.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationStatus;
import kr.hhplus.be.server.api.reservation.application.service.ReservationService;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationRepository;
import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;
import kr.hhplus.be.server.api.user.domain.entity.User;
import kr.hhplus.be.server.api.user.domain.repository.UserRepository;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.api.user.application.service.UserService;
import kr.hhplus.be.server.util.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationFacadeIntegrationTest {

	@Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;

	@Autowired
	private UserRepository userRepository;
    @Autowired
    private ConcertSeatRepository concertSeatRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private DatabaseCleaner databaseCleaner;

	LocalDateTime mockTime = LocalDateTime.of(2025, 1, 15, 10, 30);

	@BeforeEach
	void setUp() {
		databaseCleaner.clear();
		userRepository.save(User.builder().balance(BigDecimal.valueOf(100_000)).build());
		concertSeatRepository.save(ConcertSeat.builder().scheduleId(1L).seatNo(10).price(BigDecimal.valueOf(55_000)).isReserved(false).build());
	}

	@Test
	void 임시_예약_성공() {
		// given
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 10, 1L, mockTime.toLocalDate(), BigDecimal.valueOf(55000));

		// when
		ReservationResult sut = reservationService.makeReservation(dto);

		// then
		assertThat(sut.status()).isEqualTo(ReservationStatus.pending);
		assertThat(sut.seatNo()).isEqualTo(dto.seatNo());
		List<Reservation> list = reservationRepository.findAll();
		assertThat(list).hasSize(1);
	}

	@Test
	void 예약_결제_성공() {
		// given
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 12,1L, mockTime.toLocalDate(), BigDecimal.valueOf(55_000));

		// when
		reservationService.makeReservation(dto);
		ReservationResult sut = reservationService.confirmReservation(dto.id());
		User user = userRepository.findById(1L).orElseThrow();

		// then
		assertThat(sut.status()).isEqualTo(ReservationStatus.reserved);
		assertThat(sut.seatNo()).isEqualTo(dto.seatNo());
		assertThat(user.getBalance().setScale(0, RoundingMode.DOWN)).isEqualTo(BigDecimal.valueOf(45_000)); // 100_000 - 55_000
	}

	@Test
	void 예약_결제_잔액부족시_INSUFFICIENT_BALANCE() {
		// given
		userService.payAmount(new BalanceHistoryDto(1L, BalanceHistoryType.PAY, BigDecimal.valueOf(100_000))); // 잔액 0으로 만듦
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 12, 1L, mockTime.toLocalDate(), BigDecimal.valueOf(55_000));

		// when & then
		reservationService.makeReservation(dto);
		assertThatThrownBy(() -> reservationService.confirmReservation(dto.id())).hasMessage(ErrorCode.INSUFFICIENT_BALANCE.getReason());
	}
}
