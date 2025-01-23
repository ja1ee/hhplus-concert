package kr.hhplus.be.server.api.concert.domain.service;

import kr.hhplus.be.server.api.common.exception.CustomException;
import kr.hhplus.be.server.api.common.exception.ErrorCode;
import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedissonSeatReservationService implements SeatReservationService {

    private final ConcertSeatRepository concertSeatRepository;
    private final RedissonClient redissonClient;

    @Override
    public void reserveSeat(long seatId) {
        RLock lock = redissonClient.getLock("concertSeat:" + seatId);

        try {
            boolean isLocked = lock.tryLock(1, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new CustomException(ErrorCode.ALREADY_RESERVED_SEAT);
            }

            ConcertSeat seat = concertSeatRepository.findById(seatId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEAT));

            if (seat.getIsReserved()) {
                throw new CustomException(ErrorCode.ALREADY_RESERVED_SEAT);
            }

            seat.reserve();
            concertSeatRepository.save(seat);
        } catch (InterruptedException e) {
            throw new CustomException(ErrorCode.INTERRUPT_OCCURRED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}