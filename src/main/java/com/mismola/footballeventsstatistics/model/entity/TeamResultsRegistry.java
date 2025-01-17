package com.mismola.footballeventsstatistics.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "team_results_registry")
@SequenceGenerator(name = "seq_team_results_registry", sequenceName = "seq_team_results_registry", allocationSize = 1)
public class TeamResultsRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_team_results_registry")
    private Integer id;

    @Column(name = "match_date_time")
    private Date matchDateTime;

    @Column(name = "wld_result")
    @Enumerated(EnumType.STRING)
    private WDLResult wdlResult;

    @Column(name = "goals_scored")
    private Integer goalsScored;

    @Column(name = "goals_conceded")
    private Integer goalsConceded;

    @Column(name = "points_gained")
    private Integer pointsGained;

    // Many-to-One relationship with Team (home team)
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

}