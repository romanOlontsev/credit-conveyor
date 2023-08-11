package ru.neoflex.neosudy.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class DealApplication {
    public static void main(String[] args) {
       SpringApplication.run(DealApplication.class, args);
    }
}
