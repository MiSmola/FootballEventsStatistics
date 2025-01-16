package com.mismola.footballeventsstatistics.controller.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A data tranfer object used for transferring the statistical data of last 3 matches to the Response.
 */
@AllArgsConstructor
@Getter
@Setter
public class GetStatisticsResponseDto {
    private String WDL;
    private float average;
    private Integer eventsPlayed;
    private Integer pointsGained;
    private Integer goalsScoredSum;
    private Integer goalsConcededSum;
}
