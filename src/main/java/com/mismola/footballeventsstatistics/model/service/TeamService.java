package com.mismola.footballeventsstatistics.model.service;

import com.mismola.footballeventsstatistics.model.entity.Team;
import com.mismola.footballeventsstatistics.model.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team saveTeam(Team team){
        return teamRepository.save(team);
    }
}
