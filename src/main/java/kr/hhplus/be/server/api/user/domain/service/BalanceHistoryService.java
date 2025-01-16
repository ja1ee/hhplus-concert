package kr.hhplus.be.server.api.user.domain.service;

import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryResult;
import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.domain.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceHistoryService {

	private final BalanceHistoryRepository balanceHistoryRepository;

	@Transactional
	public BalanceHistoryResult addHistory(BalanceHistoryDto dto) {
		return BalanceHistoryResult.from(balanceHistoryRepository.save(dto.convertToEntity()));
	}

}
