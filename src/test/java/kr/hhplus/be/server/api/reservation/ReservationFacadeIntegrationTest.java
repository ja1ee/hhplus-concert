package kr.hhplus.be.server.api.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.api.reservation.application.service.ReservationService;
import kr.hhplus.be.server.api.reservation.presentation.facade.ReservationFacade;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class ReservationFacadeIntegrationTest {

	@Autowired
    private ReservationFacade reservationFacade;
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
	void setUp(@Autowired JdbcTemplate jdbcTemplate) {
		databaseCleaner.clear();
		jdbcTemplate.execute("ALTER TABLE reservation MODIFY COLUMN expired_at DATETIME NULL"); // 테스트 컨테이너라서 별도 작성

		userRepository.save(User.builder().balance(BigDecimal.valueOf(100_000)).build());
		concertSeatRepository.save(ConcertSeat.builder().scheduleId(1L).seatNo(10).price(BigDecimal.valueOf(55_000)).isReserved(false).build());
	}

	@Test
	void 임시_예약_성공() {
		// given
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 10, mockTime.toLocalDate(), BigDecimal.valueOf(55000));

		// when
		ReservationResult sut = reservationFacade.makeReservation(dto);

		// then
		assertThat(sut.isReserved()).isEqualTo(true);
		assertThat(sut.seatNo()).isEqualTo(dto.seatNo());
		assertThat(sut.expiredAt()).isNotNull();
		List<Reservation> list = reservationRepository.findAll();
		assertThat(list).hasSize(1);
	}

	@Test
	void 임시_예약_이미예약된좌석일시_ALREADY_RESERVED_SEAT() {
		// given
		ReservationDto successRes = new ReservationDto(1L, 1L, 1L, 10, mockTime.toLocalDate(), BigDecimal.valueOf(55000));
		ReservationDto failRes = new ReservationDto(1L, 2L, 1L, 10, mockTime.toLocalDate(), BigDecimal.valueOf(55000));

		// when
		reservationFacade.makeReservation(successRes);

		// then
		assertThatThrownBy(() -> reservationFacade.makeReservation(failRes)).hasMessage(ErrorCode.ALREADY_RESERVED_SEAT.getReason());
		Reservation sut = reservationRepository.findBySeatIdAndIsReservedTrue(1L);
		assertThat(sut.getUserId()).isEqualTo(1L);
	}

	@Test
	void 예약_결제_성공() {
		// given
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 12, mockTime.toLocalDate(), BigDecimal.valueOf(55_000));

		// when
		reservationFacade.makeReservation(dto);
		ReservationResult sut = reservationService.confirmReservation(dto.id());
		User user = userRepository.findById(1L).orElseThrow();

		// then
		assertThat(sut.isReserved()).isEqualTo(true);
		assertThat(sut.seatNo()).isEqualTo(dto.seatNo());
		assertThat(sut.expiredAt()).isNull();
		assertThat(user.getBalance().setScale(0, RoundingMode.DOWN)).isEqualTo(BigDecimal.valueOf(45_000)); // 100_000 - 55_000
	}

	@Test
	void 예약_결제_잔액부족시_INSUFFICIENT_BALANCE() {
		// given
		userService.payAmount(new BalanceHistoryDto(1L, BalanceHistoryType.PAY, BigDecimal.valueOf(100_000))); // 잔액 0으로 만듦
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 12, mockTime.toLocalDate(), BigDecimal.valueOf(55_000));

		// when & then
		reservationFacade.makeReservation(dto);
		assertThatThrownBy(() -> reservationService.confirmReservation(dto.id())).hasMessage(ErrorCode.INSUFFICIENT_BALANCE.getReason());
	}
}
