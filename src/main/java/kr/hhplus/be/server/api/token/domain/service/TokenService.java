package kr.hhplus.be.server.api.token.domain.service;

import kr.hhplus.be.server.api.token.application.dto.TokenResult;
import kr.hhplus.be.server.api.token.domain.entity.Token;
import kr.hhplus.be.server.api.token.domain.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final TokenRepository tokenRepository;

	@Transactional
	public TokenResult createToken(long userId) {
		Token existingToken = findByUserId(userId);
		if (existingToken != null) {
			return TokenResult.from(existingToken);
		}

		Token token = new Token(userId);
		return TokenResult.from(tokenRepository.save(token));
	}

	public Token findByUserId(long userId) {
		return tokenRepository.findByUserId(userId);
	}

	@Transactional
	public void deleteTokenByUserId(long userId) {
		Token token = findByUserId(userId);
		tokenRepository.delete(token);
	}
}
