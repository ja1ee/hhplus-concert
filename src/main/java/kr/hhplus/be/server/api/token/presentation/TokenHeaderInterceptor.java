package kr.hhplus.be.server.api.token.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.response.ErrorResponse;
import kr.hhplus.be.server.api.token.domain.service.TokenService;
import kr.hhplus.be.server.api.token.application.dto.TokenResult;
import kr.hhplus.be.server.api.token.presentation.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class TokenHeaderInterceptor implements HandlerInterceptor {

	private final TokenService tokenService;
	private final ObjectMapper objectMapper;


	// todo
	//* TokenHeaderInterceptor에서 토큰에 관련된 필요 이상의 로직이 포함되어 있어요.
	// 토큰을 생성한다거나, 토큰의 상태를 확인한다거나.. 유효한 토큰인지 검증을 토큰서비스에 요청하는 것에 집중해주세요.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		String userIdHeader = request.getHeader("userId");

		if (userIdHeader == null || userIdHeader.isEmpty()) {
			return respondUnauthorized(response, "Unauthorized access");
		}

		Long userId = parseUserId(userIdHeader);
		TokenResult tokenResult = tokenService.createToken(userId);

		if (!tokenResult.isActivated()) {
			return respondWithToken(response, tokenResult);
		}
		return true;
	}

	private boolean respondWithToken(HttpServletResponse response, TokenResult tokenResult)
		throws IOException {
		response.setContentType("application/json");
		response.getWriter()
			.write(objectMapper.writeValueAsString(TokenResponse.from(tokenResult)));
		return false;
	}

	private boolean respondUnauthorized(HttpServletResponse response, String errorMessage)
		throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter()
			.write(objectMapper.writeValueAsString(
				new ErrorResponse(HttpStatus.NOT_FOUND.value(), errorMessage)));
		return false;
	}

	private Long parseUserId(String userIdHeader) {
		try {
			return Long.parseLong(userIdHeader);
		} catch (NumberFormatException e) {
			throw new CustomException(ErrorCode.NOT_FOUND_USER);
		}
	}

}