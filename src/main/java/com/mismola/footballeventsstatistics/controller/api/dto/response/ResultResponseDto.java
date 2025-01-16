package com.mismola.footballeventsstatistics.controller.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResultResponseDto {
    private Integer sumOfEventsPlayed;
    private Integer sumOfGainedPoints;
    private Integer sumOfScoredGoals;
    private Integer sumOfConcededGoals;

}
