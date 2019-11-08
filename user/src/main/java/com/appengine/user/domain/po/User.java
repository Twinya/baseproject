package com.appengine.user.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.appengine.common.entity.po.BasePo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "user", indexes = {@Index(name = "username", columnList = "username,regist_from"), @Index(name = "cci", columnList = "channel,create_time,ip"), @Index(name = "createTime", columnList = "create_time"), @Index(name = "cpj", columnList = "create_time,phone_os,jurisdiction")})
public class User extends BasePo {

    private static final long serialVersionUID = -5943828603966833654L;
    private int jurisdiction = 0;                //权限
    @JSONField(serialize = false)
    private int isShow;                      //是否显示
    @JSONField(serialize = false)
    private int isApply;                     //是否申请
    private int isLoan;                      //是否放款
    private String username;
    @JSONField(serialize = false)
    private String password;
    @JSONField(serialize = false)
    private String salt;
    @Column(name = "real_name")
    private String realName;
    private String mobile;
    private String sex;
    private String ip;
    @JSONField(name = "portrait")
    private String headurl;
    private String channel = "local";
    @Column(name = "regist_from")
    private String registFrom = "local";
    private BigDecimal balance = BigDecimal.ZERO;
    @Version
    private int version;
    @Column(name = "phone_os")
    private String phoneOs;

    @Transient
    private LoginRecord lastLoginTime;
    @Transient
    private Integer usableNum;

    public User(String username, String from, String password, String mobile, String channel, String ip) {
        this.ip = ip;
        this.registFrom = from;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.channel = channel;
    }

    public User(String username, String from, String realName, String password, String mobile, String channel, String ip) {
        this.ip = ip;
        this.registFrom = from;
        this.username = username;
        this.realName = realName;
        this.password = password;
        this.mobile = mobile;
        this.channel = channel;
    }

    public User(String username, String password, int jurisdiction, String realName, String registFrom) {
        this.username = username;
        this.password = password;
        this.jurisdiction = jurisdiction;
        this.realName = realName;
        this.registFrom = registFrom;
    }

    public BigDecimal charge(BigDecimal amount) {
        setBalance(this.balance.add(amount));
        return this.balance;
    }

    public BigDecimal reduce(BigDecimal amount) {
        setBalance(this.balance.subtract(amount));
        return this.balance;
    }

}