package kr.hhplus.be.server.common.config;

import kr.hhplus.be.server.common.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {
    @Bean
    public FilterRegistrationBean<LoggingFilter> filterRegistrationBean() {
        final FilterRegistrationBean<LoggingFilter> filterRegistrationBean = new FilterRegistrationBean<>(new LoggingFilter());
        filterRegistrationBean.addUrlPatterns("/reservation/**", "/concerts/**", "/users/**", "/tokens/**");

        return filterRegistrationBean;
    }
}
