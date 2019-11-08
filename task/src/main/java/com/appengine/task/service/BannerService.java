package com.appengine.task.service;

import com.appengine.task.dao.BannerDao;
import com.appengine.task.domain.Banner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-12 00:17.
 */
@Service
public class BannerService {
    private BannerDao bannerDao;

    public Banner getBanner(Long id) {
        return bannerDao.findById(id).orElse(null);
    }

    public Banner saveBanner(Banner entity) {
        Banner banner;
        if (entity.getId() != null) {
            banner = bannerDao.findById(entity.getId()).orElse(null);
            if (banner == null) {
                banner = entity;
            }
            if (entity.getTitle() != null) {
                banner.setTitle(entity.getTitle());
            }
            if (entity.getImage() != null) {
                banner.setImage(entity.getImage());
            }
            if (entity.getUrl() != null) {
                banner.setUrl(entity.getUrl());
            }
            if (entity.getApp() != null) {
                banner.setApp(entity.getApp());
            }
        } else {
            banner = entity;
        }
        return bannerDao.save(banner);
    }

    public boolean deleteBanner(Long id) {
        Banner task = getBanner(id);
        if (task == null) {
            return false;
        }
        bannerDao.deleteById(id);
        return true;
    }

    public Page<Banner> getAllBanner(PageRequest request, String app) {
        return bannerDao.findAllByApp(app, request);
    }

    public Page<Banner> getAllBanner(PageRequest request) {
        return bannerDao.findAll(request);
    }

    @Autowired
    public void setBannerDao(BannerDao bannerDao) {
        this.bannerDao = bannerDao;
    }
}
