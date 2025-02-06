package kr.hhplus.be.server.api.reservation;

import kr.hhplus.be.server.api.concert.domain.entity.ConcertSeat;
import kr.hhplus.be.server.api.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.api.reservation.presentation.facade.ReservationFacade;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationConcurrencyTest {
    @Autowired
    private ReservationFacade reservationFacade;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ConcertSeatRepository concertSeatRepository;

    LocalDateTime mockTime = LocalDateTime.of(2025, 1, 15, 10, 30);

    @Test
    void 열명중_한명만_성공() throws InterruptedException {
        // given
        concertSeatRepository.save(ConcertSeat.builder().scheduleId(1L).seatNo(10).price(BigDecimal.valueOf(55_000)).isReserved(false).build());
        ReservationDto dto = new ReservationDto(1L, 1L, 1L, 10, mockTime.toLocalDate(), BigDecimal.valueOf(55000));

        int tryCount = 10;
        List<Callable<Void>> tasks = new ArrayList<>();
        for (long i = 1; i <= tryCount; i++) {
            tasks.add(() -> {
                reservationFacade.makeReservation(dto);
                return null;
            });
        }
        ExecutorService executorService = Executors.newFixedThreadPool(tryCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        List<Future<Void>> futures = executorService.invokeAll(tasks);
        for (Future<Void> future : futures) {
            try {
                future.get();
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            }
        }
        executorService.shutdown();

        // then
        List<Reservation> list = reservationRepository.findAll();
        assertThat(list).hasSize(1);
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(9);
    }
}
