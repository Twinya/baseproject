package com.appengine.user.domain.po;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "login_record",indexes = {@Index(name = "uid", columnList = "uid"),@Index(name = "create_time", columnList = "create_time"),@Index(name = "channel", columnList = "channel")})
public class LoginRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uid", nullable = false)
    private Long uid;
    private String ip;
    private String brand;
    private String phone_os;
    private String province;
    private String city;
    private String requestId;
    private String code;
    private String channel;
    private String district;
    private String street;
    private String streetNum;
    @Column(name = "first_login")
    private Boolean firstLogn = false;
    @Column(name = "create_time", nullable = false)
    private Long createTime;
    private String app;


}
