package com.appengine.im.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.appengine.common.entity.po.BasePo;
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
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(name = "username",columnNames = {"username"})},indexes = { @Index(name = "createTime", columnList = "create_time")})
public class User extends BasePo {

    private static final long serialVersionUID = -5943828603966833654L;
    private String username;
    @JSONField(serialize = false)
    private String password;
    @Column(name = "nick_name")
    private String nickName;
    private String avatar;
    private String gender;
    private String signature;
    private String region;
    private String address;
    private String ip;

}