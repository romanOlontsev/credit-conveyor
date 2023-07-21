package ru.neoflex.neostudy.conveyor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.neoflex.neostudy.conveyor.config.AppConfig;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class ConveyorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ConveyorApplication.class, args);
        System.out.println(run.getBean(AppConfig.class));
    }

}
