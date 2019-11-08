package com.appengine.task.dao;

import com.appengine.task.domain.Toutiao;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ToutiaoDao extends PagingAndSortingRepository<Toutiao, Long>, JpaSpecificationExecutor<Toutiao> {

}
