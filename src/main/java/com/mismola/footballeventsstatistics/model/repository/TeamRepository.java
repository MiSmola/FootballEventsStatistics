package com.mismola.footballeventsstatistics.model.repository;

import com.mismola.footballeventsstatistics.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
