package kr.hhplus.be.server.api.reservation.domain;

import java.time.Instant;
import java.time.LocalDateTime;
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
		List<Reservation> expiredReservations =
			reservationRepository.findByExpiredAtBeforeAndIsReservedTrue(LocalDateTime.now());

		if (!expiredReservations.isEmpty()) {
			expiredReservations.forEach(reservation -> {
				reservation.open();
				reservationRepository.save(reservation);
			});
		}
	}
}