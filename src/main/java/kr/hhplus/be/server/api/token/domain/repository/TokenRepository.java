package kr.hhplus.be.server.api.token.domain.repository;

import java.time.LocalDateTime;

import kr.hhplus.be.server.api.token.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Token findByUserId(long userId);

	long countByIsActivatedTrue();

	List<Token> findByExpiredAtBeforeAndIsActivatedFalse(LocalDateTime now);

	@Query("SELECT t FROM Token t WHERE t.isActivated = false ORDER BY t.id ASC")
	List<Token> findTopNByIsActivatedFalseOrderByIdAsc(int limit);
}
