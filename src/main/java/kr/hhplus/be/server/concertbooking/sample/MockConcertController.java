package kr.hhplus.be.server.concertbooking.sample;

import kr.hhplus.be.server.concertbooking.sample.dto.ApiErrorResponse;
import kr.hhplus.be.server.concertbooking.sample.dto.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/sample/concerts")
public class MockConcertController {
	// 예매 가능 날짜 조회
	@GetMapping("/{concertId}/schedules")
	public ResponseEntity<ApiResponse<?>> getAvailableDates(
		@PathVariable("concertId") String concertId) {
		// Mock 데이터 생성
		List<Map<String, Object>> dateList = Arrays.asList(
			Map.of("id", 987654, "concert_date", "2025-01-01"),
			Map.of("id", 987655, "concert_date", "2025-01-02"),
			Map.of("id", 987656, "concert_date", "2025-01-03")
		);
		return ResponseEntity.ok(ApiResponse.builder()
			.success(true)
			.message("예약 가능한 날짜가 조회되었습니다.")
			.data(Map.of(
				"concertId", concertId,
				"dates", dateList
			))
			.build());
	}
	// 특정 날짜의 예매 가능 좌석 조회
	@GetMapping("/{concertId}/schedules/seats")
	public ResponseEntity<ApiResponse<?>> getAvailableSeats(
		@PathVariable("concertId") String concertId,
		@RequestParam String date) {
		// Mock 데이터 생성
		List<Map<String, Object>> seatList = Arrays.asList(
			Map.of("id", 987654, "seat_no", 30),
			Map.of("id", 987655, "seat_no", 31),
			Map.of("id", 987656, "seat_no", 32)
		);
		return ResponseEntity.ok(ApiResponse.builder()
			.success(true)
			.message(date + " 예매 가능 좌석 목록이 조회되었습니다.")
			.data(Map.of(
				"concertId", concertId,
				"date", date,
				"seats", seatList
			))
			.build());
	}
	// 좌석 예매 요청
	@PostMapping("/{concertId}/reservation")
	public ResponseEntity<ApiResponse<?>> reserveSeat(
		@PathVariable("concertId") String concertId,
		@RequestBody ReservationRequest request) {
		// 예매 실패
		if (request.getSeatId() == null || request.getSeatNo() <= 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ApiResponse.builder()
					.success(false)
					.message("이미 선점된 좌석입니다. 다른 좌석을 선택하세요.")
					.error(ApiErrorResponse.builder()
						.code("SEAT_ALREADY_RESERVED")
						.build())
					.build());
		}
		// 예약 만료 시간 계산 (5분 후 / Unix Timestamp로 표현)
		long reservedUntil = Instant.now().plusSeconds(5 * 60).getEpochSecond();
		return ResponseEntity.ok(ApiResponse.builder()
			.success(true)
			.message("좌석 임시 배정이 완료되었습니다.")
			.data(Map.of(
				"status", "reserved",
				"expired_at", reservedUntil
			))
			.build());
	}
}
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ReservationRequest {
	private String date;
	private Long seatId;
	private int seatNo;
}