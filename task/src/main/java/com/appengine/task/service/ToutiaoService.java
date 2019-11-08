package com.appengine.task.service;

import com.appengine.task.dao.ToutiaoDao;
import com.appengine.task.domain.Toutiao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-12 00:17.
 */
@Service
public class ToutiaoService {
    @Resource
    private ToutiaoDao toutiaoDao;

    public Page<Toutiao> getList(PageRequest request) {
        return toutiaoDao.findAll(request);
    }

    public Toutiao addToutiao(String content) {
        return toutiaoDao.save(new Toutiao(content));
    }

    public Toutiao editToutiao(Long id,String content){
        return toutiaoDao.save(new Toutiao(id, content));
    }

}
