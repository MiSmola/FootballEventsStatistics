package com.mismola.footballeventsstatistics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="teams")
public class Teams {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="team_name")
    private String teamName;
}
