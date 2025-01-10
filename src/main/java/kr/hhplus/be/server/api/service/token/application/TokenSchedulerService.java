package kr.hhplus.be.server.api.service.token.application;

import java.time.temporal.ChronoUnit;
import kr.hhplus.be.server.api.service.token.domain.Token;
import kr.hhplus.be.server.api.service.token.domain.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
			Instant.now());

		if (!expiredTokens.isEmpty()) {
			tokenRepository.deleteAll(expiredTokens);
			System.out.println("삭제됨");
		}
		System.out.println("삭제안됨");

	}

	@Scheduled(fixedRate = 10_000)
	public void activateWaitingTokens() {
		long activeTokenCount = tokenRepository.countByIsActivatedTrue();

		int tokensToActivate = MAX_ACTIVE_TOKENS - (int) activeTokenCount;

		if (tokensToActivate <= 0) {
			System.out.println("Cannot activate new tokens. Active token limit reached.");
			return;
		}

		List<Token> waitingTokens = tokenRepository.findTopNByIsActivatedFalseOrderByIdAsc(
			tokensToActivate);

		if (waitingTokens.isEmpty()) {
			return;
		}

		Instant tenMinutesLater = Instant.now().plus(10, ChronoUnit.MINUTES);
		waitingTokens.forEach((token) -> token.activate(tenMinutesLater));
		tokenRepository.saveAll(waitingTokens);
		System.out.println("Activated " + waitingTokens.size() + " waiting tokens.");
	}
}