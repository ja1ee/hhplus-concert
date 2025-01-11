package kr.hhplus.be.server.api.service.concert.domain;

import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

	List<ConcertSeat> findAvailableSeatsByScheduleIdAndIsReservedFalse(Long scheduleId);
}
