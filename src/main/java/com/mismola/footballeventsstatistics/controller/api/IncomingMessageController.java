package com.mismola.footballeventsstatistics.controller.api;

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

/**
 * Class managing the main logic of the endpoint that consumes incoming JSON message and processes it.
 */
@RestController
@RequestMapping("/message")
public class IncomingMessageController {

    private static final Logger log = Logger.getLogger(IncomingMessageController.class.getName());

    @Autowired
    private ResultProcessingService resultProcessingService;

    @Autowired
    private GetStatisticsProcessingService getStatisticsProcessingService;

    @PostMapping
    /**
     * Handles the incoming POST API requests.
     */
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

        }catch (InvalidInputException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            log.severe("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the message.");
        }
    }

    /**
     * Method that invokes the processing of data received with a "result" message type.
     * @param resultMessage Data transfer object with obtained information.
     * @return Formatted response with general information on updated teams.
     */
    private String handleResultMessage(ResultMessage resultMessage) {
        log.info("Processing RESULT message");
        resultProcessingService.populateResultData(resultMessage);
        return resultProcessingService.customResultResponse(resultMessage);
    }

    /**
     * Method that invokes further processing related to "get_statistics" message type.
     * @param teamNames List of fetched teams' names.
     * @return Formatted response with information about last 3 matches played by requested teams.
     */
    private String handleGetStatisticsMessage(List<String> teamNames) {
        log.info("Processing GET_STATISTICS message");
        return getStatisticsProcessingService.customGetStatisticsResponse(teamNames);
    }

    /**
     * Checking incoming JSON for the "type" field presence.
     * @param jsonNode Object that contains the fetched JSON data.
     */
    private void validateInput(JsonNode jsonNode) {
        if (jsonNode == null || !jsonNode.has("type")) {
            throw new InvalidInputException("Invalid message: Missing 'type' field.");
        }
    }

    /**
     * Method that parses the data from the fetched JSON, provided by the "result" type of message.
     * Additionally, it checks for the incoming JSON data types compliance.
     * @param jsonNode Object that contains the fetched JSON data.
     * @return Data transfer object that contains the fetched data of type "result".
     */
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

    /**
     * Method that parses the data from the fetched JSON, provided by the "get_statistics" type of message.
     * @param jsonNode Object that contains the fetched JSON data.
     * @return List that contains all fetched teams' names.
     */
    private List<String> parseStatisticsTeams(JsonNode jsonNode) {
        JsonNode teamsNode = jsonNode.path("get_statistics").path("teams");
        if (!teamsNode.isArray()) {
            throw new InvalidInputException("'teams' must be an array.");
        }
        if (teamsNode.isEmpty()) {
            throw new InvalidInputException("Empty 'teams' array provided.");
        }

        List<String> teams = new ArrayList<>();
        for (JsonNode teamNode : teamsNode) {
            teams.add(teamNode.asText());
        }
        return teams;
    }


    /**
     * Handling custom exceptions related to input JSON.
     */
    public static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message) {
            super(message);
        }
    }
}