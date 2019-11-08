package com.appengine.task.dao;

import com.appengine.task.domain.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BannerDao extends PagingAndSortingRepository<Banner, Long>, JpaSpecificationExecutor<Banner> {
    Page<Banner> findAllByApp(String app, Pageable pageable);
}
