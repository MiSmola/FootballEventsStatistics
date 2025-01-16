package com.mismola.footballeventsstatistics.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.GetStatisticsMessage;
import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.ResultMessage;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/message")
public class IncomingMessageController {

    Logger log = Logger.getLogger(IncomingMessageController.class.getName());

    @PostMapping
    public String processMessage(@RequestBody JsonNode jsonNode) {

        String type = jsonNode.get("type").asText();

        if (Objects.equals(type, "RESULT")) {
            return handleResultMessage(new ResultMessage());
        } else if (Objects.equals(type, "GET_STATISTICS")) {
            return handleGetStatisticsMessage(new GetStatisticsMessage());
        } else {
            return "Unsupported message type: " + type;
        }
    }

    private String handleResultMessage(ResultMessage resultMessage) {
        //TODO: ResultMessage logic
        log.info("RESULT message detected");
        return "RESULT message detected";
    }

    private String handleGetStatisticsMessage(GetStatisticsMessage getStatisticsMessage) {
        //TODO: GetStatisticsMessage logic
        log.info("GET_STATISTICS message detected");
        return "GET_STATISTICS message detected";
    }
}
