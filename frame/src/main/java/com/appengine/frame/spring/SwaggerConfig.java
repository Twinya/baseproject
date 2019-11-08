package com.appengine.frame.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fuyou
 * @version 1.0 Created at: 2016-10-18 20:10
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket merchantStoreApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("internal-api")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .apis(RequestHandlerSelectors.any()) // 对所有api进行监控
                .paths(PathSelectors.any()) // 对所有路径进行监控
                .build()
                .globalOperationParameters(setHeaderToken())
                .apiInfo(testApiInfo());
    }

    private ApiInfo testApiInfo() {
        return new ApiInfo("接口文档",
                "文档说明",
                "0.1",
                "NO terms of service",
                new Contact("fuyou", "https://github.com/fuyou", "fuyou@gmail.com"),
                "The Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0.html",
                Collections.emptyList()
        );
    }

    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Token").description("token 登陆或注册返回的 登陆后的接口添加").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }
}