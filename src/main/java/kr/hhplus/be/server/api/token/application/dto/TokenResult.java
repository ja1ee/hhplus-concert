package kr.hhplus.be.server.api.token.application.dto;

import java.time.LocalDateTime;

import kr.hhplus.be.server.api.token.domain.entity.Token;

public record TokenResult(
	long id,
	long userId,
	Boolean isActivated,
	LocalDateTime expiredAt
) {

	public static TokenResult from(Token token) {
		return new TokenResult(
			token.getId(),
			token.getUserId(),
			token.getIsActivated(),
			token.getExpiredAt()
		);
	}
}
