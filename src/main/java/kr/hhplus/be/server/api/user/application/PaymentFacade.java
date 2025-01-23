package kr.hhplus.be.server.api.user.application;

import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.application.dto.UserResult;
import kr.hhplus.be.server.api.user.domain.service.BalanceHistoryService;
import kr.hhplus.be.server.api.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

	private final UserService userService;
	private final BalanceHistoryService balanceHistoryService;

	@Transactional
	public void payAmount(BalanceHistoryDto dto) {
		userService.payAmount(dto);
		balanceHistoryService.addHistory(dto);
	}

	@Transactional
	public void chargeAmount(BalanceHistoryDto dto) {
		userService.chargeAmount(dto);
		balanceHistoryService.addHistory(dto);
	}

	@Transactional(readOnly = true)
	public UserResult getUserById(long userId) {
		return userService.getUserById(userId);
	}
}
