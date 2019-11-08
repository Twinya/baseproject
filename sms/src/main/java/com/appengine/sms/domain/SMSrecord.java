package com.appengine.sms.domain;

import com.appengine.common.entity.po.BasePo;
import lombok.*;

import javax.persistence.*;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-12 00:12.
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "sms_record", indexes = {@Index(name = "phoneNumber", columnList = "phoneNumber"), @Index(name = "requestId", columnList = "requestId")})
public class SMSrecord extends BasePo {
    private static final long serialVersionUID = -315763539488782065L;
    /**
     * 0:register
     * 1:login
     */
    public Integer type;
    public String phoneNumber;
    public String code;
    public String msg;
    public String ip;
    @Column(name = "requestId", nullable = false)
    public String requestId;

}
