package com.appengine.task.dao;

import com.appengine.task.domain.PhoneArea;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PhoneAreaDao extends PagingAndSortingRepository<PhoneArea, Long>, JpaSpecificationExecutor<PhoneArea> {
    PhoneArea findByPhone(String phone);
}
