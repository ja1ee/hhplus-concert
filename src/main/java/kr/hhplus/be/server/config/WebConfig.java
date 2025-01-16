package kr.hhplus.be.server.config;

import kr.hhplus.be.server.api.token.presentation.TokenHeaderInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final TokenHeaderInterceptor tokenHeaderInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenHeaderInterceptor)
			.addPathPatterns("/reservation/**", "/concerts/**", "/users/**");
		//.excludePathPatterns("/auth/**"); // 제외 경로
	}
}
