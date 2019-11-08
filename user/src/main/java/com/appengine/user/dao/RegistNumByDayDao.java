package com.appengine.user.dao;

import com.appengine.user.domain.po.RegisterNumByDay;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistNumByDayDao extends CrudRepository<RegisterNumByDay, Long> {
    RegisterNumByDay findFirstByYyyyMMdd(Integer yyyyMMdd);

    RegisterNumByDay findFirstByOrderByIdDesc();

    List<RegisterNumByDay> findTop30ByOrderByIdDesc();
}
