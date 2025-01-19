package com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage;

import lombok.*;

/**
 * Data transfer object for data received with "result" type messages.
 */
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
