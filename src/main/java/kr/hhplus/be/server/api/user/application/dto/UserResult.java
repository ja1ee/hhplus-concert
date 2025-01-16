package kr.hhplus.be.server.api.user.application.dto;

import java.math.BigDecimal;
import kr.hhplus.be.server.api.user.domain.entity.User;

public record UserResult(
	long id,
	BigDecimal balance
) {

	public static UserResult from(User user) {
		return new UserResult(
			user.getId(),
			user.getBalance()
		);
	}
}
