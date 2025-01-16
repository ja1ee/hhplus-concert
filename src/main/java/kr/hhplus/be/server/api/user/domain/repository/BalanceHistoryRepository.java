package kr.hhplus.be.server.api.user.domain.repository;

import kr.hhplus.be.server.api.user.domain.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {

}
