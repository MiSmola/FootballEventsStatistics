package com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
public class GetStatisticsMessage {
    private List<String> teams;
}
