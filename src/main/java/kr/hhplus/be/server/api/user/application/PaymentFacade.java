package kr.hhplus.be.server.api.user.application;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.application.dto.UserResult;
import kr.hhplus.be.server.api.user.domain.service.BalanceHistoryService;
import kr.hhplus.be.server.api.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

	private final UserService userService;
	private final BalanceHistoryService balanceHistoryService;

	public void payAmount(BalanceHistoryDto dto) {
		userService.payAmount(dto);
		balanceHistoryService.addHistory(dto);
	}

	public void chargeAmount(BalanceHistoryDto dto) {
		userService.chargeAmount(dto);
		balanceHistoryService.addHistory(dto);
	}

	public UserResult getUserById(long userId) {
		return userService.getUserById(userId);
	}
}
