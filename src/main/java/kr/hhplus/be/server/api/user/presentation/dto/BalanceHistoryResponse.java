package kr.hhplus.be.server.api.user.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryResult;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;

public record BalanceHistoryResponse(
	long id,
	long userId,
	BalanceHistoryType type,
	BigDecimal amount,
	LocalDateTime changedAt
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
