package kr.hhplus.be.server.concertbooking.sample;

import java.util.Map;
import kr.hhplus.be.server.concertbooking.sample.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.UUID;
@RestController
@RequestMapping("/sample")
public class MockTokenController {
	//Mock Token 발급
	@PostMapping("/tokens")
	public ResponseEntity<ApiResponse<?>> generateMockToken(@RequestBody String userId) {
		String generatedToken = UUID.randomUUID().toString();
		// 예약 만료 시간 계산 (5분 후 / Unix Timestamp로 표현)
		long reservedUntil = Instant.now().plusSeconds(5 * 60).getEpochSecond();
		// Mock 응답 데이터 생성(대기열 등록)
		return ResponseEntity.ok(ApiResponse.builder()
			.success(true)
			.message("예약 가능한 날짜가 조회되었습니다.")
			.data(Map.of(
				"id", generatedToken,
				"status", "waiting",
				"user_id", userId,
				"created_at", Instant.now(),
				"expired_at", reservedUntil
			))
			.build());
	}
}