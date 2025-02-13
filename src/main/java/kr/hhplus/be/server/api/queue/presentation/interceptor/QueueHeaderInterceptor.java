package kr.hhplus.be.server.api.queue.presentation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import kr.hhplus.be.server.api.queue.application.service.QueueService;
import kr.hhplus.be.server.api.queue.application.dto.TokenStatusResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class QueueHeaderInterceptor implements HandlerInterceptor {

	private final QueueService queueService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		String userId = request.getHeader("userId");

		if (userId == null || userId.isEmpty()) {
			return respondUnauthorized(response, "Unauthorized access");
		}

		TokenStatusResult tokenStatus = queueService.checkQueueStatus(userId);

		Boolean isActivated = tokenStatus.isActivated();
		if (Boolean.TRUE.equals(isActivated)) {
			return true;
		}

		Long rank = tokenStatus.rank();
		if (rank != null) {
			return respondWithRank(response, rank + 1);
		}

		return respondUnauthorized(response, "User not found in wait queue");
	}

	private boolean respondWithRank(HttpServletResponse response, Long rank) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("User's rank: " + rank);
		return false;
	}

	private boolean respondUnauthorized(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(message);
		return false;
	}
}