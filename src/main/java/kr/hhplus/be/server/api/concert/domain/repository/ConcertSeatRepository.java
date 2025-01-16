package kr.hhplus.be.server.api.concert.domain.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<ConcertSeat> findById(long id);

	List<ConcertSeat> findAvailableSeatsByScheduleIdAndIsReservedFalse(long scheduleId);
}
