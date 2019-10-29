package com.neuedu.config;

import com.google.common.collect.Lists;
import com.neuedu.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author jyw
 * @date 2019/10/27-17:54
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> addPatterns = Lists.newArrayList();
        addPatterns.add("/user/**");
        addPatterns.add("/cart/**");
        addPatterns.add("/order/**");
        addPatterns.add("/shipping/**");

        List<String> excludePathPatterns = Lists.newArrayList();
        excludePathPatterns.add("/user/register");
        excludePathPatterns.add("/user/login/**");
        excludePathPatterns.add("/user/forget_get_question/**");
        excludePathPatterns.add("/user/forget_check_answer.do");
        excludePathPatterns.add("/user/forget_reset_password.do");
        excludePathPatterns.add("/order/callback.do");


        registry.addInterceptor(loginInterceptor).addPathPatterns(addPatterns)
                .excludePathPatterns(excludePathPatterns);
    }
}
