package com.appengine.task.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "app_config")
public class AppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Integer delay;
    public String app;

    public AppConfig(String app, Integer delay) {
        this.app = app;
        this.delay = delay;
    }
}
