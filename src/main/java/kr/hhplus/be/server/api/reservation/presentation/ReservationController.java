package kr.hhplus.be.server.api.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.api.common.response.ApiResponse;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.presentation.dto.ReservationRequest;
import kr.hhplus.be.server.api.reservation.application.ReservationFacade;
import kr.hhplus.be.server.api.reservation.presentation.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "좌석 예약 요청 및 결제 API", description = "좌석을 예약하고 결제합니다.")
@RequestMapping("/reservation")
public class ReservationController {

	private final ReservationFacade reservationFacade;

	@Operation(
		summary = "좌석 예약",
		description = "날짜와 좌석 정보를 선택해 좌석을 임시 예약합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "예약 완료",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class))
		),
	})
	@PostMapping()
	public ResponseEntity<ApiResponse<ReservationResponse>> reserveSeat(
		@Valid @RequestBody ReservationRequest req) {
		ReservationResult reservation = reservationFacade.makeReservation(req.toDto());
		ReservationResponse response = ReservationResponse.from(reservation);
		return ResponseEntity.ok(ApiResponse.of("좌석이 예약되었습니다.", response));
	}

	@Operation(
		summary = "예약건 결제",
		description = "결제를 처리하고 결제 내역을 생성합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "결제 완료",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class))
		)
	})
	@PostMapping("/payment")
	public ResponseEntity<ApiResponse<ReservationResponse>> payForSeat(
		@Valid @RequestBody ReservationRequest req) {
		ReservationResult reservation = reservationFacade.payAmountAndConfirmReservation(
			req.toDto());
		ReservationResponse response = ReservationResponse.from(reservation);
		return ResponseEntity.ok(ApiResponse.of("좌석이 결제되었습니다.", response));
	}

}
