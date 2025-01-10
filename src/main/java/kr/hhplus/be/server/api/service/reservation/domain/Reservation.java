package kr.hhplus.be.server.api.service.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
	private Instant expiredAt;
}
