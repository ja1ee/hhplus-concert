package kr.hhplus.be.server.api.queue.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.api.queue.application.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "토큰 발급 및 대기열 관리 API", description = "대기열 관리 토큰을 발급합니다.")
@RequestMapping("/tokens")
public class QueueController {

	private final QueueService queueService;

	@Operation(
		summary = "Token 발급",
		description = "사용자 ID를 기반으로 Token을 발급합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "토큰이 성공적으로 발급됨",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class))
		)
	})
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<Long>> createToken(
		@Parameter(name = "userId", description = "유저의 고유 ID", example = "12345")
		@PathVariable("userId") long userId) {
		Long rank = queueService.addToWaitQueue(userId);
		return ResponseEntity.ok(ApiResponse.of("대기열에 등록되었습니다.", rank));
	}
}