package com.appengine.user.domain.po;

import com.appengine.common.entity.po.BasePo;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@ToString
@Table(name = "feedback")
public class FeedBackRecord extends BasePo {
    private static final long serialVersionUID = -1820222037526867372L;
    @Column(name = "uid", nullable = false)
    private Long uid;
    private int type;           //反馈：0 回复：1
    private String content;     //内容
    private String brand;       //品牌
    private String os;          //系统
    private String channel;     //渠道
    private String version;     //app版本
}
