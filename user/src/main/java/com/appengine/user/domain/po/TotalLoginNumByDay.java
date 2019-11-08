package com.appengine.user.domain.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TotalLoginNumByDay {

    public String channel;
    // 统计值 不定长用string表示
    public String num;

    public TotalLoginNumByDay(String channel, String num) {
        this.channel = channel;
        this.num = num;
    }
}
