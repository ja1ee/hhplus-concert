package kr.hhplus.be.server;

import com.redis.testcontainers.RedisContainer;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
class TestcontainersConfig {

	public static final MySQLContainer<?> MYSQL_CONTAINER;
	public static final RedisContainer REDIS_CONTAINER;
	public static final ConfluentKafkaContainer KAFKA_CONTAINER = new ConfluentKafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka:latest")
					.asCompatibleSubstituteFor("apache/kafka")
	);

	static {
		MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
				.withDatabaseName("hhplus")
				.withUsername("test")
				.withPassword("test");
		MYSQL_CONTAINER.start();

		System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
		System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
		System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());

		REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest"))
				.withExposedPorts(6379);
		REDIS_CONTAINER.start();
		// Spring Redis 환경 속성 설정
		System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
		System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());

		KAFKA_CONTAINER.start();
		System.setProperty("spring.kafka.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
	}

}
