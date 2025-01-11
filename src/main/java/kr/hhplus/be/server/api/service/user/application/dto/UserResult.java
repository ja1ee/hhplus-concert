package kr.hhplus.be.server.api.service.user.application.dto;

import java.math.BigDecimal;
import kr.hhplus.be.server.api.service.user.domain.User;

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
