package ru.neoflex.neostudy.conveyor.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.conveyor", ignoreUnknownFields = false)
public record AppConfig(@NotNull double baseRate) {
}
