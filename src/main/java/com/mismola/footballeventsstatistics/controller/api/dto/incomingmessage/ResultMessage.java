package com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
public class ResultMessage {

    private String homeTeam;
    private String awayTeam;
    private Integer homeScore;
    private Integer awayScore;
}
