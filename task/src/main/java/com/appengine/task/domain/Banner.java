package com.appengine.task.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String title;
    public String image;
    public String url;
    @Column(name = "product_id")
    public Long productId = 0L;
    public String channel;
    public String app;

    public Banner(Long id, String title, String image, String url, Long productId, String channel, String app) {
        this.id = id;
        this.app = app;
        this.title = title;
        this.image = image;
        this.url = url;
        this.productId = productId;
        this.channel = channel;
    }
}
