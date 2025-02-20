package kr.hhplus.be.server.api.reservation.domain.entity;

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
@ToString
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private Long concertId;

	private Long seatId;

	private int seatNo;

	private LocalDate concertDate;

	private BigDecimal finalPrice;

	private Boolean isReserved = false;

	@Column(nullable = false)
	private LocalDateTime expiredAt;

	public void confirm() {
		this.expiredAt = null;
	}

	public void expire() {
		this.isReserved = false;
	}
}
