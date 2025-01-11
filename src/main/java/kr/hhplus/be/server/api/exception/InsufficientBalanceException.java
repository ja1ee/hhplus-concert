package kr.hhplus.be.server.api.exception;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class InsufficientBalanceException extends RuntimeException {

	private final BigDecimal balance;

	public InsufficientBalanceException(String message, BigDecimal balance) {
		super(message);
		this.balance = balance;
	}
}
