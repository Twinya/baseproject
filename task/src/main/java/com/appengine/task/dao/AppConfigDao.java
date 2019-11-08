package com.appengine.task.dao;

import com.appengine.task.domain.AppConfig;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppConfigDao extends PagingAndSortingRepository<AppConfig, Long>, JpaSpecificationExecutor<AppConfig> {
    AppConfig findFirstByAppOrderByIdDesc(String app);
}
