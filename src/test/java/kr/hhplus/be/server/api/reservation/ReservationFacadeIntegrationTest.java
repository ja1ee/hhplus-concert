package kr.hhplus.be.server.api.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.api.reservation.application.ReservationFacade;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.token.domain.entity.Token;
import kr.hhplus.be.server.api.token.domain.repository.TokenRepository;
import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;
import kr.hhplus.be.server.api.user.domain.entity.User;
import kr.hhplus.be.server.api.user.domain.repository.UserRepository;
import kr.hhplus.be.server.api.common.exception.ErrorCode;
import kr.hhplus.be.server.api.user.domain.service.UserService;
import kr.hhplus.be.server.util.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class ReservationFacadeIntegrationTest {

	@Autowired
    ReservationFacade reservationFacade;
	@Autowired
	UserRepository userRepository;
    @Autowired
    private ConcertSeatRepository concertSeatRepository;
    @Autowired
    private TokenRepository tokenRepository;
	@Autowired
	DatabaseCleaner databaseCleaner;
    @Autowired
    private UserService userService;

	LocalDateTime mockTime = LocalDateTime.of(2025, 1, 15, 10, 30);

	@BeforeEach
	void setUp(@Autowired JdbcTemplate jdbcTemplate) {
		databaseCleaner.clear();
		jdbcTemplate.execute("ALTER TABLE reservation MODIFY COLUMN expired_at DATETIME NULL"); // 테스트 컨테이너라서 별도 작성

		userRepository.save(User.builder().balance(BigDecimal.valueOf(100_000)).build());
		tokenRepository.save(Token.builder().userId(1L).isActivated(true).expiredAt(mockTime.plusMinutes(10)).build());
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
	}

	@Test
	void 임시_예약_이미예약된좌석일시_ALREADY_RESERVED_SEAT() {
		// given
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 11, mockTime.toLocalDate(), BigDecimal.valueOf(55000));

		// when
		reservationFacade.makeReservation(dto);

		// then
		assertThatThrownBy(() -> reservationFacade.makeReservation(dto)).hasMessage(ErrorCode.ALREADY_RESERVED_SEAT.getReason());
	}

	@Test
	void 예약_결제_성공() {
		// given
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 12, mockTime.toLocalDate(), BigDecimal.valueOf(55_000));

		// when
		reservationFacade.makeReservation(dto);
		ReservationResult sut = reservationFacade.payAmountAndConfirmReservation(dto);
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
		assertThatThrownBy(() -> reservationFacade.payAmountAndConfirmReservation(dto)).hasMessage(ErrorCode.INSUFFICIENT_BALANCE.getReason());
	}
}
