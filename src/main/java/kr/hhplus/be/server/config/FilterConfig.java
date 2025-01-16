package kr.hhplus.be.server.config;

import kr.hhplus.be.server.filter.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new Filter());
        filterRegistrationBean.addUrlPatterns("/reservation/**", "/concerts/**", "/users/**", "/tokens/**");

        return filterRegistrationBean;
    }
}
