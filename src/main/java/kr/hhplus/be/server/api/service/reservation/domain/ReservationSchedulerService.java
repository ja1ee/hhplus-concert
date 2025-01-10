package kr.hhplus.be.server.api.service.reservation.domain;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationSchedulerService {

	private final ReservationRepository reservationRepository;

	@Scheduled(fixedRate = 10_000)  // 10초마다 실행
	public void checkAndReleaseExpiredReservations() {
		Instant now = Instant.now();

		List<Reservation> expiredReservations =
			reservationRepository.findByExpiredAtBeforeAndIsReservedTrue(now);

		if (!expiredReservations.isEmpty()) {
			expiredReservations.forEach(reservation -> {
				reservation.setIsReserved(false);
				reservationRepository.save(reservation);
			});
		}
	}
}