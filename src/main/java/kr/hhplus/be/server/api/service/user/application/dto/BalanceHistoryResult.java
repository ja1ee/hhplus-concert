package kr.hhplus.be.server.api.service.user.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import kr.hhplus.be.server.api.service.user.domain.BalanceHistory;

public record BalanceHistoryResult(
	long id,
	long userId,
	String type,
	BigDecimal amount,
	Instant changedAt
) {

	public static BalanceHistoryResult from(BalanceHistory balanceHistory) {
		return new BalanceHistoryResult(
			balanceHistory.getId(),
			balanceHistory.getUserId(),
			balanceHistory.getType(),
			balanceHistory.getAmount(),
			balanceHistory.getChangedAt()
		);
	}
}
