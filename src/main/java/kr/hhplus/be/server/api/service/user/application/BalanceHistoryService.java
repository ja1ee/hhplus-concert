package kr.hhplus.be.server.api.service.user.application;


import java.math.BigDecimal;
import java.time.Instant;
import kr.hhplus.be.server.api.service.user.application.dto.BalanceHistoryResult;
import kr.hhplus.be.server.api.service.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.service.user.domain.BalanceHistory;
import kr.hhplus.be.server.api.service.user.domain.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceHistoryService {

	private final BalanceHistoryRepository balanceHistoryRepository;

	@Transactional
	public BalanceHistoryResult addChargeHistory(BalanceHistoryDto dto) {
		return BalanceHistoryResult.from(balanceHistoryRepository.save(dto.convertToEntity()));
	}

	@Transactional
	public void addWithdrawHistory(long userId, BigDecimal totalAmount) {
		BalanceHistory balanceHistory = BalanceHistory.builder().userId(userId).type("withdraw")
			.amount(totalAmount).changedAt(Instant.now()).build();
		balanceHistoryRepository.save(balanceHistory);
	}

}
