package com.mismola.footballeventsstatistics.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class RecentGeneralTeamStatsDto {
    private Integer sumOfPlayedEvents;
    private Integer sumOfPoints;
    private Integer sumOfGoalsScored;
    private Integer sumOfGoalsConceded;
}
