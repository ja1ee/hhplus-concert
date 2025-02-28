package kr.hhplus.be.server.api.reservation.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationStatus;
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

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDate concertDate;

	private BigDecimal finalPrice;

	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(nullable = false)
	private LocalDateTime expiredAt;

	public void confirm() {
		this.status = ReservationStatus.reserved;
	}

	public void fail() {
		this.status = ReservationStatus.failed;
	}

	public void expire() {
		this.status = ReservationStatus.expired;
	}
}
