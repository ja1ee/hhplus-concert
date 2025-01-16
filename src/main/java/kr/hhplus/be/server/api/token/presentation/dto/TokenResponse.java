package kr.hhplus.be.server.api.token.presentation.dto;

import java.time.Instant;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.token.application.dto.TokenResult;

public record TokenResponse(
	long id,
	long userId,
	Boolean isActivated,
	LocalDateTime expiredAt
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
