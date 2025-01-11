package kr.hhplus.be.server.api.service.token.application.dto;

import java.time.Instant;
import kr.hhplus.be.server.api.service.token.domain.Token;

public record TokenResult(
	long id,
	long userId,
	Boolean isActivated,
	Instant expiredAt
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
