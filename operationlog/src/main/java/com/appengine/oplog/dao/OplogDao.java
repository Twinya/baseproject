package com.appengine.oplog.dao;

import com.appengine.oplog.domain.Oplog;
import com.appengine.user.domain.IdUserNameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OplogDao extends PagingAndSortingRepository<Oplog, Long>, JpaSpecificationExecutor<Oplog> {

    Page<IdUserNameEntity> findAllByOperation(String operation, Pageable request);

    Page<IdUserNameEntity> findAllByModuleAndResourceId(String module, Long resourceId, Pageable request);

    Page<IdUserNameEntity> findAllByUserId(Long userId, Pageable request);

    Page<IdUserNameEntity> findAllByModule(String module, Pageable request);
}
