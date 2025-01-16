package com.mismola.footballeventsstatistics.services;

import com.mismola.footballeventsstatistics.controller.api.dto.incomingmessage.ResultMessage;
import com.mismola.footballeventsstatistics.model.dto.RecentGeneralTeamStatsDto;
import com.mismola.footballeventsstatistics.model.entity.GeneralTeamStats;
import com.mismola.footballeventsstatistics.model.entity.Team;
import com.mismola.footballeventsstatistics.model.repository.GeneralTeamStatsRepository;
import com.mismola.footballeventsstatistics.model.repository.TeamRepository;
import com.mismola.footballeventsstatistics.model.repository.TeamResultsRegistryRepository;
import com.mismola.footballeventsstatistics.model.service.GeneralTeamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultProcessingService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GeneralTeamStatsRepository generalTeamStatsRepository;

    @Autowired
    private TeamResultsRegistryRepository teamResultsRegistryRepository;

    private final GeneralTeamStatsService generalTeamStatsService;

    public void populateResultData(ResultMessage resultMessage) {
        // Check or create the home team
        Team homeTeam = teamRepository.findByTeamName(resultMessage.getHomeTeam())
                .orElseGet(() -> {
                    Team newHomeTeam = new Team();
                    newHomeTeam.setTeamName(resultMessage.getHomeTeam());
                    return newHomeTeam;
                });

        // Check or create the away team
        Team awayTeam = teamRepository.findByTeamName(resultMessage.getAwayTeam())
                .orElseGet(() -> {
                    Team newAwayTeam = new Team();
                    newAwayTeam.setTeamName(resultMessage.getAwayTeam());
                    return newAwayTeam;
                });

        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);

        //TODO: Transfer to some static utils methods
        Integer homeTeamPointsEarned;
        Integer awayTeamPointsEarned;
        if(resultMessage.getHomeScore() > resultMessage.getAwayScore()) {
            homeTeamPointsEarned = 3;
            awayTeamPointsEarned = 0;
        } else if(resultMessage.getHomeScore() < resultMessage.getAwayScore()) {
            homeTeamPointsEarned = 0;
            awayTeamPointsEarned = 3;
        } else {
            homeTeamPointsEarned = 1;
            awayTeamPointsEarned = 1;
        }

        GeneralTeamStats statsHome = generalTeamStatsRepository.findByTeam(homeTeam)
                .orElseGet(() -> {
                    GeneralTeamStats newStats = new GeneralTeamStats();
                    newStats.setTeam(homeTeam); // Link to the team
                    return newStats;
                });

        RecentGeneralTeamStatsDto homeTeamStatsDto = RecentGeneralTeamStatsDto.builder()
                .sumOfPlayedEvents(1)
                .sumOfPoints(homeTeamPointsEarned)
                .sumOfGoalsScored(resultMessage.getHomeScore())
                .sumOfGoalsConceded(resultMessage.getAwayScore())
                .build();

        // Update stats with additional data
        statsHome.setSumOfPlayedEvents(
                (statsHome.getSumOfPlayedEvents() != null ? statsHome.getSumOfPlayedEvents() : 0)
                        + (homeTeamStatsDto.getSumOfPlayedEvents() != null ? homeTeamStatsDto.getSumOfPlayedEvents() : 0)
        );
        statsHome.setSumOfPoints(
                (statsHome.getSumOfPoints() != null ? statsHome.getSumOfPoints() : 0)
                        + (homeTeamStatsDto.getSumOfPoints() != null ? homeTeamStatsDto.getSumOfPoints() : 0)
        );
        statsHome.setSumOfGoalsScored(
                (statsHome.getSumOfGoalsScored() != null ? statsHome.getSumOfGoalsScored() : 0)
                        + (homeTeamStatsDto.getSumOfGoalsScored() != null ? homeTeamStatsDto.getSumOfGoalsScored() : 0)
        );
        statsHome.setSumOfGoalsConceded(
                (statsHome.getSumOfGoalsConceded() != null ? statsHome.getSumOfGoalsConceded() : 0)
                        + (homeTeamStatsDto.getSumOfGoalsConceded() != null ? homeTeamStatsDto.getSumOfGoalsConceded() : 0)
        );

        // Save and return the updated stats



        GeneralTeamStats statsAway = generalTeamStatsRepository.findByTeam(awayTeam)
                .orElseGet(() -> {
                    GeneralTeamStats newStats = new GeneralTeamStats();
                    newStats.setTeam(awayTeam); // Link to the team
                    return newStats;
                });

        RecentGeneralTeamStatsDto awayTeamStatsDto = RecentGeneralTeamStatsDto.builder()
                .sumOfPlayedEvents(1)
                .sumOfPoints(awayTeamPointsEarned)
                .sumOfGoalsScored(resultMessage.getAwayScore())
                .sumOfGoalsConceded(resultMessage.getHomeScore())
                .build();

        // Update stats with additional data
        statsAway.setSumOfPlayedEvents(
                (statsAway.getSumOfPlayedEvents() != null ? statsAway.getSumOfPlayedEvents() : 0)
                        + (awayTeamStatsDto.getSumOfPlayedEvents() != null ? awayTeamStatsDto.getSumOfPlayedEvents() : 0)
        );
        statsAway.setSumOfPoints(
                (statsAway.getSumOfPoints() != null ? statsAway.getSumOfPoints() : 0)
                        + (awayTeamStatsDto.getSumOfPoints() != null ? awayTeamStatsDto.getSumOfPoints() : 0)
        );
        statsAway.setSumOfGoalsScored(
                (statsAway.getSumOfGoalsScored() != null ? statsAway.getSumOfGoalsScored() : 0)
                        + (awayTeamStatsDto.getSumOfGoalsScored() != null ? awayTeamStatsDto.getSumOfGoalsScored() : 0)
        );
        statsAway.setSumOfGoalsConceded(
                (statsAway.getSumOfGoalsConceded() != null ? statsAway.getSumOfGoalsConceded() : 0)
                        + (awayTeamStatsDto.getSumOfGoalsConceded() != null ? awayTeamStatsDto.getSumOfGoalsConceded() : 0)
        );

        // Save and return the updated stats
        generalTeamStatsRepository.save(statsHome);
        generalTeamStatsRepository.save(statsAway);




//        // Create GeneralTeamStats
//        RecentGeneralTeamStatsDto homeTeamStatsDto = RecentGeneralTeamStatsDto.builder()
//                .sumOfPlayedEvents(1)
//                .sumOfPoints(homeTeamPointsEarned)
//                .sumOfGoalsScored(resultMessage.getHomeScore())
//                .sumOfGoalsConceded(resultMessage.getAwayScore())
//                .build();
//        GeneralTeamStats updatedHomeTeamStats =
//                generalTeamStatsService.downloadAndUpdateStats(resultMessage.getHomeTeam(), homeTeamStatsDto);
//
//        RecentGeneralTeamStatsDto awayTeamStatsDto = RecentGeneralTeamStatsDto.builder()
//                .sumOfPlayedEvents(1)
//                .sumOfPoints(awayTeamPointsEarned)
//                .sumOfGoalsScored(resultMessage.getAwayScore())
//                .sumOfGoalsConceded(resultMessage.getHomeScore())
//                .build();
//        GeneralTeamStats updatedAwayTeamStats =
//                generalTeamStatsService.downloadAndUpdateStats(resultMessage.getAwayTeam(), awayTeamStatsDto);



//        stats.setSumOfPlayedEvents(resultMessage.getSumOfPlayedEvents());
//        stats.setSumOfPoints(resultMessage.getSumOfPoints());
//        stats.setSumOfGoalsScored(resultMessage.getSumOfGoalsScored());
//        stats.setSumOfGoalsConceded(resultMessage.getSumOfGoalsConceded());
//        stats.setTeam(team);
//
//        team.setGeneralTeamStats(stats);
//
//        // Add TeamResultsRegistry for home matches
//        List<TeamResultsRegistry> homeMatches = resultMessage.getHomeMatches().stream().map(match -> {
//            TeamResultsRegistry registry = new TeamResultsRegistry();
//            registry.setMatchDateTime(match.getMatchDateTime());
//            registry.setPointsGainedHome(match.getPointsGainedHome());
//            registry.setGoalsScoredHome(match.getGoalsScoredHome());
//            registry.setWdlHomeTeam(match.getWdlHomeTeam());
//            registry.setHomeTeam(team);
//            return registry;
//        }).collect(Collectors.toList());
//
//        team.setHomeMatches(homeMatches);
//
//        // Add TeamResultsRegistry for away matches
//        List<TeamResultsRegistry> awayMatches = resultMessage.getAwayMatches().stream().map(match -> {
//            TeamResultsRegistry registry = new TeamResultsRegistry();
//            registry.setMatchDateTime(match.getMatchDateTime());
//            registry.setPointsGainedAway(match.getPointsGainedAway());
//            registry.setGoalsScoredAway(match.getGoalsScoredAway());
//            registry.setWdlAwayTeam(match.getWdlAwayTeam());
//            registry.setAwayTeam(team);
//            return registry;
//        }).collect(Collectors.toList());
//
//        team.setAwayMatches(awayMatches);
//
//        // Save everything

    }
}
