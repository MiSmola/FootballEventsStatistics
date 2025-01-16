package com.mismola.footballeventsstatistics.model.service;

import com.mismola.footballeventsstatistics.model.dto.RecentGeneralTeamStatsDto;
import com.mismola.footballeventsstatistics.model.entity.GeneralTeamStats;
import com.mismola.footballeventsstatistics.model.entity.Team;
import com.mismola.footballeventsstatistics.model.repository.GeneralTeamStatsRepository;
import com.mismola.footballeventsstatistics.model.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeneralTeamStatsService {


    private final GeneralTeamStatsRepository generalTeamStatsRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public GeneralTeamStatsService(GeneralTeamStatsRepository generalTeamStatsRepository, TeamRepository teamRepository) {
        this.generalTeamStatsRepository = generalTeamStatsRepository;
        this.teamRepository = teamRepository;
    }

//    @Transactional
    public GeneralTeamStats downloadAndUpdateStats(String teamName, RecentGeneralTeamStatsDto additionalData) {
        // Retrieve the Team entity
        Team team = teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> new EntityNotFoundException("Team not found: " + teamName));

        // Fetch existing GeneralTeamStats or create a new one
        GeneralTeamStats stats = generalTeamStatsRepository.findByTeam(team)
                .orElseGet(() -> {
                    GeneralTeamStats newStats = new GeneralTeamStats();
                    newStats.setTeam(team); // Link to the team
                    return newStats;
                });

        // Update stats with additional data
        stats.setSumOfPlayedEvents(
                (stats.getSumOfPlayedEvents() != null ? stats.getSumOfPlayedEvents() : 0)
                        + (additionalData.getSumOfPlayedEvents() != null ? additionalData.getSumOfPlayedEvents() : 0)
        );
        stats.setSumOfPoints(
                (stats.getSumOfPoints() != null ? stats.getSumOfPoints() : 0)
                        + (additionalData.getSumOfPoints() != null ? additionalData.getSumOfPoints() : 0)
        );
        stats.setSumOfGoalsScored(
                (stats.getSumOfGoalsScored() != null ? stats.getSumOfGoalsScored() : 0)
                        + (additionalData.getSumOfGoalsScored() != null ? additionalData.getSumOfGoalsScored() : 0)
        );
        stats.setSumOfGoalsConceded(
                (stats.getSumOfGoalsConceded() != null ? stats.getSumOfGoalsConceded() : 0)
                        + (additionalData.getSumOfGoalsConceded() != null ? additionalData.getSumOfGoalsConceded() : 0)
        );

        // Save and return the updated stats
        return generalTeamStatsRepository.save(stats);
    }
}
