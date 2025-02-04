package kr.hhplus.be.server.api.concert.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.api.concert.domain.service.ConcertService;
import kr.hhplus.be.server.api.concert.application.dto.ConcertScheduleResult;
import kr.hhplus.be.server.api.concert.application.dto.ConcertSeatResult;
import kr.hhplus.be.server.api.concert.presentation.dto.ConcertScheduleResponse;
import kr.hhplus.be.server.api.concert.presentation.dto.ConcertSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "콘서트 API", description = "예약 가능 날짜와 좌석을 조회합니다.")
@RequestMapping("/concerts")
public class ConcertController {

	private final ConcertService concertService;

	@Operation(
		summary = "예약 가능 날짜 조회",
		description = "콘서트 ID를 기반으로 예약 가능 날짜를 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "예약 가능 날짜 조회 완료",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class))
		)
	})
	@GetMapping("/{concertId}/schedules")
	public ResponseEntity<ApiResponse<ConcertScheduleResponse>> getAvailableDates(
		@Parameter(name = "concertId", description = "조회할 콘서트의 ID", example = "12345")
		@PathVariable("concertId") long concertId) {
		ConcertScheduleResult concertSchedules = concertService.getAvailableSchedules(concertId);
		ConcertScheduleResponse response = ConcertScheduleResponse.from(concertSchedules);
		return ResponseEntity.ok(ApiResponse.of("예약 가능한 날짜가 조회되었습니다.", response));
	}

	@Operation(
		summary = "예약 가능 좌석 조회",
		description = "콘서트 스케줄 ID를 기반으로 예약 가능 좌석을 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "예약 가능 좌석 조회 완료",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class))
		)
	})
	@GetMapping("/schedules/{scheduleId}/seats")
	public ResponseEntity<ApiResponse<ConcertSeatResponse>> getAvailableSeats(
		@Parameter(name = "scheduleId", description = "조회할 콘서트 스케쥴의 ID", example = "12345")
		@PathVariable("scheduleId") long scheduleId) {
		ConcertSeatResult concertSeats = concertService.getAvailableSeats(scheduleId);
		ConcertSeatResponse response = ConcertSeatResponse.from(concertSeats);
		return ResponseEntity.ok(ApiResponse.of("예약 가능한 좌석이 조회되었습니다.", response));
	}
}