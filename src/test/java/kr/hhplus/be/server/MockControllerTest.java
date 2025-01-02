package kr.hhplus.be.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.concertbooking.sample.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest({MockBalanceController.class, MockConcertController.class, MockTokenController.class})
public class MockControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	// ----------------------- MockTokenController Tests -----------------------
	@Test
	void 토큰발급_테스트() throws Exception {
		String userId = "testUser";
		mockMvc.perform(post("/sample/tokens")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("예약 가능한 날짜가 조회되었습니다."))
			.andExpect(jsonPath("$.data.user_id").value(userId))
			.andExpect(jsonPath("$.data.status").value("waiting"))
			.andExpect(jsonPath("$.data.id").exists())
			.andExpect(jsonPath("$.data.expired_at").exists())
			.andExpect(jsonPath("$.data.created_at").exists());
	}
	// ---------------------------------------------------------------------------
	// ----------------------- MockBalanceController Tests -----------------------
	@Test
	void 잔액조회_테스트() throws Exception {
		Long userId = 123456L;
		mockMvc.perform(post("/sample/users/{userId}/balance", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"userId\": " + userId + "}"))
			.andExpect(status().isOk()) // 상태 코드 확인
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("잔액이 조회되었습니다."))
			.andExpect(jsonPath("$.data.id").value(userId))
			.andExpect(jsonPath("$.data.balance").isNumber());
	}
	@Test
	void 잔액충전_테스트() throws Exception {
		Long userId = 123456L;
		Long chargeAmount = 5000L;
		mockMvc.perform(post("/sample/users/{userId}/charge", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"userId\": " + userId + ", \"amount\": " + chargeAmount + "}"))
			.andExpect(status().isOk()) // 상태 코드 확인
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("충전이 완료되었습니다."))
			.andExpect(jsonPath("$.data.id").value(userId))
			.andExpect(jsonPath("$.data.newBalance").isNumber());
	}
	@Test
	void 좌석결제_테스트() throws Exception {
		Long userId = 123456L;
		Long paymentAmount = 20000L;

		// 충전된 잔액을 임의로 설정
		mockMvc.perform(post("/sample/users/{userId}/charge", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"userId\": " + userId + ", \"amount\": 30000}"))
			.andExpect(status().isOk());

		mockMvc.perform(post("/sample/payments")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"userId\": " + userId + ", \"reservationId\": 987654, \"paymentInfo\": {\"amount\": " + paymentAmount + ", \"method\": \"CREDIT_CARD\"}}"))
			.andExpect(status().isOk()) // 상태 코드 확인
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("예매가 완료되었습니다."))
			.andExpect(jsonPath("$.data.id").value(123456))
			.andExpect(jsonPath("$.data.seat_no").value(40))
			.andExpect(jsonPath("$.data.concert_date").value("2025-01-01"))
			.andExpect(jsonPath("$.data.final_price").value(111000));
	}
	@Test
	void 잔액부족_결제실패_테스트() throws Exception {
		Long userId = 123456L;
		Long paymentAmount = 50000L;

		mockMvc.perform(post("/sample/payments")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"userId\": " + userId + ", \"reservationId\": 987654, \"paymentInfo\": {\"amount\": " + paymentAmount + ", \"method\": \"CREDIT_CARD\"}}"))
			.andExpect(status().isUnprocessableEntity()) // 상태 코드가 422 UNPROCESSABLE_ENTITY인지 확인
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.message").value("잔액이 부족합니다. 충전 후 다시 시도해주세요."))
			.andExpect(jsonPath("$.error.code").value("INSUFFICIENT_BALANCE"));
	}
	// ---------------------------------------------------------------------------
	// ----------------------- MockConcertController Tests -----------------------
	@Test
	void 예매가능날짜조회_테스트() throws Exception {
		String concertId = "1234567";
		mockMvc.perform(get("/sample/concerts/{concertId}/schedules", concertId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("예약 가능한 날짜가 조회되었습니다."))
			.andExpect(jsonPath("$.data.concertId").value(concertId))
			.andExpect(jsonPath("$.data.dates").isArray())
			.andExpect(jsonPath("$.data.dates[0].id").value(987654))
			.andExpect(jsonPath("$.data.dates[0].concert_date").value("2025-01-01"));
	}

	@Test
	void 좌석예매_테스트() throws Exception {
		String concertId = "1234567";
		mockMvc.perform(post("/sample/concerts/{concertId}/reservation", concertId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"date\": \"2025-01-01\", \"seatId\": 987654, \"seatNo\": 30 }"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("좌석 임시 배정이 완료되었습니다."))
			.andExpect(jsonPath("$.data.status").value("reserved"))
			.andExpect(jsonPath("$.data.expired_at").exists());
	}
	// ---------------------------------------------------------------------------
}