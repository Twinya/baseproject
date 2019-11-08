package com.appengine.frame.filters;

import com.appengine.frame.utils.StringToEnumConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 15-6-8 23:15.
 */
@Configuration
public class FilterConfigFactory implements WebMvcConfigurer {

    @Autowired
    StringToEnumConverterFactory stringToEnumConverterFactory;

    @Bean
    public FilterRegistrationBean requestLogChain() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter headerFilter = new RequestLogFilter();
        registration.setFilter(headerFilter);
        registration.setOrder(Integer.MAX_VALUE - 1);
        //拦截错误转发
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Bean
    public FilterRegistrationBean headerFilterChain() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter headerFilter = new HeaderResponseFilter();
        registration.setFilter(headerFilter);
        registration.setOrder(Integer.MAX_VALUE - 1);
        return registration;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(stringToEnumConverterFactory);
    }
}
