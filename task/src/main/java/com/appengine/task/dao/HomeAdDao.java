package com.appengine.task.dao;

import com.appengine.task.domain.HomeAd;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HomeAdDao extends PagingAndSortingRepository<HomeAd, Long>, JpaSpecificationExecutor<HomeAd> {
    HomeAd findFirstByApp(String app);
}
