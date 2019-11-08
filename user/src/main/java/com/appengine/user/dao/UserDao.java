package com.appengine.user.dao;

import com.appengine.user.domain.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;

public interface UserDao extends CrudRepository<User, Long> , JpaSpecificationExecutor<User> {

    User findByUsernameAndJurisdictionIsNot(String username,Integer zero);

    User findByUsernameAndRegistFrom(String username, String from);

    User findFirstByIdAndJurisdiction(long uid, int jurisdiction);


    Page<User> findAllByJurisdiction(int jurisdiction, Pageable request);

    Page<User> findAllByRealNameLikeAndJurisdiction(String name, int jurisdiction, Pageable request);



    Long countAllByRegistFromAndJurisdictionIsNotAndIdLessThanEqual(String from, Integer zero, long uid);




    @Query(value = "SELECT * FROM `user` ORDER BY id LIMIT 1", nativeQuery = true)
    User getMinUid();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO `user` (uid,username,password,jurisdiction,real_name,create_time,regist_from,salt,isShow,isApply,isLoan,version) VALUES(?1,?2,?3,?4,?5,?6,?7,?8,0,0,0,0)",nativeQuery = true)
    void saveInnerUser(Long uid, String username, String password, int jurisdiction, String realName, Date createTime, String registFrom, String salt);

}
