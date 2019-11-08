package com.appengine.oplog.service;

import com.alibaba.fastjson.JSON;
import com.appengine.oplog.dao.OplogDao;
import com.appengine.oplog.domain.Oplog;
import com.appengine.user.dao.UserDao;
import com.appengine.user.domain.IdUserNameEntity;
import com.appengine.user.domain.po.User;
import com.appengine.user.utils.UserId2NameUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-12 00:17.
 */
@Service
public class OplogService {
    @Resource
    private OplogDao oplogDao;
    @Resource
    UserDao userDao;

    public Page<IdUserNameEntity> getListByOperation(String operation, PageRequest request) {
        Page<IdUserNameEntity> all = oplogDao.findAllByOperation(operation, request);
        Iterable<User> users = userDao.findAllById(getUserIds(all));
        return UserId2NameUtil.getIpaRecordWithUserName(all, users);
    }

    public Page<IdUserNameEntity> getListByModuleResourceId(String module, Long resourceId, PageRequest request) {
        Page<IdUserNameEntity> all = oplogDao.findAllByModuleAndResourceId(module, resourceId, request);
        Iterable<User> users = userDao.findAllById(getUserIds(all));
        return UserId2NameUtil.getIpaRecordWithUserName(all, users);
    }

    public Page<IdUserNameEntity> getListByUserId(Long userId, PageRequest request) {
        Page<IdUserNameEntity> all = oplogDao.findAllByUserId(userId, request);
        Iterable<User> users = userDao.findAllById(getUserIds(all));
        return UserId2NameUtil.getIpaRecordWithUserName(all, users);
    }

    public Page<IdUserNameEntity> getListByModule(String module, PageRequest request) {
        Page<IdUserNameEntity> all = oplogDao.findAllByModule(module, request);
        Iterable<User> users = userDao.findAllById(getUserIds(all));
        return UserId2NameUtil.getIpaRecordWithUserName(all, users);
    }

    public Oplog addOplog(Long userId, String module, Long resourceId, String operation, Object after) {
        after = JSON.toJSON(after);
        return oplogDao.save(new Oplog(userId, module, resourceId, operation, after.toString()));
    }

    private Iterable<Long> getUserIds(Page<IdUserNameEntity> all) {
        List<Long> result = new ArrayList<>();
        all.forEach(record -> result.add(record.getUserId()));
        return result;
    }

}
