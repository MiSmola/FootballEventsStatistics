package com.mismola.footballeventsstatistics.model.dto;

import lombok.*;

/**
 * Data transfer object for handling manipulations on General Teams' Statistics.
 */
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
