package com.appengine.im.service;

import com.appengine.auth.model.AuthRequest;
import com.appengine.auth.provider.UserProvider;
import com.appengine.im.domain.po.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-02 22:10.
 */
@Service
public class IMUserProviderImpl implements UserProvider {
    @Resource
    private IMService imService;

    @Override
    public boolean isValidUser(long uid) {
        if (uid == 0) {
            return true;
        }
        return imService.get(uid) != null;
    }

    @Override
    public boolean checkCanAccess(AuthRequest request, long uid) {
        return true;
    }

    @Override
    public long authUser(String loginName, String from, String password) {
        User user = imService.login(loginName, from, password);
        return user != null ? user.getId() : 0;
    }
}
