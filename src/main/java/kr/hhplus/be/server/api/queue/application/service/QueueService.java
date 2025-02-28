package kr.hhplus.be.server.api.queue.application.service;

import kr.hhplus.be.server.api.queue.domain.repository.QueueRepository;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

	private final QueueRepository queueRepository;

	public void addToWaitQueue(long userId) {
		queueRepository.addToWaitQueue(String.valueOf(userId));
	}

	public Boolean isActivated(String userId) {
		return queueRepository.getQueueStatus(userId);
	}

	public void removeFromRunQueue(ReservationResult result) {
		queueRepository.removeFromRunQueue(String.valueOf(result.userId()));
	}

	public Long getRank(String userId) {
		return queueRepository.getRank(userId);
	}
}
