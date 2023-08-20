package com.xxxx.crm.config;

import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * description:
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/11 22:48:31
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       //需要实现拦截器功能的实例对象，noLoginInterceptor
        registry.addInterceptor(noLoginInterceptor())
                .addPathPatterns("/**")//默认拦截所有
                //不需要被拦截的资源
                .excludePathPatterns("/css/**","/images/**","/js/**","/lib/**","/index","/user/login");
    }
}
