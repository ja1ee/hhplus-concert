package kr.hhplus.be.server.api.queue.application.service;

import kr.hhplus.be.server.api.queue.application.dto.TokenStatusResult;
import kr.hhplus.be.server.api.queue.application.event.QueueUpdatedEvent;
import kr.hhplus.be.server.api.queue.domain.repository.QueueRepository;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

	private final QueueRepository queueRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	public Long addToWaitQueue(long userId) {
		return queueRepository.addToWaitQueue(String.valueOf(userId));
	}

	public TokenStatusResult checkQueueStatus(String userId) {
		return TokenStatusResult.from(queueRepository.checkQueueStatus(userId));
	}

	public void removeFromRunQueue(ReservationResult result) {
		queueRepository.removeFromRunQueue(String.valueOf(result.userId()));
		applicationEventPublisher.publishEvent(new QueueUpdatedEvent(result));
	}
}
