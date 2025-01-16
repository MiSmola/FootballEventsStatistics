package com.mismola.footballeventsstatistics.model.repository;

import com.mismola.footballeventsstatistics.model.entity.TeamResultsRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamResultsRegistryRepository extends JpaRepository<TeamResultsRegistry, Integer> {
}
