package com.mismola.footballeventsstatistics.service;

import com.mismola.footballeventsstatistics.controller.api.dto.response.GetStatisticsResponseDto;
import com.mismola.footballeventsstatistics.model.entity.Team;
import com.mismola.footballeventsstatistics.model.entity.TeamResultsRegistry;
import com.mismola.footballeventsstatistics.model.repository.TeamRepository;
import com.mismola.footballeventsstatistics.model.repository.TeamResultsRegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class related to handling operations related to "get_statistics" message type.
 */
@Service
public class GetStatisticsProcessingService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamResultsRegistryRepository teamResultsRegistryRepository;

    /**
     * Retrieving and processing information about last 3 matches for requested teams.
     * @param fetchedTeams List of teams that the statistics are requested for.
     * @return Formatted statistics response message.
     */
    public String customGetStatisticsResponse(List<String> fetchedTeams) {
        List<Team> teamsToSearch = new ArrayList<>();
        List<String> incorrectTeamEntries = new ArrayList<>();
        for (String teamName : fetchedTeams) {
            teamRepository.findByTeamName(teamName).ifPresentOrElse(
                    teamsToSearch::add,
                    () -> incorrectTeamEntries.add(teamName)
            );
        }
        StringBuilder response = new StringBuilder();
        if (!teamsToSearch.isEmpty()) {
            Map<String, List<TeamResultsRegistry>> teamResultsRegistries = new HashMap<>();
            //Retrieving last 3 match statistics for requested teams.
            for (Team team : teamsToSearch) {
                teamResultsRegistries.put(team.getTeamName(), teamResultsRegistryRepository.findTop3ByTeamOrderByMatchDateTimeDesc(team));
            }
            List<String> requestedTeamStatistics = new ArrayList<>();

            for (Map.Entry<String, List<TeamResultsRegistry>> entry : teamResultsRegistries.entrySet()) {
                String key = entry.getKey();
                List<TeamResultsRegistry> registries = entry.getValue();

                StringBuilder tmpWDL = new StringBuilder();
                Integer tmpPointsGained = 0;
                Integer tmpGoalsScored = 0;
                Integer tmpGoalsConceded = 0;

                //Aggregation of results for last 3 matches per team
                for (TeamResultsRegistry registry : registries) {
                    tmpWDL.append(registry.getWdlResult());
                    tmpPointsGained = tmpPointsGained + registry.getPointsGained();
                    tmpGoalsScored = tmpGoalsScored + registry.getGoalsScored();
                    tmpGoalsConceded = tmpGoalsConceded + registry.getGoalsConceded();
                }

                //Combining the processed values to a DTO object per team.
                GetStatisticsResponseDto getStatisticsResponseDto = GetStatisticsResponseDto.builder()
                        .teamName(key)
                        .WDL(tmpWDL.toString())
                        .average(Math.round((((float) tmpGoalsScored + (float) tmpGoalsConceded) / registries.size()) * 100) / 100.0f)
                        .eventsPlayed(registries.size())
                        .pointsGained(tmpPointsGained)
                        .goalsScoredSum(tmpGoalsScored)
                        .goalsConcededSum(tmpGoalsConceded).build();
                requestedTeamStatistics.add(getStatisticsResponseDto.toString());
                tmpPointsGained = 0;
                tmpGoalsScored = 0;
                tmpGoalsConceded = 0;
                tmpWDL.setLength(0);
            }
            //Combining the response message.
            for (int i = requestedTeamStatistics.size() - 1; i >= 0; i--) {
                String result = requestedTeamStatistics.get(i);
                response.append(result);
            }
        }
        //Additional information about incorreclty provided or non-existing teams.
        if (!incorrectTeamEntries.isEmpty()) {
            response.append("Requested incorrect or non-existing teams: " + incorrectTeamEntries);
        }
        return response.toString();
    }
}
