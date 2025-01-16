package com.mismola.footballeventsstatistics.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "general_team_stats")
@SequenceGenerator(name = "seq_general_team_stats", sequenceName = "seq_general_team_stats", allocationSize = 1)
public class GeneralTeamStats {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_general_team_stats")
    private Integer id;

    @Column(name = "sum_of_played_events")
    private Integer sumOfPlayedEvents;

    @Column(name = "sum_of_points")
    private Integer sumOfPoints;

    @Column(name = "sum_of_goals_scored")
    private Integer sumOfGoalsScored;

    @Column(name = "sum_of_goals_conceded")
    private Integer sumOfGoalsConceded;

    // One-to-One relationship with Team
    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
}