package com.nuce.duantp.sunshine.config.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conf_schedules")
public class ConfigSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "bean")
    private String bean;

    @Column(name = "description")
    private String description;

    @Column(name = "cron")
    private String cron;

    @Column(name = "active")
    private boolean active;
}
