package com.mismola.footballeventsstatistics.service;

import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.ResultMessage;
import com.mismola.footballeventsstatistics.model.entity.WDLResult;
import org.springframework.data.util.Pair;

public class Utils {

    public static Pair<Integer, Integer> calculateEarnedPoints(ResultMessage resultMessage) {
        int homeTeamPointsEarned;
        int awayTeamPointsEarned;

        if (resultMessage.getHomeScore() > resultMessage.getAwayScore()) {
            homeTeamPointsEarned = 3;
            awayTeamPointsEarned = 0;
        } else if (resultMessage.getHomeScore() < resultMessage.getAwayScore()) {
            homeTeamPointsEarned = 0;
            awayTeamPointsEarned = 3;
        } else {
            homeTeamPointsEarned = 1;
            awayTeamPointsEarned = 1;
        }

        return Pair.of(homeTeamPointsEarned, awayTeamPointsEarned);
    }

    public static Pair<WDLResult, WDLResult> defineWDLStatusByScores(ResultMessage resultMessage) {
        WDLResult homeTeamRegistryWDL;
        WDLResult awayTeamRegistryWDL;
        if (resultMessage.getHomeScore() > resultMessage.getAwayScore()) {
            homeTeamRegistryWDL = WDLResult.W;
            awayTeamRegistryWDL = WDLResult.L;
        } else if (resultMessage.getHomeScore() < resultMessage.getAwayScore()) {
            homeTeamRegistryWDL = WDLResult.L;
            awayTeamRegistryWDL = WDLResult.W;
        } else {
            homeTeamRegistryWDL = WDLResult.D;
            awayTeamRegistryWDL = WDLResult.D;
        }

        return Pair.of(homeTeamRegistryWDL, awayTeamRegistryWDL);
    }
}
