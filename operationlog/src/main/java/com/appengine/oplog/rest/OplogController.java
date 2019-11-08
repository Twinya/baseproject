package com.appengine.oplog.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.oplog.service.OplogService;
import com.appengine.user.domain.IdUserNameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/oplog")
public class OplogController {

    @Resource
    OplogService oplogService;

    @GetMapping(value = "")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    public Page<IdUserNameEntity> get(
            @RequestParam String operation,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "100") int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageRequest request = PageRequest.of(page - 1, pageSize, sort);
        return oplogService.getListByOperation(operation, request);
    }

    @GetMapping(value = "/module")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    public Page<IdUserNameEntity> getByResourceId(
            @RequestParam String module,
            @RequestParam(required = false, defaultValue = "0") Long resourceId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "100") int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageRequest request = PageRequest.of(page - 1, pageSize, sort);
        if (resourceId == 0) {
            return oplogService.getListByModule(module, request);
        } else {
            return oplogService.getListByModuleResourceId(module, resourceId, request);
        }
    }

    @GetMapping(value = "/user")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    public Page<IdUserNameEntity> getByUserId(
            @RequestParam Long uid,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "100") int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageRequest request = PageRequest.of(page - 1, pageSize, sort);
        return oplogService.getListByUserId(uid, request);
    }

}
