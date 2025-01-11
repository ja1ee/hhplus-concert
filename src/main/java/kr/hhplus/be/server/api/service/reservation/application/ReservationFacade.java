package kr.hhplus.be.server.api.service.reservation.application;

import kr.hhplus.be.server.api.service.concert.application.ConcertService;
import kr.hhplus.be.server.api.service.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.service.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.service.reservation.domain.Reservation;
import kr.hhplus.be.server.api.service.reservation.domain.ReservationService;
import kr.hhplus.be.server.api.service.token.application.TokenService;
import kr.hhplus.be.server.api.service.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationFacade {

	private final ReservationService reservationService;
	private final UserService userService;
	private final TokenService tokenService;
	private final ConcertService concertService;

	@Transactional
	public ReservationResult payAmountAndConfirmReservation(ReservationDto dto) {
		// 결제 처리
		userService.payAmount(dto);
		// 좌석 만료 여부 확인
		Reservation reservation = reservationService.checkIfReservationExpired(dto.seatDto());
		// 예약 확정 후 예약 정보 반환
		Reservation confirmdReservation = reservationService.confirmReservation(reservation);
		// 대기열 토큰 만료
		tokenService.deleteTokenByUserId(dto.userId());

		return ReservationResult.from(confirmdReservation);
	}

	@Transactional
	public ReservationResult makeReservation(ReservationDto dto) {
		Reservation reservation = reservationService.makeReservation(dto);
		concertService.reserveSeat(dto.seatDto().convertToEntity());
		return ReservationResult.from(reservation);
	}
}
