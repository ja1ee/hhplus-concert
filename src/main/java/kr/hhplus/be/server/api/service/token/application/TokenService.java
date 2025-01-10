package kr.hhplus.be.server.api.service.token.application;

import java.util.Optional;
import kr.hhplus.be.server.api.service.token.application.dto.TokenResult;
import kr.hhplus.be.server.api.service.token.domain.Token;
import kr.hhplus.be.server.api.service.token.domain.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final TokenRepository tokenRepository;

	@Transactional
	public TokenResult createToken(long userId) {
		Optional<Token> existingToken = findByUserId(userId);
		if (existingToken.isPresent()) {
			return TokenResult.from(existingToken.get());
		}
		Token token = new Token();
		token.setUserId(userId);
		token.setIsActivated(false);

		return TokenResult.from(tokenRepository.save(token));
	}

	public Optional<Token> findByUserId(long userId) {
		return tokenRepository.findByUserId(userId);
	}

	@Transactional
	public void deleteTokenByUserId(long userId) {
		Token token = findByUserId(userId).orElseThrow();
		tokenRepository.delete(token);
	}
}
