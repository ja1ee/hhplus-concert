package kr.hhplus.be.server.api.user;

import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.application.dto.UserResult;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;
import kr.hhplus.be.server.api.user.domain.entity.User;
import kr.hhplus.be.server.api.user.domain.repository.UserRepository;
import kr.hhplus.be.server.api.user.domain.service.UserService;
import kr.hhplus.be.server.api.common.exception.ErrorCode;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

	private final UserRepository userRepository = mock(UserRepository.class);
	private final UserService userService = new UserService(userRepository);

	@Test
	public void 잔액충전_성공() {
		// given
		BalanceHistoryDto dto = new BalanceHistoryDto(1L, BalanceHistoryType.CHARGE, BigDecimal.valueOf(20000));

		User user = User.builder()
				.id(1L)
				.balance(BigDecimal.valueOf(10000))
				.build();

		when(userRepository.findByIdWithLock(1L)).thenReturn(Optional.ofNullable(user));
        assert user != null;
        when(userRepository.save(user)).thenReturn(user);

		// when
		UserResult sut = userService.chargeAmount(dto);

		// then
		assertThat(sut.balance()).isEqualTo(BigDecimal.valueOf(30000));
	}

	@Test
	public void 잔액충전_사용자존재하지않으면_NOT_FOUND_USER() {
		// given
		BalanceHistoryDto dto = new BalanceHistoryDto(1L, BalanceHistoryType.CHARGE, BigDecimal.valueOf(20000));

		when(userRepository.findByIdWithLock(1L))
				.thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.chargeAmount(dto)).hasMessage(ErrorCode.NOT_FOUND_USER.getReason());
	}

	@Test
	public void 잔액사용_성공() {
		// given
		BalanceHistoryDto dto = new BalanceHistoryDto(1L, BalanceHistoryType.PAY, BigDecimal.valueOf(10000));

		User user = User.builder()
				.id(1L)
				.balance(BigDecimal.valueOf(20000))
				.build();

		when(userRepository.findByIdWithLock(1L)).thenReturn(Optional.ofNullable(user));
		assert user != null;
		when(userRepository.save(user)).thenReturn(user);

		// when
		UserResult sut = userService.payAmount(dto);

		// then
		assertThat(sut.balance()).isEqualTo(BigDecimal.valueOf(10000));
	}

	@Test
	public void 잔액사용_잔액부족하면_INSUFFICIENT_BALANCE() {
		// given
		BalanceHistoryDto dto = new BalanceHistoryDto(1L, BalanceHistoryType.PAY, BigDecimal.valueOf(20000));

		User user = User.builder()
				.id(1L)
				.balance(BigDecimal.valueOf(10000))
				.build();

		when(userRepository.findByIdWithLock(1L)).thenReturn(Optional.ofNullable(user));

		// when & then
		assertThatThrownBy(() -> userService.payAmount(dto)).hasMessage(ErrorCode.INSUFFICIENT_BALANCE.getReason());
	}


}