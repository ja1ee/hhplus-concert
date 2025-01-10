package kr.hhplus.be.server.api.service.token.presentation.dto;

import java.time.Instant;
import kr.hhplus.be.server.api.service.token.application.dto.TokenResult;

public record TokenResponse(
	long id,
	long userId,
	Boolean isActivated,
	Instant expiredAt
) {

	public static TokenResponse from(TokenResult result) {
		return new TokenResponse(
			result.id(),
			result.userId(),
			result.isActivated(),
			result.expiredAt()
		);
	}
}
