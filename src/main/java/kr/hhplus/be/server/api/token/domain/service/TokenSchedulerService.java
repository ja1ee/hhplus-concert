package kr.hhplus.be.server.api.token.domain.service;

import java.time.LocalDateTime;

import kr.hhplus.be.server.api.token.domain.entity.Token;
import kr.hhplus.be.server.api.token.domain.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenSchedulerService {

	private final TokenRepository tokenRepository;

	private static final int MAX_ACTIVE_TOKENS = 50;

	@Scheduled(fixedRate = 10_000)  // 10초마다 실행
	public void deleteExpiredTokens() {
		List<Token> expiredTokens = tokenRepository.findByExpiredAtBeforeAndIsActivatedFalse(
			LocalDateTime.now());

		if (!expiredTokens.isEmpty()) {
			tokenRepository.deleteAll(expiredTokens);
		}

	}

	@Scheduled(fixedRate = 10_000)
	public void activateWaitingTokens() {
		long activeTokenCount = tokenRepository.countByIsActivatedTrue();

		int tokensToActivate = MAX_ACTIVE_TOKENS - (int) activeTokenCount;

		if (tokensToActivate <= 0) {
			return;
		}

		List<Token> waitingTokens = tokenRepository.findTopNByIsActivatedFalseOrderByIdAsc(
			tokensToActivate);

		if (waitingTokens.isEmpty()) {
			return;
		}

		LocalDateTime tenMinutesLater = LocalDateTime.now().plusMinutes(10);
		waitingTokens.forEach((token) -> token.activate(tenMinutesLater));
		tokenRepository.saveAll(waitingTokens);
	}
}