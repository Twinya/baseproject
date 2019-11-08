package com.appengine.user.dao;

import com.appengine.user.domain.po.LoginNumByDay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoginNumByDayDao extends CrudRepository<LoginNumByDay, Long> {

    @Query(value = "SELECT channel,SUM(num) as num FROM login_num_day GROUP BY channel", nativeQuery = true)
    List<Object> getAllChannelLoginNum();

    LoginNumByDay findFirstByYyyyMMdd(Integer yyyyMMdd);

}
