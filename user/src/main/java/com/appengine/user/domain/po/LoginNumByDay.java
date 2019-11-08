package com.appengine.user.domain.po;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "login_num_day")
public class LoginNumByDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String channel;
    public Integer yyyyMMdd;
    public Integer num;

    public LoginNumByDay(String channel, int yyyyMMdd, int num) {
        this.channel = channel;
        this.yyyyMMdd = yyyyMMdd;
        this.num = num;
    }
}
