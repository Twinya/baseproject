package com.appengine.user.dao;

import com.appengine.user.domain.po.FeedBackRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface FeedBackDao extends CrudRepository<FeedBackRecord, Long> {
    Page<FeedBackRecord> findByUid(Long uid, Pageable request);
}
