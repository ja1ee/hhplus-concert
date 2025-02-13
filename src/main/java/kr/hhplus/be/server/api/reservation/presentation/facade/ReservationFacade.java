package kr.hhplus.be.server.api.reservation.presentation.facade;

import kr.hhplus.be.server.api.concert.application.service.ConcertService;
import kr.hhplus.be.server.api.queue.application.service.QueueService;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.application.service.ReservationService;
import kr.hhplus.be.server.api.user.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationFacade {

	private final ReservationService reservationService;
	private final PaymentService paymentService;
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
		// 예약 확정
		ReservationResult result = reservationService.confirmReservation(dto.id());
		// 결제 처리
		paymentService.payAmount(result);
		// 대기열 토큰 만료
		queueService.removeFromRunQueue(result);
		// 데이터 플랫폼 전달

		return result;
	}

}
