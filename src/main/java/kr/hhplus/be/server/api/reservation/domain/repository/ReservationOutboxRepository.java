package kr.hhplus.be.server.api.reservation.domain.repository;

import kr.hhplus.be.server.api.reservation.domain.entity.ReservationOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationOutboxRepository extends JpaRepository<ReservationOutbox, Long> {
    List<ReservationOutbox> findTop10ByPublishedFalseOrderByCreatedAtAsc();
}
