package kr.hhplus.be.server.api.reservation.application;

import java.util.List;

import kr.hhplus.be.server.api.concert.domain.service.ConcertService;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationRepository;
import kr.hhplus.be.server.api.reservation.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationSchedulerFacade {

	private final ReservationService reservationService;
	private final ConcertService concertService;

	@Scheduled(fixedRate = 10_000)  // 10초마다 실행
	@Transactional
	public void checkAndReleaseExpiredReservations() {
		List<Reservation> expiredReservations = reservationService.getExpiredReservations();

		if (!expiredReservations.isEmpty()) {
			expiredReservations.forEach(reservation -> {
				concertService.expireSeat(reservation.getSeatId());
				reservationService.expireReservation(reservation);
			});
		}
	}
}