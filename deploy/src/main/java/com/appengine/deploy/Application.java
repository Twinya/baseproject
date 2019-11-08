package com.appengine.deploy;

import com.appengine.common.config.DefaultProfileLoader;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @author fuyou
 * @version 1.0 Created at: 2015-04-29 16:17
 */
@Configuration
@EnableEurekaClient
//多数据源不能自动配置
//@EnableAutoConfiguration
@EnableTransactionManagement
@ImportResource("classpath:spring-context.xml")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class})
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        DefaultProfileLoader.getInstance().getEnv();
        SpringApplication application = new SpringApplication(Application.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        String tmpUtl = "/filecache/";
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            tmpUtl = "C:\\filecache\\";
        } else if (osName.contains("Mac OS X")) {
            tmpUtl = "/Users/filecache/";
        } else if (osName.contains("Linux")) {
            tmpUtl = "/filecache/";
        }
        MultipartConfigFactory factory = new MultipartConfigFactory();
        File tmp = new File(tmpUtl);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        factory.setLocation(tmpUtl);
        return factory.createMultipartConfig();
    }
    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
