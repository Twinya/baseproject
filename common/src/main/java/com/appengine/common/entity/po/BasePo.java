package com.appengine.common.entity.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@MappedSuperclass
public class BasePo implements Serializable {
    private static final long serialVersionUID = -1298214506075329663L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, name = "create_time", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private Date createdTime = Date.from(ZonedDateTime.now().toInstant());
    @Column(nullable = false, name = "update_time", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
    private Date updatedTime = Date.from(ZonedDateTime.now().toInstant());
}
