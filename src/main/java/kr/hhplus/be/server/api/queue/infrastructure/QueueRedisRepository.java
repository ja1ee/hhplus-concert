package kr.hhplus.be.server.api.queue.infrastructure;

import kr.hhplus.be.server.api.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class QueueRedisRepository implements QueueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private final String WAIT_QUEUE_KEY = "queue:waiting";
    private final String RUN_QUEUE_KEY = "queue:running";

    @Override
    public void addToWaitQueue(String userId) {
        redisTemplate.opsForZSet().add(WAIT_QUEUE_KEY, userId, System.currentTimeMillis());
    }

    @Override
    public Long getRank(String userId) {
        return redisTemplate.opsForZSet().rank(WAIT_QUEUE_KEY, userId);
    }

    @Override
    public void removeTokensFromWaitQueue(Set<String> userIds) {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            userIds.forEach(userId -> {
                connection.zRem(serializer.serialize(WAIT_QUEUE_KEY), serializer.serialize(userId));
            });
            return null;
        });
    }

    @Override
    public Set<String> pollTokens(int numberToActivate) {
        return redisTemplate.opsForZSet().range(WAIT_QUEUE_KEY, 0, numberToActivate);
    }

    @Override
    public void removeFromRunQueue(String userId) {
        redisTemplate.opsForZSet().remove(RUN_QUEUE_KEY, userId);
    }


    @Override
    public void removeExpiredTokens(double expirationTime) {
        redisTemplate.opsForZSet().removeRangeByScore(RUN_QUEUE_KEY, 0, expirationTime);
    }

    @Override
    public Boolean getQueueStatus(String userId) {
        return redisTemplate.opsForZSet().rank(RUN_QUEUE_KEY, userId) != null;
    }

    @Override
    public void addTokensToRunQueue(Set<String> userIds) {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            userIds.forEach(userId -> {
                connection.zAdd(serializer.serialize(RUN_QUEUE_KEY), System.currentTimeMillis(), serializer.serialize(userId));
            });
            return null;
        });
    }
}
