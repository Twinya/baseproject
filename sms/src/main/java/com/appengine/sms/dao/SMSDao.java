package com.appengine.sms.dao;

import com.appengine.sms.domain.SMSrecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SMSDao extends PagingAndSortingRepository<SMSrecord, Long>, JpaSpecificationExecutor<SMSrecord> {

    SMSrecord findFirstByRequestId(String requestId);

    List<SMSrecord> findTop100ByOrderByIdDesc();
}
