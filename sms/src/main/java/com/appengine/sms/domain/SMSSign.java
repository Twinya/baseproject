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
@NoArgsConstructor
@Entity
@Table(name = "sms_sign")
public class SMSSign extends BasePo {

    public String sign;
    public String name;
    @Column(name = "template_id")
    public String templateId;
    public String isp;

    public SMSSign(String sign, String name, String templateId) {
        this.sign = sign;
        this.name = name;
        this.templateId = templateId;
    }

}
