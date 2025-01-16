package com.mismola.footballeventsstatistics.controller.api;

import com.mismola.footballeventsstatistics.model.entity.Team;
import com.mismola.footballeventsstatistics.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    //adding a new team
    @PostMapping
    public ResponseEntity<Team> saveTeam(@RequestBody Team team) {
        Team newTeam = teamService.saveTeam(team);
        return ResponseEntity.ok(newTeam);
    }
}
