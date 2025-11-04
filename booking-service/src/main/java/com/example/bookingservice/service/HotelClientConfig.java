package com.example.bookingservice.service;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class HotelClientConfig {

    @Value("${internal.key}")
    private String internalKey;

    @Bean
    public RequestInterceptor internalKeyInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Internal-Key", internalKey);
            requestTemplate.header("X-Request-Id", UUID.randomUUID().toString());
        };
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(200, 2000, 3);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}