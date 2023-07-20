package ru.neoflex.neostudy.conveyor.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationStatusHistoryDTO {
    private Enum status;
    private LocalDateTime time;
    private Enum changeType;
}
