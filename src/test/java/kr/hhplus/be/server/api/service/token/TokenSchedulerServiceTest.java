package kr.hhplus.be.server.api.service.token;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import kr.hhplus.be.server.api.service.token.application.TokenSchedulerService;
import kr.hhplus.be.server.api.service.token.domain.Token;
import kr.hhplus.be.server.api.service.token.domain.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.Instant;
import java.util.*;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
public class TokenSchedulerServiceTest {

	private final TokenRepository tokenRepository = mock(TokenRepository.class);
	private final TokenSchedulerService tokenSchedulerService = new TokenSchedulerService(
		tokenRepository);

	private Instant currentTime;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		currentTime = Instant.now();
	}
	

	@Test
	public void 활성화토큰_40개일때_대기중인토큰_10개활성화성공() {
		// given
		long activeTokenCount = 40;  // 이미 활성화된 토큰이 40개
		int tokensToActivate = 10;  // 활성화 가능한 토큰 수는 10개
		Token waitingToken1 = new Token();
		Token waitingToken2 = new Token();
		List<Token> waitingTokens = Arrays.asList(waitingToken1, waitingToken2);

		when(tokenRepository.countByIsActivatedTrue()).thenReturn(activeTokenCount);
		when(tokenRepository.findTopNByIsActivatedFalseOrderByIdAsc(tokensToActivate)).thenReturn(
			waitingTokens);

		// when
		tokenSchedulerService.activateWaitingTokens();

		// then
		assertThat(waitingTokens).allMatch(token -> token.getIsActivated());  // 모든 토큰이 활성화되어야 함
		verify(tokenRepository, times(1)).saveAll(waitingTokens);
	}

	@Test
	public void 활성화토큰_50개일때_대기중인토큰_활성화실패() {
		// given
		long activeTokenCount = 50;  // 이미 활성화된 토큰이 50개 있다고 가정
		Token waitingToken1 = new Token();
		Token waitingToken2 = new Token();
		List<Token> waitingTokens = Arrays.asList(waitingToken1, waitingToken2);

		when(tokenRepository.countByIsActivatedTrue()).thenReturn(activeTokenCount);  // 활성화된 토큰 수

		// when
		tokenSchedulerService.activateWaitingTokens();

		// then
		assertThat(waitingTokens).allMatch(token -> !token.getIsActivated());  // 아무 토큰도 활성화되지 않음
		verify(tokenRepository, never()).saveAll(waitingTokens);  // 저장 메서드는 호출되지 않아야 함
	}

}
