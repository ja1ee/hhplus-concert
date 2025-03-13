package kr.hhplus.be.server.api.concert.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ConcertSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;

	private Long scheduleId;

	private int seatNo;

	private BigDecimal price;

	private Boolean isReserved = false;

	public void reserve() { this.isReserved = true; }

	public void expire() { this.isReserved = false; }
}
