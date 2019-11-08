package com.appengine.user.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "regist_num_day")
public class RegisterNumByDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JSONField(serialize = false)
    private Long id;
    private int num;
    private int yyyyMMdd;

    public RegisterNumByDay(int yyyyMMdd, int num) {
        this.num = num;
        this.yyyyMMdd = yyyyMMdd;
    }
}
