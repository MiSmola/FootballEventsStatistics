package com.mismola.footballeventsstatistics.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "team_results_registry")
@SequenceGenerator(name = "seq_team_results_registry", sequenceName = "seq_team_results_registry", allocationSize = 1)
public class TeamResultsRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_team_results_registry")
    private Integer id;

    @Column(name = "match_date_time")
    private Date matchDateTime;

    @Column(name = "points_gained_home")
    private Integer pointsGainedHome;

    @Column(name = "points_gained_away")
    private Integer pointsGainedAway;

    @Column(name = "goals_scored_home")
    private Integer goalsScoredHome;

    @Column(name = "goals_scored_away")
    private Integer goalsScoredAway;

    @Column(name = "wld_home_team")
    @Enumerated(EnumType.STRING)
    private WDLResult wdlHomeTeam;

    @Column(name = "wld_away_team")
    @Enumerated(EnumType.STRING)
    private WDLResult wdlAwayTeam;

    // Many-to-One relationship with Team (home team)
    @ManyToOne
    @JoinColumn(name = "home_team_id", referencedColumnName = "id")
    private Team homeTeam;

    // Many-to-One relationship with Team (away team)
    @ManyToOne
    @JoinColumn(name = "away_team_id", referencedColumnName = "id")
    private Team awayTeam;
}