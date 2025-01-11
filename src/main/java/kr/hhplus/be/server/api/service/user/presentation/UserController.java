package kr.hhplus.be.server.api.service.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.api.service.common.ApiResult;
import kr.hhplus.be.server.api.service.user.application.UserService;
import kr.hhplus.be.server.api.service.user.application.dto.BalanceHistoryResult;
import kr.hhplus.be.server.api.service.user.application.dto.UserResult;
import kr.hhplus.be.server.api.service.user.presentation.dto.BalanceHistoryRequest;
import kr.hhplus.be.server.api.service.user.application.BalanceHistoryService;
import kr.hhplus.be.server.api.service.user.presentation.dto.BalanceHistoryResponse;
import kr.hhplus.be.server.api.service.user.presentation.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "잔액 충전 및 조회 API", description = "예매에 사용할 잔액을 조회하고 충전합니다.")
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final BalanceHistoryService balanceHistoryService;

	@Operation(
		summary = "잔액 충전",
		description = "유저 ID를 기반으로 결제에 사용될 금액을 충전합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "충전 완료",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
		),
	})
	@PostMapping("/charge")
	public ResponseEntity<ApiResult<BalanceHistoryResponse>> setAmount(
		@RequestBody BalanceHistoryRequest req) {
		BalanceHistoryResult balanceHistory = balanceHistoryService.addChargeHistory(req.toDto());
		BalanceHistoryResponse response = BalanceHistoryResponse.from(balanceHistory);
		return ResponseEntity.ok(ApiResult.of("잔액이 충전되었습니다.", response));
	}

	@Operation(
		summary = "잔액 조회",
		description = "유저 ID를 기반으로 결제에 사용될 금액을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "조회 완료",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
		),
	})
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResult<UserResponse>> getBalance(
		@Parameter(name = "userId", description = "조회할 사용자의 ID", example = "1")
		@PathVariable("userId") long userId) {
		UserResult user = userService.getUserById(userId);
		UserResponse response = UserResponse.from(user);
		return ResponseEntity.ok(ApiResult.of("잔액이 조회되었습니다.", response));
	}

}