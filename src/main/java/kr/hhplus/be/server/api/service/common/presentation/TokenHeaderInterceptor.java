package kr.hhplus.be.server.api.service.common.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import kr.hhplus.be.server.api.service.common.ErrorResult;
import kr.hhplus.be.server.api.service.token.application.TokenService;
import kr.hhplus.be.server.api.service.token.application.dto.TokenResult;
import kr.hhplus.be.server.api.service.token.presentation.dto.TokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class TokenHeaderInterceptor implements HandlerInterceptor {

	private final TokenService tokenService;
	private final ObjectMapper objectMapper;

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {
		String userIdHeader = request.getHeader("Authorization");
		userIdHeader = "1"; // todo: 임시데이터 삭제

		if (userIdHeader == null || userIdHeader.isEmpty()) {
			return respondUnauthorized(response, "Authorization header가 유효하지 않습니다.");
		}

		Long userId = parseUserId(userIdHeader);
		if (userId == null || !isValidToken(userId)) {
			return respondUnauthorized(response, "만료된 토큰입니다.");
		}

		TokenResult tokenResult = getTokenResult(userId);

		return respondWithToken(response, tokenResult);
	}

	private boolean respondWithToken(HttpServletResponse response, TokenResult tokenResult)
		throws IOException {
		response.setContentType("application/json");
		response.getWriter()
			.write(objectMapper.writeValueAsString(TokenResponse.from(tokenResult)));
		return true;
	}

	private boolean respondUnauthorized(HttpServletResponse response, String errorMessage)
		throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter()
			.write(objectMapper.writeValueAsString(
				new ErrorResult(HttpStatus.NOT_FOUND.value(), errorMessage)));
		return false;
	}

	private Long parseUserId(String userIdHeader) {
		try {
			return Long.parseLong(userIdHeader);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private TokenResult getTokenResult(Long userId) {
		return TokenResult.from(tokenService.findByUserId(userId)
			.orElseThrow(
				() -> new EntityNotFoundException("Token not found for userId: " + userId)));
	}

	private boolean isValidToken(long userId) {
		TokenResult result = TokenResult.from(tokenService.findByUserId(userId).orElseThrow());
		Instant currentTime = Instant.now();
		return result.expiredAt().isAfter(currentTime);
	}

}