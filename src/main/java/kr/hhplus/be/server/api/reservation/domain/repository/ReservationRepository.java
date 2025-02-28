package kr.hhplus.be.server.api.reservation.domain.repository;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationStatus;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	Reservation findById(long id);

	Reservation findBySeatIdAndStatus(long seatId, ReservationStatus status); // 좌석 만료 확인용

	List<Reservation> findByExpiredAtBeforeAndStatus(LocalDateTime expiredAt, ReservationStatus status); // 스케줄러용

}
