package kr.hhplus.be.server.api.user.application.service;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.application.dto.UserResult;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final UserService userService;
	private final BalanceHistoryService balanceHistoryService;


	public void payAmount(ReservationResult result) {
		BalanceHistoryDto balanceHistoryDto = new BalanceHistoryDto(result.userId(), BalanceHistoryType.PAY, result.finalPrice());
		userService.payAmount(balanceHistoryDto);
		balanceHistoryService.addHistory(balanceHistoryDto);
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
