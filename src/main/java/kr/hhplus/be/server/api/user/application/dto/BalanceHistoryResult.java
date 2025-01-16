package kr.hhplus.be.server.api.user.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.user.domain.entity.BalanceHistory;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;

public record BalanceHistoryResult(
	long id,
	long userId,
	BalanceHistoryType type,
	BigDecimal amount,
	LocalDateTime changedAt
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
