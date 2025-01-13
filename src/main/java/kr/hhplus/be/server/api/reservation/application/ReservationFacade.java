package kr.hhplus.be.server.api.reservation.application;

import kr.hhplus.be.server.api.concert.application.ConcertUsecase;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.domain.Reservation;
import kr.hhplus.be.server.api.reservation.domain.ReservationService;
import kr.hhplus.be.server.api.token.application.TokenUsecase;
import kr.hhplus.be.server.api.user.application.UserUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationFacade {

	private final ReservationService reservationService;
	private final UserUsecase userUsecase;
	private final TokenUsecase tokenUsecase;
	private final ConcertUsecase concertService;

	@Transactional
	public ReservationResult payAmountAndConfirmReservation(ReservationDto dto) {
		// 결제 처리
		userUsecase.payAmount(dto);
		// 예약확정
		Reservation reservation = reservationService.confirmReservation(dto.seatDto());
		// 대기열 토큰 만료
		tokenUsecase.deleteTokenByUserId(dto.userId());

		return ReservationResult.from(reservation);
	}

	@Transactional
	public ReservationResult makeReservation(ReservationDto dto) {
		Reservation reservation = reservationService.makeReservation(dto);
		concertService.reserveSeat(dto.seatDto().convertToEntity());
		return ReservationResult.from(reservation);
	}
}
