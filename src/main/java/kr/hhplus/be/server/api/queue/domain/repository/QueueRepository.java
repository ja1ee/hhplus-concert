package kr.hhplus.be.server.api.queue.domain.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QueueRepository {

    Long addToWaitQueue(String userId);

    void addToRunQueue(String userId);

    void activateTokens(Set<String> userId);

    Set<String> getTokensFromFront(int tokenNumberToActivate);

    void removeFromRunQueue(String userId);

    void removeFromWaitQueue(String userId);

    void removeExpiredTokens(double expirationTime);

    Long getRank(String userId);

    List<Object> checkQueueStatus(String userId);
}
