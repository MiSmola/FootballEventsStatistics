package com.mismola.footballeventsstatistics.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.ResultMessage;
import com.mismola.footballeventsstatistics.service.GetStatisticsProcessingService;
import com.mismola.footballeventsstatistics.service.ResultProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/message")
public class IncomingMessageController {

    private static final Logger log = Logger.getLogger(IncomingMessageController.class.getName());

    @Autowired
    private ResultProcessingService resultProcessingService;

    @Autowired
    private GetStatisticsProcessingService getStatisticsProcessingService;

    @PostMapping
    public ResponseEntity<String> processMessage(@RequestBody JsonNode jsonNode) {
        try {
            validateInput(jsonNode);

            String type = jsonNode.get("type").asText();

            switch (type) {
                case "RESULT":
                    return ResponseEntity.ok(handleResultMessage(parseResultMessage(jsonNode)));

                case "GET_STATISTICS":
                    return ResponseEntity.ok(handleGetStatisticsMessage(parseStatisticsTeams(jsonNode)));

                default:
                    log.warning("Unsupported message type: " + type);
                    return ResponseEntity.badRequest().body("Unsupported message type: " + type);
            }

        } catch (InvalidInputException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.severe("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the message.");
        }
    }

    private void validateInput(JsonNode jsonNode) {
        if (jsonNode == null || !jsonNode.has("type")) {
            throw new InvalidInputException("Invalid message: Missing 'type' field.");
        }
    }

    private ResultMessage parseResultMessage(JsonNode jsonNode) {
        JsonNode resultNode = jsonNode.path("result");

        if (!resultNode.path("home_score").isInt() || !resultNode.path("away_score").isInt()) {
            throw new InvalidInputException("'home_score' and 'away_score' must be integers.");
        }

        return ResultMessage.builder()
                .homeTeam(resultNode.path("home_team").asText())
                .awayTeam(resultNode.path("away_team").asText())
                .homeScore(resultNode.path("home_score").asInt())
                .awayScore(resultNode.path("away_score").asInt())
                .build();
    }

    private List<String> parseStatisticsTeams(JsonNode jsonNode) {
        JsonNode teamsNode = jsonNode.path("get_statistics").path("teams");
        if (!teamsNode.isArray()) {
            throw new InvalidInputException("'teams' must be an array of strings.");
        }

        List<String> teams = new ArrayList<>();
        for (JsonNode teamNode : teamsNode) {
            teams.add(teamNode.asText());
        }
        return teams;
    }

    private String handleResultMessage(ResultMessage resultMessage) {
        log.info("Processing RESULT message");
        resultProcessingService.populateResultData(resultMessage);
        return resultProcessingService.customResultResponse(resultMessage);
    }

    private String handleGetStatisticsMessage(List<String> teamNames) {
        log.info("Processing GET_STATISTICS message");
        return getStatisticsProcessingService.customGetStatisticsResponse(teamNames);
    }

    private static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message) {
            super(message);
        }
    }
}