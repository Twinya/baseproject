package com.appengine.task.domain;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name = "phone_area")
public class PhoneArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String phone;
    public String prefix;
    public String province;
    public String city;
    public String isp;
    public String post_code;
    public String city_code;
    public String area_code;
    public String types;

}
