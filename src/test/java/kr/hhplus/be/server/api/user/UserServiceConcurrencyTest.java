package kr.hhplus.be.server.api.user;

import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;
import kr.hhplus.be.server.api.user.domain.entity.User;
import kr.hhplus.be.server.api.user.domain.repository.UserRepository;
import kr.hhplus.be.server.api.user.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceConcurrencyTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    void 충전과_사용_동시에_실행() throws InterruptedException, ExecutionException {
        // given
        userRepository.save(User.builder().balance(BigDecimal.ZERO).build());

        BalanceHistoryDto chargeDto = new BalanceHistoryDto(1L, BalanceHistoryType.CHARGE, BigDecimal.valueOf(1000));
        BalanceHistoryDto payDto = new BalanceHistoryDto(1L, BalanceHistoryType.PAY, BigDecimal.valueOf(1000));

        int tryCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(tryCount * 2);

        AtomicInteger chargeSuccessCount = new AtomicInteger();
        AtomicInteger chargeFailCount = new AtomicInteger();
        AtomicInteger paySuccessCount = new AtomicInteger();
        AtomicInteger payFailCount = new AtomicInteger();

        CountDownLatch latch = new CountDownLatch(tryCount);  // 충전 후 사용을 위해 카운트다운 래치 사용

        // 충전 및 사용을 각각 다른 스레드에서 실행
        List<Callable<Void>> chargeTasks = new ArrayList<>();
        List<Callable<Void>> payTasks = new ArrayList<>();

        for (long i = 1; i <= tryCount; i++) {
            chargeTasks.add(() -> {
                try {
                    userService.chargeAmount(chargeDto);
                    chargeSuccessCount.incrementAndGet();
                } catch (Exception e) {
                    chargeFailCount.incrementAndGet();
                } finally {
                    latch.countDown(); // 충전이 끝나면 count 감소
                }
                return null;
            });

            payTasks.add(() -> {
                latch.await();  // 충전이 어느 정도 진행된 후 사용 실행
                try {
                    userService.payAmount(payDto);
                    paySuccessCount.incrementAndGet();
                } catch (Exception e) {
                    payFailCount.incrementAndGet();
                }
                return null;
            });
        }

        // when - 충전 먼저 실행 후 사용 실행
        List<Future<Void>> chargeFutures = executorService.invokeAll(chargeTasks);
        List<Future<Void>> payFutures = executorService.invokeAll(payTasks);

        for (Future<Void> future : chargeFutures) {
            future.get();
        }
        for (Future<Void> future : payFutures) {
            future.get();
        }

        executorService.shutdown();

        // then
        User savedUser = userRepository.findById(1L).orElseThrow();
        assertThat(savedUser.getBalance().setScale(0, RoundingMode.DOWN))
                .isEqualTo(BigDecimal.valueOf(1000L * (chargeSuccessCount.get() - paySuccessCount.get())));
    }
}