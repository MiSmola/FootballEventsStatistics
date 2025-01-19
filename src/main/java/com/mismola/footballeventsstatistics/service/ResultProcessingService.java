package com.mismola.footballeventsstatistics.service;

import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.ResultMessage;
import com.mismola.footballeventsstatistics.model.dto.RecentGeneralTeamStatsDto;
import com.mismola.footballeventsstatistics.model.entity.GeneralTeamStats;
import com.mismola.footballeventsstatistics.model.entity.Team;
import com.mismola.footballeventsstatistics.model.entity.TeamResultsRegistry;
import com.mismola.footballeventsstatistics.model.repository.GeneralTeamStatsRepository;
import com.mismola.footballeventsstatistics.model.repository.TeamRepository;
import com.mismola.footballeventsstatistics.model.repository.TeamResultsRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service class related to handling operations related to "result" message type.
 */
@Service
@RequiredArgsConstructor
public class ResultProcessingService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GeneralTeamStatsRepository generalTeamStatsRepository;

    @Autowired
    private TeamResultsRegistryRepository teamResultsRegistryRepository;

    /**
     * Combining and populating the DB with data based on the information provided in "result" message type.
     * @param resultMessage Data retrieved form JSON.
     */
    public void populateResultData(ResultMessage resultMessage) {
        // Checking for presence in DB or creating the new home and away teams
        Team homeTeam = teamRepository.findByTeamName(resultMessage.getHomeTeam())
                .orElseGet(() -> {
                    Team newHomeTeam = new Team();
                    newHomeTeam.setTeamName(resultMessage.getHomeTeam());
                    return newHomeTeam;
                });

        Team awayTeam = teamRepository.findByTeamName(resultMessage.getAwayTeam())
                .orElseGet(() -> {
                    Team newAwayTeam = new Team();
                    newAwayTeam.setTeamName(resultMessage.getAwayTeam());
                    return newAwayTeam;
                });

        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);

        //Retrieving or creating new entry for General Team Statistics
        GeneralTeamStats statsHome = generalTeamStatsRepository.findByTeam(homeTeam)
                .orElseGet(() -> {
                    GeneralTeamStats newStats = new GeneralTeamStats();
                    newStats.setTeam(homeTeam);
                    return newStats;
                });

        RecentGeneralTeamStatsDto homeTeamStatsDto = RecentGeneralTeamStatsDto.builder()
                .sumOfPlayedEvents(1)
                .sumOfPoints(Utils.calculateEarnedPoints(resultMessage).getFirst())
                .sumOfGoalsScored(resultMessage.getHomeScore())
                .sumOfGoalsConceded(resultMessage.getAwayScore())
                .build();

        // Updating stats with new data
        createOrUpdateGeneralStats(statsHome, homeTeamStatsDto);

        //Retrieving or creating new entry for General Team Statistics
        GeneralTeamStats statsAway = generalTeamStatsRepository.findByTeam(awayTeam)
                .orElseGet(() -> {
                    GeneralTeamStats newStats = new GeneralTeamStats();
                    newStats.setTeam(awayTeam);
                    return newStats;
                });

        RecentGeneralTeamStatsDto awayTeamStatsDto = RecentGeneralTeamStatsDto.builder()
                .sumOfPlayedEvents(1)
                .sumOfPoints(Utils.calculateEarnedPoints(resultMessage).getSecond())
                .sumOfGoalsScored(resultMessage.getAwayScore())
                .sumOfGoalsConceded(resultMessage.getHomeScore())
                .build();

        // Updating stats with new data
        createOrUpdateGeneralStats(statsAway, awayTeamStatsDto);

        generalTeamStatsRepository.save(statsHome);
        generalTeamStatsRepository.save(statsAway);

        //In the registry of individual results, both teams participating in a match
        //are provided with the same date and time of entry, in order to allow identifying them
        //and sorting for the purposes of retrieving last 3 matches information in 'GetStatisticsProcessingService'.
        Date matchEntryDate = new Date();

        //Supplementing and combining data for the MatchRegistry
        TeamResultsRegistry homeTeamRegistryEntry = TeamResultsRegistry.builder()
                .matchDateTime(matchEntryDate)
                .wdlResult(Utils.defineWDLStatusByScores(resultMessage).getFirst())
                .goalsScored(resultMessage.getHomeScore())
                .goalsConceded(resultMessage.getAwayScore())
                .pointsGained(Utils.calculateEarnedPoints(resultMessage).getFirst())
                .team(homeTeam).build();

        TeamResultsRegistry awayTeamRegistryEntry = TeamResultsRegistry.builder()
                .matchDateTime(matchEntryDate)
                .wdlResult(Utils.defineWDLStatusByScores(resultMessage).getSecond())
                .goalsScored(resultMessage.getAwayScore())
                .goalsConceded(resultMessage.getHomeScore())
                .pointsGained(Utils.calculateEarnedPoints(resultMessage).getSecond())
                .team(awayTeam).build();

        teamResultsRegistryRepository.save(homeTeamRegistryEntry);
        teamResultsRegistryRepository.save(awayTeamRegistryEntry);

    }

    /**
     * Creating or updating the table representing the Teams' General Statistics.
     * @param teamStatistics Information from DB.
     * @param teamStatsDto Fresh data that needs to be added to the DB.
     */
    private void createOrUpdateGeneralStats(GeneralTeamStats teamStatistics, RecentGeneralTeamStatsDto teamStatsDto) {
        teamStatistics.setSumOfPlayedEvents(
                (teamStatistics.getSumOfPlayedEvents() != null ? teamStatistics.getSumOfPlayedEvents() : 0)
                        + (teamStatsDto.getSumOfPlayedEvents() != null ? teamStatsDto.getSumOfPlayedEvents() : 0)
        );
        teamStatistics.setSumOfPoints(
                (teamStatistics.getSumOfPoints() != null ? teamStatistics.getSumOfPoints() : 0)
                        + (teamStatsDto.getSumOfPoints() != null ? teamStatsDto.getSumOfPoints() : 0)
        );
        teamStatistics.setSumOfGoalsScored(
                (teamStatistics.getSumOfGoalsScored() != null ? teamStatistics.getSumOfGoalsScored() : 0)
                        + (teamStatsDto.getSumOfGoalsScored() != null ? teamStatsDto.getSumOfGoalsScored() : 0)
        );
        teamStatistics.setSumOfGoalsConceded(
                (teamStatistics.getSumOfGoalsConceded() != null ? teamStatistics.getSumOfGoalsConceded() : 0)
                        + (teamStatsDto.getSumOfGoalsConceded() != null ? teamStatsDto.getSumOfGoalsConceded() : 0)
        );
    }

    /**
     * Constructing the response for "result" message type request.
     * @param resultMessage Information fetched and processed from the request JSON.
     * @return Formatted response text.
     */
    public String customResultResponse(ResultMessage resultMessage) {
        Team homeTeam = teamRepository.findByTeamName(resultMessage.getHomeTeam())
                .orElseThrow(() -> new RuntimeException("Not found team " + resultMessage.getHomeTeam()));
        Team awayTeam = teamRepository.findByTeamName(resultMessage.getAwayTeam())
                .orElseThrow(() -> new RuntimeException("Not found team " + resultMessage.getAwayTeam()));
        GeneralTeamStats homeTeamGeneralStats = generalTeamStatsRepository.findByTeam(homeTeam)
                .orElseThrow(() -> new RuntimeException("No Stats entry found for " + resultMessage.getHomeTeam()));
        GeneralTeamStats awayTeamGeneralStats = generalTeamStatsRepository.findByTeam(awayTeam)
                .orElseThrow(() -> new RuntimeException("No Stats entry found for " + resultMessage.getHomeTeam()));
        return resultMessage.getHomeTeam() + " " +
                homeTeamGeneralStats.getSumOfPlayedEvents() + " " +
                homeTeamGeneralStats.getSumOfPoints() + " " +
                homeTeamGeneralStats.getSumOfGoalsScored() + " " +
                homeTeamGeneralStats.getSumOfGoalsConceded() + "\n" +
                resultMessage.getAwayTeam() + " " +
                awayTeamGeneralStats.getSumOfPlayedEvents() + " " +
                awayTeamGeneralStats.getSumOfPoints() + " " +
                awayTeamGeneralStats.getSumOfGoalsScored() + " " +
                awayTeamGeneralStats.getSumOfGoalsConceded() + "\n";
    }
}
