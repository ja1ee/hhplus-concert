package kr.hhplus.be.server.api.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private long userId;

	@Column(nullable = false)
	private long seatId;

	@Column(nullable = false)
	private long seatNo;

	@Column(nullable = false)
	private LocalDate concertDate;

	@Column(nullable = false)
	private BigDecimal finalPrice;

	@Column(nullable = false)
	private Boolean isReserved = false;

	@Column(nullable = false)
	private LocalDateTime expiredAt;

	public void confirm() {
		this.expiredAt = null;
	}

	public void open() {
		this.isReserved = false;
	}
}
