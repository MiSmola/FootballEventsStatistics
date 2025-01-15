package com.mismola.footballeventsstatistics.model.entity;

import java.util.Date;

public class TeamResultsRegistry {
    private Integer id;
    private String homeTeam;
    private String awayTeam;
    private Date matchDateTime;
    private Integer pointsGainedHome;
    private Integer pointsGainedAway;
    private Integer goalsScoredHome;
    private Integer goalsScoredAway;
    private WDLResult wdlHomeTeam;
    private WDLResult wdlAwayTeam;
}
