package kr.hhplus.be.server.api.reservation.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import static org.awaitility.Awaitility.await;


@SpringBootTest
public class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final AtomicInteger counter = new AtomicInteger(0);


    @KafkaListener(topics = "reservation-test-topic", groupId = "test-group")
    public void listen(String message) {
        counter.incrementAndGet();
        assertThat(message).isEqualTo("test-message");
    }

    @DisplayName("Kafka 이벤트 메시지 발행 및 응답 테스트")
    @Test
    public void handle_event_test() {
        kafkaTemplate.send("reservation-test-topic", "test-message");
        await()
            .pollInterval(Duration.ofMillis(50))
            .atMost(Duration.ofSeconds(2))
            .untilAsserted(() -> {
                assertThat(counter.get()).isEqualTo(1L);
            });
    }

}
