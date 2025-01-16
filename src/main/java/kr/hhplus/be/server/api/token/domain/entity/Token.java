package kr.hhplus.be.server.api.token.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long userId;

	private Boolean isActivated = false; // 대기열 진입 시 활성화

	private LocalDateTime expiredAt;

	public void activate(LocalDateTime time) {
		isActivated = true;
		expiredAt = time;
	}

	public Token(long userId) {
		this.userId = userId;
		this.isActivated = false;
	}
}
