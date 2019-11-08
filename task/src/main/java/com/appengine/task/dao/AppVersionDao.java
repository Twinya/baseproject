package com.appengine.task.dao;

import com.appengine.task.domain.AppVersion;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppVersionDao extends PagingAndSortingRepository<AppVersion, Long>, JpaSpecificationExecutor<AppVersion> {
    AppVersion findFirstByAppAndPlatform(String app, String platform);
}
