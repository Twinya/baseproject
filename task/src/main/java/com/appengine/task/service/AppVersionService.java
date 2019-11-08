package com.appengine.task.service;

import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.task.dao.AppVersionDao;
import com.appengine.task.domain.AppVersion;
import com.appengine.task.utils.TaskExcepFactor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-12 00:17.
 */
@Service
public class AppVersionService {
    @Resource
    private AppVersionDao appVersionDao;

    public Page<AppVersion> getAll(Pageable pageable) {
        return appVersionDao.findAll(pageable);
    }

    public AppVersion getVersion(String app, String platform) {
        return appVersionDao.findFirstByAppAndPlatform(app, platform);
    }

    public AppVersion add(String app, String platform, String url, Integer version, Boolean forceUpdate) {
        if (appVersionDao.findFirstByAppAndPlatform(app, platform) != null) {
            throw EngineExceptionHelper.localException(TaskExcepFactor.VERSION_EXIST);
        }
        return appVersionDao.save(new AppVersion(app, platform, url, version, forceUpdate));
    }

    public AppVersion edit(Long id, String url, Integer version, Boolean update) {
        AppVersion appVersion = appVersionDao.findById(id).orElseThrow(() -> EngineExceptionHelper.localException(TaskExcepFactor.VERSION_NOT_EXIST));
        appVersion.setUrl(url);
        appVersion.setVersion(version);
        appVersion.setUpdate(update);
        appVersion.setUpdateTime(System.currentTimeMillis());
        return appVersionDao.save(appVersion);
    }

}
