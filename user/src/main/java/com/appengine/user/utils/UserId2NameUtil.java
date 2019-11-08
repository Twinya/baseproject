package com.appengine.user.utils;

import com.appengine.user.domain.IdUserNameEntity;
import com.appengine.user.domain.po.User;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author fuyou
 * @Date 2019/7/15 18:02
 */
public class UserId2NameUtil {
    public static Page<IdUserNameEntity> getIpaRecordWithUserName(Page<IdUserNameEntity> all, Iterable<User> users) {
        Map<Long, String> userNames = new HashMap<>();
        users.forEach(u -> userNames.put(u.getId(), u.getRealName()));
        userNames.put(0L, "系统");
        all.getContent().forEach(r -> r.setUserName(userNames.get(r.getUserId())));
        return all;
    }
}
