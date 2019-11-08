package com.appengine.user.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author fuyou
 * @Date 2019/7/15 17:59
 */
@Getter
@Setter
@MappedSuperclass
public abstract class IdUserNameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "uid", nullable = false, columnDefinition = "bigint default 0 COMMENT '用户id'")
    public Long userId;
    @Transient
    public String userName;
}
