package com.appengine.oplog.domain;

import com.appengine.user.domain.IdUserNameEntity;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "operation_log", indexes = {@Index(name = "module", columnList = "module"), @Index(name = "uid", columnList = "uid"), @Index(name = "create_time", columnList = "create_time")})
public class Oplog extends IdUserNameEntity {
    @Column(name = "resource_id", nullable = false, columnDefinition = "bigint default 0 COMMENT '资源id'")
    public Long resourceId;
    @Column(name = "module", nullable = false, columnDefinition = "varchar(128) default 0 COMMENT '模块'")
    public String module;
    @Column(name = "after_update", nullable = false, columnDefinition = "text COMMENT '修改后'")
    public String after;
    @Column(name = "operation", nullable = false, columnDefinition = "varchar(128) default 0 COMMENT '操作'")
    public String operation;
    @Column(nullable = false, name = "create_time", columnDefinition = "BIGINT DEFAULT 0 COMMENT '创建时间'")
    private Long createTime;


    public Oplog(Long userId, String module, Long resourceId, String operation, String after) {
        this.userId = userId;
        this.module = module;
        this.resourceId = resourceId;
        this.operation = operation;
        this.after = after;
        this.createTime = System.currentTimeMillis();
    }
}
