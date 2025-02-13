package kr.hhplus.be.server.api.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import kr.hhplus.be.server.api.user.application.service.BalanceHistoryService;
import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryResult;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistory;
import kr.hhplus.be.server.api.user.application.dto.BalanceHistoryDto;
import kr.hhplus.be.server.api.user.domain.repository.BalanceHistoryRepository;
import kr.hhplus.be.server.api.user.domain.entity.BalanceHistoryType;
import org.junit.Test;

public class BalanceHistoryServiceTest {

	private final BalanceHistoryRepository balanceHistoryRepository = mock(
		BalanceHistoryRepository.class);
	private final BalanceHistoryService balanceHistoryService = new BalanceHistoryService(
		balanceHistoryRepository);


	@Test
	public void 충전이력추가_성공() {
		// given
		BalanceHistoryDto dto = BalanceHistoryDto.builder()
				.userId(1L)
				.type(BalanceHistoryType.CHARGE)
				.amount(BigDecimal.valueOf(10000))
				.build();

		BalanceHistory mock = BalanceHistory.builder().userId(1L)
			.type(BalanceHistoryType.CHARGE).amount(BigDecimal.valueOf(10000)).build();

		// save에 리턴되는 타입을 넣어줘야 함 -> 실제로 테스트할 때 만들어지는 빌더와 참조값이 다르므로
		when(balanceHistoryRepository.save(any(BalanceHistory.class))).thenReturn(mock);

		//when
		BalanceHistoryResult sut = balanceHistoryService.addHistory(dto);
		//then
		assertThat(sut.amount()).isEqualTo(BigDecimal.valueOf(10000));
		assertThat(sut.type()).isEqualTo(BalanceHistoryType.CHARGE);
	}

	@Test
	public void 사용이력추가_성공() {
		// given
		BalanceHistoryDto dto = BalanceHistoryDto.builder()
				.userId(1L)
				.type(BalanceHistoryType.PAY)
				.amount(BigDecimal.valueOf(10000))
				.build();

		BalanceHistory mock = BalanceHistory.builder().userId(1L)
				.type(BalanceHistoryType.PAY).amount(BigDecimal.valueOf(10000)).build();

		when(balanceHistoryRepository.save(any(BalanceHistory.class))).thenReturn(mock);

		//when
		BalanceHistoryResult sut = balanceHistoryService.addHistory(dto);
		//then
		assertThat(sut.amount()).isEqualTo(BigDecimal.valueOf(10000));
		assertThat(sut.type()).isEqualTo(BalanceHistoryType.PAY);
	}


}