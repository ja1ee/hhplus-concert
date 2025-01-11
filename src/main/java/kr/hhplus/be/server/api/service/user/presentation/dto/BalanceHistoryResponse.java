package kr.hhplus.be.server.api.service.user.presentation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import kr.hhplus.be.server.api.service.user.application.dto.BalanceHistoryResult;

public record BalanceHistoryResponse(
	long id,
	long userId,
	String type,
	BigDecimal amount,
	Instant changedAt
) {

	public static BalanceHistoryResponse from(BalanceHistoryResult result) {
		return new BalanceHistoryResponse(
			result.id(),
			result.userId(),
			result.type(),
			result.amount(),
			result.changedAt()
		);
	}
}
