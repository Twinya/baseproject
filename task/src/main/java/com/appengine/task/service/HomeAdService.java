package com.appengine.task.service;

import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.task.dao.HomeAdDao;
import com.appengine.task.domain.HomeAd;
import com.appengine.task.utils.TaskExcepFactor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-12 00:17.
 */
@Service
public class HomeAdService {
    @Resource
    private HomeAdDao homeAdDao;

    public Page<HomeAd> getAds(PageRequest request) {
        return homeAdDao.findAll(request);
    }

    public HomeAd getByApp(String app) {
        if (StringUtils.isEmpty(app)) {
            return homeAdDao.findFirstByApp("default");
        }
        return homeAdDao.findFirstByApp(app);
    }

    public HomeAd addHomeAd(String app, String image, String url, Long productId, String name, String channel, boolean isShow) {
        return homeAdDao.save(new HomeAd(app, image, url, productId, name, channel, isShow));
    }

    public HomeAd editHomeAd(Long id, String app, String image, String url, Long productId, String name, String channel, boolean isShow) {
        return homeAdDao.save(new HomeAd(id, app, image, url, productId, name, channel, isShow));
    }

    public HomeAd setShow(Long id, boolean isShow) {
        Optional<HomeAd> homeAd = homeAdDao.findById(id);
        if (homeAd.isPresent()) {
            HomeAd ad = homeAd.get();
            ad.setIsShow(isShow);
            return homeAdDao.save(ad);
        } else {
            throw EngineExceptionHelper.localException(TaskExcepFactor.NOT_EXISTS);
        }
    }

    public boolean delHomeAd(Long id) {
        try {
            homeAdDao.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
