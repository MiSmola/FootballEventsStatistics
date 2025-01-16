package com.mismola.footballeventsstatistics.model.repository;

import com.mismola.footballeventsstatistics.model.entity.GeneralTeamStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralTeamStatsRepository extends JpaRepository<GeneralTeamStats, Integer> {
}
