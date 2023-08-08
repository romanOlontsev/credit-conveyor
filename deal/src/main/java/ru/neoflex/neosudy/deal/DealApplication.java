package ru.neoflex.neosudy.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
//@EnableConfigurationProperties(AppConfig.class)
public class DealApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DealApplication.class, args);
//            System.out.println(run.getBean(AppConfig.class));
    }
}
