package com.beertag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class BeerTagApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeerTagApplication.class, args);
    }

}
