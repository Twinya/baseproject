package com.appengine.user.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.frame.context.RequestContext;
import com.appengine.user.domain.form.FeedForm;
import com.appengine.user.domain.po.FeedBackRecord;
import com.appengine.user.service.FeedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-02 22:08.
 */
@Api(tags = "意见反馈")
@Validated
@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Resource
    private FeedService feedService;

    @ApiOperation(value = "反馈")
    @BaseInfo(desc = "意见反馈", needAuth = AuthType.REQUIRED)
    @PostMapping(value = "")
    public String feedback(@ApiIgnore RequestContext rc,
                           @Valid @RequestBody FeedForm feedForm) {
        FeedBackRecord record = feedForm.toPo(FeedBackRecord.class);
        record.setUid(rc.getCurrentUid());
        return feedService.feedback(record);
    }

    @BaseInfo(needAuth = AuthType.REQUIRED)
    @ApiOperation(value = "回复意见反馈")
    @ApiImplicitParam(paramType = "path", name = "uid", value = "用户id", required = true, dataType = "long")
    @PostMapping(value = "/replay/{uid}")
    public String feedback(
            @PathVariable Long uid,
            @RequestParam String content) {
        FeedBackRecord record = new FeedBackRecord();
        record.setUid(uid);
        record.setContent(content);
        return feedService.replay(record);
    }

    @ApiOperation(value = "意见反馈列表")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @GetMapping(value = "/list")
    public Page<FeedBackRecord> list(@ApiIgnore RequestContext rc,
                                     @RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(value = "pagesize", required = false, defaultValue = "100") int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageRequest request = PageRequest.of(page - 1, pageSize, sort);
        return feedService.getFeedRecord(rc.getCurrentUid(), request);
    }

}
