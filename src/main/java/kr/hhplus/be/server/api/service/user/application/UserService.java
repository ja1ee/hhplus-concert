package kr.hhplus.be.server.api.service.user.application;

import java.math.BigDecimal;
import kr.hhplus.be.server.api.service.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.service.user.application.dto.UserResult;
import kr.hhplus.be.server.api.service.user.domain.User;
import kr.hhplus.be.server.api.service.user.domain.UserRepository;
import kr.hhplus.be.server.api.exception.InsufficientBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final BalanceHistoryService balanceHistoryService;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public BigDecimal getCurrentBalanceById(long userId) {
		User user = userRepository.findById(userId).orElseThrow();
		return user.getBalance();
	}

	@Transactional(readOnly = true)
	public UserResult getUserById(long userId) {
		return UserResult.from(userRepository.findById(userId).orElseThrow());
	}

	@Transactional(readOnly = true)
	public void payAmount(ReservationDto dto) {
		long userId = dto.userId();
		BigDecimal price = dto.finalPrice();
		BigDecimal currentBalance = getCurrentBalanceById(userId);
		User user = userRepository.findById(userId).orElseThrow();

		if (currentBalance.compareTo(price) >= 0) {
			user.setBalance(currentBalance.subtract(price));
			balanceHistoryService.addWithdrawHistory(userId, price);
		} else {
			throw new InsufficientBalanceException("잔액이 부족합니다.", user.getBalance());
		}
	}

}
