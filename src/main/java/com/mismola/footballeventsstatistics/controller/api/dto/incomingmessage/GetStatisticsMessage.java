package com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage;

import java.util.List;

public class GetStatisticsMessage {
    private String type;
    private StatisticsData get_statistics;

    // Getters i Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StatisticsData getGet_statistics() {
        return get_statistics;
    }

    public void setGet_statistics(StatisticsData get_statistics) {
        this.get_statistics = get_statistics;
    }
}

class StatisticsData {
    private List<String> teams;

    // Getters i Setters
    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }
}
