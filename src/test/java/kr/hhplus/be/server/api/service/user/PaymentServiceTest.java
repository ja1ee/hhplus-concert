package kr.hhplus.be.server.api.service.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import kr.hhplus.be.server.api.service.user.application.BalanceHistoryService;
import kr.hhplus.be.server.api.service.user.application.dto.BalanceHistoryResult;
import kr.hhplus.be.server.api.service.user.domain.BalanceHistory;
import kr.hhplus.be.server.api.service.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.service.user.domain.BalanceHistoryRepository;
import org.junit.Test;

public class PaymentServiceTest {

	private final BalanceHistoryRepository balanceHistoryRepository = mock(
		BalanceHistoryRepository.class);
	private final BalanceHistoryService balanceHistoryService = new BalanceHistoryService(
		balanceHistoryRepository);


	@Test
	public void 잔액충전_성공() {
		// given
		BalanceHistoryDto dto = BalanceHistoryDto.builder().userId(1L)
			.amount(BigDecimal.valueOf(10000)).type("charge").build();

		BalanceHistory mock = BalanceHistory.builder().userId(1L)
			.type("charge").amount(BigDecimal.valueOf(10000)).changedAt(Instant.now()).build();

		// save에 리턴되는 타입을 넣어줘야 함 -> 실제로 테스트할 때 만들어지는 빌더와 참조값이 다르므로
		when(balanceHistoryRepository.save(any(BalanceHistory.class))).thenReturn(mock);

		//when
		BalanceHistoryResult result = balanceHistoryService.addChargeHistory(dto);
		//then
		assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(10000));
	}


}