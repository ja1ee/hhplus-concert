package kr.hhplus.be.server.api.service.token.domain;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByUserId(long userId);

	long countByIsActivatedTrue();

	List<Token> findByExpiredAtBeforeAndIsActivatedFalse(Instant now);

	@Query("SELECT t FROM Token t WHERE t.isActivated = false ORDER BY t.id ASC")
	List<Token> findTopNByIsActivatedFalseOrderByIdAsc(int limit);
}
