package kr.hhplus.be.server.api.reservation.application;

import kr.hhplus.be.server.api.concert.domain.service.ConcertService;
import kr.hhplus.be.server.api.concert.domain.service.RedissonSeatReservationService;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import kr.hhplus.be.server.api.reservation.domain.service.ReservationService;
import kr.hhplus.be.server.api.token.domain.service.TokenService;
import kr.hhplus.be.server.api.user.application.PaymentFacade;
import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationFacade {

	private final ReservationService reservationService;
	private final PaymentFacade paymentFacade;
	private final TokenService tokenService;
	private final RedissonSeatReservationService concertService;

	@Transactional
	public ReservationResult makeReservation(ReservationDto dto) {
		// 좌석 임시 예약
		concertService.reserveSeat(dto.seatId());
		// 예약 내역 생성
		Reservation reservation = reservationService.makeReservation(dto);

		return ReservationResult.from(reservation);
	}

	@Transactional
	public ReservationResult payAmountAndConfirmReservation(ReservationDto dto) {
		// 결제 처리
		paymentFacade.payAmount(new BalanceHistoryDto(dto.userId(), BalanceHistoryType.PAY, dto.finalPrice()));
		// 예약 확정
		Reservation reservation = reservationService.confirmReservation(dto.id());
		// 대기열 토큰 만료
		tokenService.deleteTokenByUserId(dto.userId());

		return ReservationResult.from(reservation);
	}


}
