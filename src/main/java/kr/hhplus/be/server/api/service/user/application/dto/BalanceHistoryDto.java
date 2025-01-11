package kr.hhplus.be.server.api.service.user.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import kr.hhplus.be.server.api.service.user.domain.BalanceHistory;
import lombok.Builder;

@Builder
public record BalanceHistoryDto(long userId, String type, BigDecimal amount) {

	public BalanceHistory convertToEntity() {
		return BalanceHistory.builder().
			userId(userId).type(type).amount(amount).changedAt(Instant.now()).build();
	}
}
