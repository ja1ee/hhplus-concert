package kr.hhplus.be.server.api.user.domain.service;

import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.domain.entity.User;
import kr.hhplus.be.server.api.user.domain.repository.UserRepository;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.api.user.application.dto.UserResult;
import kr.hhplus.be.server.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UserResult payAmount(BalanceHistoryDto dto) {
		BigDecimal amount = dto.amount();
		User user = userRepository.findByIdWithLock(dto.userId())
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
		BigDecimal currentBalance = user.getBalance();

		if (currentBalance.compareTo(amount) < 0) {
			throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
		}
		user.pay(amount);
		return UserResult.from(userRepository.save(user));
	}

	@Transactional
	public UserResult chargeAmount(BalanceHistoryDto dto) {
		if (dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new CustomException(ErrorCode.INSUFFICIENT_CHARGE);
		};

		User user = userRepository.findByIdWithLock(dto.userId())
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
		user.charge(dto.amount());
		return UserResult.from(userRepository.save(user));
	}

	@Transactional(readOnly = true)
	public UserResult getUserById(long userId) {
		return UserResult.from(userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)));
	}


}
