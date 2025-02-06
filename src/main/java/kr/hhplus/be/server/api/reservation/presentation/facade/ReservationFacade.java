package kr.hhplus.be.server.api.reservation.presentation.facade;

import kr.hhplus.be.server.api.concert.application.service.ConcertService;
import kr.hhplus.be.server.api.queue.application.service.QueueService;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.application.service.ReservationService;
import kr.hhplus.be.server.api.user.presentation.facade.PaymentFacade;
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
	private final ConcertService concertService;
	private final QueueService queueService;

	@Transactional
	public ReservationResult makeReservation(ReservationDto dto) {
		// 좌석 임시 예약
		concertService.reserveSeat(dto.seatId());
		// 예약 내역 생성
        return reservationService.makeReservation(dto);
	}

	@Transactional
	public ReservationResult payAmountAndConfirmReservation(ReservationDto dto) {
		// 결제 처리
		paymentFacade.payAmount(new BalanceHistoryDto(dto.userId(), BalanceHistoryType.PAY, dto.finalPrice()));
		// 대기열 토큰 만료
		queueService.removeFromRunQueue(dto.userId());
		// 예약 확정
		return reservationService.confirmReservation(dto.id());
	}


}
