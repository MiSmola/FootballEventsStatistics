package com.mismola.footballeventsstatistics.model.repository;

import com.mismola.footballeventsstatistics.model.entity.Team;
import com.mismola.footballeventsstatistics.model.entity.TeamResultsRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamResultsRegistryRepository extends JpaRepository<TeamResultsRegistry, Integer> {
    TeamResultsRegistry findByTeam(Team team);
    List<TeamResultsRegistry> findTop3ByTeamOrderByMatchDateTimeDesc(Team team);
}
