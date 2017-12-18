package com.bingo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则,excludePathPatterns 用户排除拦截
        registry.addInterceptor(new SystemInterceptHandler())
                .addPathPatterns("/**").excludePathPatterns("/").excludePathPatterns("/*.html")
                .excludePathPatterns("/user/login").excludePathPatterns("/user/register")
                .excludePathPatterns("/user/existEmail");
        super.addInterceptors(registry);
    }
}
