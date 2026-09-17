package com.example.API_commerce_pw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApiCommercePwApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiCommercePwApplication.class, args);
    }
}