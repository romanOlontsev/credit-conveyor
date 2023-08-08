package ru.neoflex.neosudy.deal.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "passport", schema = "credit_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "passport_id")
    private Long passportId;
    @JsonProperty(value = "series")
    private String series;
    @JsonProperty(value = "number")
    private String number;
    @JsonProperty(value = "issue_branch")
    private String issueBranch;
    @JsonProperty(value = "issue_date")
    private OffsetDateTime issueDate;
}
