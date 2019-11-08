package com.appengine.im.dao;

import com.appengine.im.domain.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findFirstByUsername(String username);

}
