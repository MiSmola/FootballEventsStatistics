package com.mismola.footballeventsstatistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mismola.footballeventsstatistics.controller.api.IncomingMessageController;
import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.ResultMessage;
import com.mismola.footballeventsstatistics.service.GetStatisticsProcessingService;
import com.mismola.footballeventsstatistics.service.ResultProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncomingMessageController.class)
public class IncomingMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResultProcessingService resultProcessingService;

    @MockitoBean
    private GetStatisticsProcessingService getStatisticsProcessingService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    // Positive: Valid RESULT message
    @Test
    void testProcessMessage_validResultMessage() throws Exception {
        String validResultJson = """
        {
            "type": "RESULT",
            "result": {
                "home_team": "Team A",
                "away_team": "Team B",
                "home_score": 2,
                "away_score": 1
            }
        }
        """;

        Mockito.when(resultProcessingService.customResultResponse(any(ResultMessage.class)))
                .thenReturn("Result processed successfully");

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validResultJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Result processed successfully"));
    }

    // Positive: Valid GET_STATISTICS message
    @Test
    void testProcessMessage_validGetStatisticsMessage() throws Exception {
        String validStatisticsJson = """
        {
            "type": "GET_STATISTICS",
            "get_statistics": {
                "teams": ["Team A", "Team B"]
            }
        }
        """;

        Mockito.when(getStatisticsProcessingService.customGetStatisticsResponse(Mockito.anyList()))
                .thenReturn("Statistics processed successfully");

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validStatisticsJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Statistics processed successfully"));
    }

    // Negative: Missing 'type' field
    @Test
    void testProcessMessage_missingTypeField() throws Exception {
        String invalidJson = """
        {
            "result": {
                "home_team": "Team A",
                "away_team": "Team B",
                "home_score": 2,
                "away_score": 1
            }
        }
        """;

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid message: Missing 'type' field."));
    }

    // Negative: Invalid type
    @Test
    void testProcessMessage_invalidType() throws Exception {
        String invalidTypeJson = """
        {
            "type": "INVALID_TYPE"
        }
        """;

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTypeJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unsupported message type: INVALID_TYPE"));
    }

    // Negative: Non-integer scores in RESULT message
    @Test
    void testProcessMessage_nonIntegerScores() throws Exception {
        String invalidResultJson = """
        {
            "type": "RESULT",
            "result": {
                "home_team": "Team A",
                "away_team": "Team B",
                "home_score": "two",
                "away_score": 1
            }
        }
        """;

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidResultJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("'home_score' and 'away_score' must be integers."));
    }

    // Negative: Non-array team in GET_STATISTICS message
    @Test
    void testProcessMessage_nonArrayTeams() throws Exception {
        String invalidStatisticsJson = """
        {
            "type": "GET_STATISTICS",
            "get_statistics": {
                "teams": "Team A"
            }
        }
        """;

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidStatisticsJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("'teams' must be an array."));
    }

    // Negative: Empty teams array in GET_STATISTICS message
    @Test
    void testProcessMessage_emptyTeamsArray() throws Exception {
        String invalidStatisticsJson = """
        {
            "type": "GET_STATISTICS",
            "get_statistics": {
                "teams": []
            }
        }
        """;

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidStatisticsJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Empty 'teams' array provided."));
    }

    // Negative: Malformed JSON
    @Test
    void testProcessMessage_malformedJson() throws Exception {
        String malformedJson = "{ invalid }";

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Malformed JSON request")));
    }
}
