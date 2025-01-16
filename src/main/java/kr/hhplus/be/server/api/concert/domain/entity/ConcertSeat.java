package kr.hhplus.be.server.api.concert.domain.entity;

import jakarta.persistence.Column;
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
public class ConcertSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long scheduleId;

	private int seatNo;

	private BigDecimal price;

	private Boolean isReserved = false;

	public void reserve() {
		this.isReserved = true;
	}

	public void expire() { this.isReserved = false; }
}
