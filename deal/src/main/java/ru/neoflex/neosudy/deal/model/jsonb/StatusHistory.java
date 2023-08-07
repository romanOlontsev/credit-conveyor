package ru.neoflex.neosudy.deal.model.jsonb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.neoflex.neosudy.deal.model.types.ChangeType;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class StatusHistory implements Serializable {
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "time")
    private OffsetDateTime time;
    @JsonProperty(value = "change_type")
    private ChangeType changeType;
}
