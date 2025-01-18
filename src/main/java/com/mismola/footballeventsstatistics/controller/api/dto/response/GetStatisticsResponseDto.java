package com.mismola.footballeventsstatistics.controller.api.dto.response;

import lombok.*;

/**
 * A data transfer object used for transferring the statistical data of last 3 matches to the Response.
 */
@AllArgsConstructor
@Getter
@Setter
@Data
@NoArgsConstructor
@Builder
public class GetStatisticsResponseDto {
    private String teamName;
    private String WDL;
    private Float average;
    private Integer eventsPlayed;
    private Integer pointsGained;
    private Integer goalsScoredSum;
    private Integer goalsConcededSum;

    @Override
    public String toString() {
        return teamName + " " +
                WDL + " " +
                average + " " +
                eventsPlayed + " " +
                pointsGained + " " +
                goalsScoredSum + " " +
                goalsConcededSum + "\n";
    }
}
