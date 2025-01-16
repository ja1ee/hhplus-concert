package kr.hhplus.be.server.api.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationRepository;
import kr.hhplus.be.server.api.reservation.domain.service.ReservationService;
import kr.hhplus.be.server.api.common.exception.ErrorCode;
import org.junit.Test;


public class ReservationServiceTest {
	private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
	private final ReservationService reservationService = new ReservationService(
		reservationRepository);
	LocalDateTime mockTime = LocalDateTime.of(2025, 1, 15, 10, 30);


	@Test
	public void 임시_예약_성공() {
		// given
		ReservationDto dto = new ReservationDto(1L, 1L, 1L, 10, mockTime.toLocalDate(), BigDecimal.valueOf(50000));

		Reservation mockReservation = dto.convertToEntity();

		when(reservationRepository.findBySeatIdAndIsReservedTrue(dto.seatId())).thenReturn(null);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);

		// when
		Reservation sut = reservationService.makeReservation(dto);

		// then
		assertThat(sut.getSeatNo()).isEqualTo(mockReservation.getSeatNo());
		assertThat(sut.getIsReserved()).isEqualTo(true);
		assertThat(sut.getFinalPrice()).isEqualTo(mockReservation.getFinalPrice());
	}

	@Test
	public void 예약_확정_성공() {
		// given
		long id = 1L;
		Reservation mockReservation = Reservation.builder()
				.id(2L)
				.userId(1L)
				.seatId(2L)
				.seatNo(20)
				.concertDate(mockTime.toLocalDate())
				.finalPrice(BigDecimal.valueOf(50000))
				.isReserved(true)
				.expiredAt(LocalDateTime.now().plusMinutes(5))
				.build();

		when(reservationRepository.findById(id)).thenReturn(mockReservation);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);

		// when
		Reservation sut = reservationService.confirmReservation(id);

		// then
		assertThat(sut.getSeatNo()).isEqualTo(mockReservation.getSeatNo());
		assertThat(sut.getIsReserved()).isEqualTo(true);
		assertThat(sut.getExpiredAt()).isNull();
	}

	@Test
	public void 예약_확정_임시배정시간만료시_EXPIRED_SEAT(){
		long id = 1L;
		Reservation mockReservation = Reservation.builder()
				.id(3L)
				.userId(1L)
				.seatId(3L)
				.seatNo(30)
				.concertDate(mockTime.toLocalDate())
				.finalPrice(BigDecimal.valueOf(50000))
				.isReserved(true)
				.expiredAt(mockTime)
				.build();

		when(reservationRepository.findById(id)).thenReturn(mockReservation);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(null);

		// when & then
		assertThatThrownBy(() -> reservationService.confirmReservation(id)).hasMessage(ErrorCode.EXPIRED_SEAT.getReason());
	}

}