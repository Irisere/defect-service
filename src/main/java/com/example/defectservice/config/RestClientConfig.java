package com.example.defectservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // 标识这是一个配置类
public class RestClientConfig {

    @Bean // 将 RestTemplate 的实例交给 Spring 容器管理
    public RestTemplate restTemplate() {
        // 可以在这里配置超时时间等参数
        return new RestTemplate();
    }
}