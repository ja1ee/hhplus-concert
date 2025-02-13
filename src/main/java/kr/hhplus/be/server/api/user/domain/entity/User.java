package kr.hhplus.be.server.api.user.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private BigDecimal balance;

	public void pay(BigDecimal amount) {
		this.balance = this.balance.subtract(amount);
	}

	public void charge(BigDecimal amount) {
		this.balance = this.balance.add(amount);
	}
}
