package ru.neoflex.neostudy.deal.model.jsonb;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ru.neoflex.neostudy.deal.model.types.ChangeType;
import ru.neoflex.neostudy.deal.model.types.ApplicationStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class StatusHistory implements Serializable {
    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "status")
    private ApplicationStatus status;
    @JsonProperty(value = "time")
    private LocalDateTime time;
    @JsonProperty(value = "change_type")
    private ChangeType changeType;
}
