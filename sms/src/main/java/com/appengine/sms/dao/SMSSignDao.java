package com.appengine.sms.dao;

import com.appengine.sms.domain.SMSSign;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SMSSignDao extends PagingAndSortingRepository<SMSSign, Long>, JpaSpecificationExecutor<SMSSign> {
    SMSSign findBySign(String sign);
}
