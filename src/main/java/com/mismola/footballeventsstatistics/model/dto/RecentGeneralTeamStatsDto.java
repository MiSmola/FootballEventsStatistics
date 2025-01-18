package com.mismola.footballeventsstatistics.model.dto;

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
