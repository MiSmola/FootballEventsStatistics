package com.mismola.footballeventsstatistics.model.repository;

import com.mismola.footballeventsstatistics.model.entity.GeneralTeamStats;
import com.mismola.footballeventsstatistics.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GeneralTeamStatsRepository extends JpaRepository<GeneralTeamStats, Integer> {
    // Find by Team entity
    Optional<GeneralTeamStats> findByTeam(Team team);
//    GeneralTeamStats findByTeamId(Integer teamId);

}
