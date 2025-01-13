package kr.hhplus.be.server.api.reservation.domain;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	Reservation findBySeatNo(long seatNo);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT r FROM Reservation r WHERE r.seatNo = :seatNo AND r.isReserved = true")
	Reservation findBySeatNoAndIsReservedTrue(@Param("seatNo") long seatNo); // 예약용

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Reservation> findByExpiredAtBeforeAndIsReservedTrue(LocalDateTime now); // 스케줄러용

}
