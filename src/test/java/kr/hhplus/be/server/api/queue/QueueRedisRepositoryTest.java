package kr.hhplus.be.server.api.queue;

import kr.hhplus.be.server.api.queue.infrastructure.QueueRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class QueueRedisRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private QueueRedisRepository queueRepository;

    @Captor
    private ArgumentCaptor<Double> scoreCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    void addToWaitQueue() {
        // given
        String userId = "12345";

        when(redisTemplate.executePipelined(any(RedisCallback.class))).thenReturn(null);
        when(zSetOperations.rank("queue:waiting", userId)).thenReturn(2L);

        // when
        Long rank = queueRepository.addToWaitQueue(userId);

        // then
        assertEquals(2L, rank);
    }

    @Test
    void addToRunQueue() {
        // given
        String userId = "12345";
        long currentTime = System.currentTimeMillis();

        //when
        queueRepository.addToRunQueue(userId);
        // ArgumentCaptor로 score 값을 캡처
        verify(zSetOperations).add(eq("queue:running"), eq(String.valueOf(userId)), scoreCaptor.capture());
        Double capturedScore = scoreCaptor.getValue();

        // then
        // `currentTime`과 `capturedScore`가 매우 가까운지 확인 (허용 오차 ±100ms)
        assertTrue(Math.abs(capturedScore - currentTime) < 100,
                "The captured score is not close enough to the current time");
    }

    @Test
    void getRank() {
        // given
        String userId = "12345";
        when(zSetOperations.rank("queue:waiting", String.valueOf(userId))).thenReturn(1L);

        // when
        Long rank = queueRepository.getRank(userId);

        // then
        assertEquals(1L, rank);
    }

    @Test
    void getTokensFromFront() {
        // given
        int tokenNumberToActivate = 5;
        Set<String> tokens = Set.of("user1", "user2", "user3");
        when(zSetOperations.range("queue:waiting", 0, tokenNumberToActivate)).thenReturn(tokens);

        // when
        Set<String> result = queueRepository.getTokensFromFront(tokenNumberToActivate);

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void removeFromWaitQueue() {
        // given
        String userId = "12345";

        // when
        queueRepository.removeFromWaitQueue(userId);

        // then
        verify(zSetOperations).remove("queue:waiting", userId);
    }

    @Test
    void removeExpiredTokens() {
        // given
        long expirationTime = System.currentTimeMillis() - 60_000L;

        // when
        queueRepository.removeExpiredTokens(expirationTime);

        // then
        verify(zSetOperations).removeRangeByScore("queue:running", 0, expirationTime);
    }

    @Test
    void checkQueueStatus() {
        // given
        String userId = "12345";
        when(redisTemplate.executePipelined(any(RedisCallback.class))).thenReturn(List.of(true, 2L));

        // when
        List<Object> result = queueRepository.checkQueueStatus(userId);

        // then
        assertNotNull(result);
        assertTrue((Boolean) result.get(0));  // isMember가 true인 경우
        assertEquals(2L, result.get(1));     // rank가 2인 경우
    }
}
