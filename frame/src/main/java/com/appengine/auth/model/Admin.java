package com.appengine.auth.model;

import java.util.Arrays;
import java.util.List;

/**
 * @Author fuyou
 * @Date 2018/12/19 14:31
 */
public class Admin {
    public static final List<Long> admin = Arrays.asList(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);

    public static boolean isAdmin(Long uid) {
        if (uid < 0) {
            return true;
        }
        return admin.contains(uid);
    }
}
