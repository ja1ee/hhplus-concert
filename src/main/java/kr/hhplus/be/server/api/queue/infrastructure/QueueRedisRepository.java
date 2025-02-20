package kr.hhplus.be.server.api.queue.infrastructure;

import kr.hhplus.be.server.api.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class QueueRedisRepository implements QueueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private final String WAIT_QUEUE_KEY = "queue:waiting";
    private final String RUN_QUEUE_KEY = "queue:running";

    @Override
    public Long addToWaitQueue(String userId) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.zAdd(WAIT_QUEUE_KEY.getBytes(), System.currentTimeMillis(), String.valueOf(userId).getBytes());
            return null;
        });
        return getRank(userId);
    }

    @Override
    public void addToRunQueue(String userId) {
        redisTemplate.opsForZSet().add(RUN_QUEUE_KEY, String.valueOf(userId), System.currentTimeMillis());
    }

    @Override
    public Long getRank(String userId) {return redisTemplate.opsForZSet().rank(WAIT_QUEUE_KEY, userId);
    }

    @Override
    public void activateTokens(Set<String> userIds) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            userIds.forEach(userId -> {
                connection.zAdd(keySerializer.serialize(RUN_QUEUE_KEY), System.currentTimeMillis(), valueSerializer.serialize(userId));
                connection.zRem(keySerializer.serialize(WAIT_QUEUE_KEY), valueSerializer.serialize(userId));
            });
            return null;
        });
    }


    @Override
    public Set<String> getTokensFromFront(int numberToActivate) {
        return redisTemplate.opsForZSet().range(WAIT_QUEUE_KEY, 0, numberToActivate);
    }

    @Override
    public void removeFromRunQueue(String userId) {
        redisTemplate.opsForZSet().remove(RUN_QUEUE_KEY, userId);
    }

    @Override
    public void removeFromWaitQueue(String userId) {
        redisTemplate.opsForZSet().remove(WAIT_QUEUE_KEY, userId);
    }

    @Override
    public void removeExpiredTokens(double expirationTime) {
        redisTemplate.opsForZSet().removeRangeByScore(RUN_QUEUE_KEY, 0, expirationTime);
    }

    @Override
    public List<Object> checkQueueStatus(String userId) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        return redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            List<Object> results = new ArrayList<>();
            results.add(connection.sIsMember(keySerializer.serialize(RUN_QUEUE_KEY), valueSerializer.serialize(userId)));
            results.add(connection.zRank(keySerializer.serialize(WAIT_QUEUE_KEY), valueSerializer.serialize(userId.getBytes())));
            return results;
        });
    }
}
