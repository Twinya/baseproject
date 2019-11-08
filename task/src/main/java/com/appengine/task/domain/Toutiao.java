package com.appengine.task.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name = "toutiao")
public class Toutiao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String content;

    public Toutiao(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Toutiao(String content) {
        this.content = content;
    }
}
