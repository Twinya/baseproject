package com.appengine.task.domain;

import com.appengine.common.domain.IdEntity;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "app_version", uniqueConstraints = {@UniqueConstraint(name = "app", columnNames = {"app", "platform"})})
public class AppVersion extends IdEntity {
    @Column(nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 0 COMMENT 'app名字'")
    public String app;
    @Column(nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 0 COMMENT 'app平台'")
    public String platform;
    @Column(nullable = false, columnDefinition = "VARCHAR(180) DEFAULT 0 COMMENT 'app下载地址'")
    public String url;
    @Column(nullable = false, columnDefinition = "int default 0 COMMENT '版本号'")
    public Integer version;
    @Column(name = "force_update", nullable = false, columnDefinition = "BOOLEAN default 0 COMMENT '是否强制更新'")
    public Boolean update = false;
    @Column(name = "create_time", nullable = false, columnDefinition = "bigint default 0 COMMENT '创建时间'")
    public Long createTime = 0L;
    @Column(nullable = false, name = "update_time", columnDefinition = "BIGINT DEFAULT 0 COMMENT '更新时间'")
    public Long updateTime = 0L;

    public AppVersion(String app, String platform, String url, Integer version, Boolean update) {
        this.app = app;
        this.platform = platform;
        this.url = url;
        this.version = version;
        this.update = update;
        this.createTime = System.currentTimeMillis();
        this.updateTime = createTime;
    }
}
