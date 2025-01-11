package kr.hhplus.be.server.api.service.token.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserIdRequest(
	@Schema(description = "사용자의 고유 ID", example = "12345")
	long userId
) {

}