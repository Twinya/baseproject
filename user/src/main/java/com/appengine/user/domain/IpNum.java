package com.appengine.user.domain;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class IpNum {
    public String ip;
    public Long num;

    public IpNum(String ip, String num) {
        this.ip = ip;
        this.num = Long.decode(num);
    }

    public IpNum(String ip, Long num) {
        this.ip = ip;
        this.num = num;
    }

    public static List<IpNum> fromObjectList(List<Object> r) {
        List<IpNum> result = new ArrayList<>();
        for (Object o : r) {
            Object[] temp = (Object[]) o;
            result.add(new IpNum(temp[0].toString(), temp[1].toString()));
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof IpNum) {

            IpNum inobj = (IpNum) obj;
            if (this.ip != null) {
                return this.ip.equals(inobj.ip);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.ip.hashCode();
    }
}
