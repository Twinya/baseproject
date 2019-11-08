package com.appengine.task.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name = "home_ad", indexes = {@Index(name = "app", columnList = "app")})
public class HomeAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String image;
    public String url;
    public String app;
    public String name;
    @Column(name = "product_id")
    public Long productId = 0L;
    public String channel;
    public Boolean isShow = false;

    public HomeAd(Long id, String app, String image, String url, Long productId, String name, String channel, boolean isShow) {
        this.isShow = isShow;
        this.id = id;
        this.productId = productId;
        this.channel = channel;
        this.image = image;
        this.name = name;
        this.app = app;
        this.url = url;
    }

    public HomeAd(String app, String image, String url, Long productId, String name, String channel, boolean isShow) {
        this.isShow = isShow;
        this.image = image;
        this.app = app;
        this.name = name;
        this.url = url;
        this.productId = productId;
        this.channel = channel;
    }


}
