package kr.hhplus.be.server.api.token.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.api.token.application.dto.TokenResult;
import kr.hhplus.be.server.api.token.presentation.dto.TokenResponse;
import kr.hhplus.be.server.api.token.domain.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "토큰 발급 및 대기열 관리 API", description = "대기열 관리 토큰을 발급합니다.")
@RequestMapping("/tokens")
public class TokenController {

	private final TokenService tokenService;

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
	public ResponseEntity<ApiResponse<TokenResponse>> createToken(
		@Parameter(name = "userId", description = "유저의 고유 ID", example = "12345")
		@PathVariable("userId") long userId) {
		TokenResult result = tokenService.createToken(userId);
		TokenResponse response = TokenResponse.from(result);
		return ResponseEntity.ok(ApiResponse.of("토큰이 발행되었습니다.", response));

	}
}