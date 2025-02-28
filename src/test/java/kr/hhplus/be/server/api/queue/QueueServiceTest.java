package kr.hhplus.be.server.api.queue;

import kr.hhplus.be.server.api.queue.application.service.QueueService;
import kr.hhplus.be.server.api.queue.domain.repository.QueueRepository;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    @BeforeEach
    void setUp() {
        // Mockito initialization happens automatically with @InjectMocks and @Mock annotations.
    }

    @Test
    void 대기열_등록_성공() {
        // Given
        long userId = 12345L;

        // When
        queueService.addToWaitQueue(userId);

        // Then
        verify(queueRepository, times(1)).addToWaitQueue(String.valueOf(userId));
    }

    @Test
    void 토큰상태_조회_성공() {
        // Given
        String userId = "12345";
        boolean expectedStatus = true;

        when(queueRepository.getQueueStatus(userId)).thenReturn(expectedStatus);

        // When
        Boolean isActivated = queueService.isActivated(userId);

        // Then
        assertTrue(isActivated);
        verify(queueRepository, times(1)).getQueueStatus(userId);
    }

    @Test
    void 예약성공시_활성화토큰에서_삭제() {
        // Given
        ReservationResult result = ReservationResult.builder().userId(1L).build();  // Assuming constructor with userId

        // When
        queueService.removeFromRunQueue(result);

        // Then
        verify(queueRepository, times(1)).removeFromRunQueue(String.valueOf(result.userId()));
    }

    @Test
    void 대기순번_조회() {
        // Given
        String userId = "12345";
        Long expectedRank = 1L;

        when(queueRepository.getRank(userId)).thenReturn(expectedRank);

        // When
        Long rank = queueService.getRank(userId);

        // Then
        assertEquals(expectedRank, rank);
        verify(queueRepository, times(1)).getRank(userId);
    }
}
