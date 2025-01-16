package com.mismola.footballeventsstatistics.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="team")
@SequenceGenerator(name = "seq_team", sequenceName = "seq_team", allocationSize = 1)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_team")
    private Integer id;

    @Column(name = "team_name")
    @JsonProperty("team_name")
    private String teamName;

    // One-to-One relationship with GeneralTeamStats
    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    private GeneralTeamStats generalTeamStats;

    // One-to-Many relationship with TeamResultsRegistry
    @OneToMany(mappedBy = "homeTeam", cascade = CascadeType.ALL)
    private List<TeamResultsRegistry> homeMatches;

    @OneToMany(mappedBy = "awayTeam", cascade = CascadeType.ALL)
    private List<TeamResultsRegistry> awayMatches;
}