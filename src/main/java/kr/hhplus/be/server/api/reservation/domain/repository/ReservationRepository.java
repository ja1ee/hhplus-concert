package kr.hhplus.be.server.api.reservation.domain.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	Reservation findById(long id);

	Reservation findBySeatIdAndIsReservedTrue(long seatId); // 좌석 만료 확인용

	List<Reservation> findByExpiredAtBeforeAndIsReservedTrue(LocalDateTime now); // 스케줄러용

}
