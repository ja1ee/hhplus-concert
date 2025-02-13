package kr.hhplus.be.server.api.reservation.presentation.facade;

import java.util.List;

import kr.hhplus.be.server.api.concert.application.service.ConcertService;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.application.service.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationSchedulerFacade {

	private final ReservationService reservationService;
	private final ConcertService concertService;

	@Scheduled(fixedRate = 10_000)  // 10초마다 실행
	@Transactional
	public void checkAndReleaseExpiredReservations() {
		List<ReservationResult> expiredReservations = reservationService.getExpiredReservations();

		if (!expiredReservations.isEmpty()) {
			expiredReservations.forEach(reservation -> {
				concertService.expireSeat(reservation.seatId());
				reservationService.expireReservation(reservation);
			});
		}
	}
}