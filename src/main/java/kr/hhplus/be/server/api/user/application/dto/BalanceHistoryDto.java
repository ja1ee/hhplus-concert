package kr.hhplus.be.server.api.user.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.user.domain.entity.BalanceHistory;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;
import lombok.Builder;

@Builder
public record BalanceHistoryDto(long userId, BalanceHistoryType type, BigDecimal amount) {

	public BalanceHistory convertToEntity() {
		return BalanceHistory.builder().
			userId(userId).type(type).amount(amount).changedAt(LocalDateTime.now()).build();
	}
}
