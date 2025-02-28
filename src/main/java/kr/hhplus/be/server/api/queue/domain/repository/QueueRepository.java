package kr.hhplus.be.server.api.queue.domain.repository;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QueueRepository {

    void addToWaitQueue(String userId);

    void addTokensToRunQueue(Set<String> userId);

    void removeTokensFromWaitQueue(Set<String> userIds);

    Set<String> pollTokens(int tokenNumberToActivate);

    void removeFromRunQueue(String userId);

    void removeExpiredTokens(double expirationTime);

    Long getRank(String userId);

    Boolean getQueueStatus(String userId);
}
