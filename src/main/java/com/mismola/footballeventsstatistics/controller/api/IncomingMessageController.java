package com.mismola.footballeventsstatistics.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.GetStatisticsMessage;
import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.ResultMessage;
import com.mismola.footballeventsstatistics.services.GetStatisticsProcessingService;
import com.mismola.footballeventsstatistics.services.ResultProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/message")
public class IncomingMessageController {

    Logger log = Logger.getLogger(IncomingMessageController.class.getName());

    @Autowired
    private ResultProcessingService resultProcessingService;

    @Autowired
    private GetStatisticsProcessingService getStatisticsProcessingService;

//    @Autowired
//    private GeneralTeamStatsService generalTeamStatsService;

    @PostMapping
    public String processMessage(@RequestBody JsonNode jsonNode) throws IOException {

        String type = jsonNode.get("type").asText();

        if (Objects.equals(type, "RESULT")) {
            ResultMessage resultMessage = ResultMessage.builder()
                    .homeTeam(jsonNode.at("/result/home_team").asText())
                    .awayTeam(jsonNode.at("/result/away_team").asText())
                    .homeScore(jsonNode.at("/result/home_score").asInt())
                    .awayScore(jsonNode.at("/result/away_score").asInt())
                    .build();

            log.info(resultMessage.toString());
            return handleResultMessage(resultMessage);

        } else if (Objects.equals(type, "GET_STATISTICS")) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonNode.toString());
            JsonNode teamsNode = rootNode.path("get_statistics").path("teams");
            List<String> teams = new ArrayList<>();
            for (JsonNode teamNode : teamsNode) {
                teams.add(teamNode.asText());
            }
//            GetStatisticsMessage getStatisticsMessage = GetStatisticsMessage.builder().teams(teams).build();
            log.info(teams.toString());
            return handleGetStatisticsMessage(teams);

        } else {
            return "Unsupported message type: " + type;
        }
    }

    private String handleResultMessage(ResultMessage resultMessage) {
        resultProcessingService.populateResultData(resultMessage);
        return resultProcessingService.customResultResponse(resultMessage);
    }

    private String handleGetStatisticsMessage(List<String> teamNames) {
        //TODO: GetStatisticsMessage logic

        log.info("GET_STATISTICS message detected");
        return getStatisticsProcessingService.customGetStatisticsResponse(teamNames);
    }
}
