package kr.hhplus.be.server.api.queue.application.service;

import kr.hhplus.be.server.api.queue.domain.repository.QueueRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class QueueSchedulerService {

	private final QueueRepository queueRepository;
	private static final int TOKEN_NUMBER_TO_ACTIVATE = 500;

	public QueueSchedulerService(QueueRepository queueRedisRepository) {
		this.queueRepository = queueRedisRepository;
	}

	@Scheduled(fixedRate = 10_000)
	public void activateWaitingTokens() {
		Set<String> tokens = queueRepository.pollTokens(TOKEN_NUMBER_TO_ACTIVATE);
		if (!tokens.isEmpty()) {
			queueRepository.addTokensToRunQueue(tokens);
			queueRepository.removeTokensFromWaitQueue(tokens);
		}
	}

	@Scheduled(fixedRate = 10_000)
	public void deleteExpiredTokens() {
		double expirationTime = System.currentTimeMillis() - 60_000;
		queueRepository.removeExpiredTokens(expirationTime);
	}
}
