package com.appengine.user.dao;

import com.appengine.user.domain.po.LoginRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoginRecordDao extends CrudRepository<LoginRecord, Long> {

    @Query(value = "SELECT channel,COUNT(uid)  as num FROM login_record WHERE first_login=true and create_time>=?1 and create_time<?2 GROUP BY channel", nativeQuery = true)
    List<Object> getChannelLoginNum(Long startTime, Long endTime);

    @Query(value = "SELECT COUNT(DISTINCT uid)  as num FROM login_record WHERE create_time>=?1 and create_time<?2", nativeQuery = true)
    String getAllLoginNum(Long startTime, Long endTime);

    @Query(value = "SELECT COUNT(DISTINCT uid)  as num  FROM login_record WHERE channel = ?1 and create_time>?2 and create_time<?3", nativeQuery = true)
    String getLoginNumByChannel(String channel, Long startTime, Long endTime);

    LoginRecord getFirstByUid(Long id);

    Page<LoginRecord> findDistinctUidByChannel(String channel, Pageable request);

    @Query(value = "SELECT app,phone_os as os,COUNT(uid) as num FROM login_record WHERE first_login=true and create_time>=?1 and create_time<?2 AND phone_os in('ios','android')AND app is not NULL GROUP BY app,phone_os", nativeQuery = true)
    List<Object> getosLoginNum(Long startTime, Long endTime);

    @Query(value = "SELECT * FROM login_record WHERE create_time = (SELECT MAX(create_time) FROM login_record WHERE uid = ?1)",nativeQuery = true)
    LoginRecord getInnerUserLastLoginTimes(Long uid);

}
