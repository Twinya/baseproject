package com.appengine.task.service;

import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.task.dao.AppConfigDao;
import com.appengine.task.domain.AppConfig;
import com.appengine.task.utils.TaskExcepFactor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
public class AppConfigService {
    @Resource
    private AppConfigDao appConfigDao;

    public Page<AppConfig> getAppConfigs(PageRequest request) {
        return appConfigDao.findAll(request);
    }

    @CacheEvict(cacheNames = "app-config", allEntries = true)
    public boolean delConfig(Long id) {
        try {
            appConfigDao.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @CacheEvict(cacheNames = "app-config", allEntries = true)
    public AppConfig addAppConfig(String app, Integer delay) {
        return appConfigDao.save(new AppConfig(app, delay));
    }

    @CacheEvict(cacheNames = "app-config", allEntries = true)
    public AppConfig editAppConfig(Long id, String app, Integer delay) {
        Optional<AppConfig> optional = appConfigDao.findById(id);
        AppConfig config = optional.orElseThrow(() -> EngineExceptionHelper.localException(TaskExcepFactor.NOT_EXISTS));
        config.setApp(app);
        config.setDelay(delay);
        return appConfigDao.save(config);
    }

    @Cacheable(cacheNames = "app-config", key = "#app", sync = true)
    public AppConfig getConfig(String app) {
        return appConfigDao.findFirstByAppOrderByIdDesc(app);
    }

}
