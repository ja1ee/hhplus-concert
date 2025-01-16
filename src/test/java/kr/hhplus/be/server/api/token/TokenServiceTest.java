package kr.hhplus.be.server.api.token;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import kr.hhplus.be.server.api.token.domain.service.TokenService;
import kr.hhplus.be.server.api.token.application.dto.TokenResult;
import kr.hhplus.be.server.api.token.domain.entity.Token;
import kr.hhplus.be.server.api.token.domain.repository.TokenRepository;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TokenServiceTest {

	private final TokenRepository tokenRepository = mock(TokenRepository.class);
	private final TokenService tokenService = new TokenService(tokenRepository);

	@Test
	public void 토큰발급_성공() {
		// given
		long userId = 1L;
		Token mockToken = Token.builder().userId(userId).isActivated(false).build();

		when(tokenRepository.findByUserId(userId)).thenReturn(null);
		when(tokenRepository.save(any(Token.class))).thenReturn(mockToken);

		// when
		TokenResult result = tokenService.createToken(userId);

		// then
		assertThat(result.isActivated()).isEqualTo(false);
	}

}