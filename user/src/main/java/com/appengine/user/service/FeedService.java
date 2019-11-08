package com.appengine.user.service;

import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.user.dao.FeedBackDao;
import com.appengine.user.domain.po.FeedBackRecord;
import com.appengine.user.utils.UserExcepFactor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FeedService {
    public static final int TYPE_FEEDBACK = 0;
    public static final int TYPE_REPLAY = 1;
    @Resource
    FeedBackDao feedBackDao;

    public String feedback(FeedBackRecord record) {
    	record.setType(TYPE_FEEDBACK);
        if (null == feedBackDao.save(record)) {
            throw EngineExceptionHelper.localException(UserExcepFactor.FEEDBACK_ERROR);
        }
        return "反馈成功";
    }

    public String replay(FeedBackRecord record) {
    	record.setType(TYPE_REPLAY);
        if (null == feedBackDao.save(record)) {
            throw EngineExceptionHelper.localException(UserExcepFactor.FEEDBACK_ERROR);
        }
        return "回复成功";
    }

    public Page<FeedBackRecord> getFeedRecord(Long uid, Pageable request) {
        if (uid <= 0) {
            throw EngineExceptionHelper.localException(UserExcepFactor.FEEDBACK_ERROR);
        }
        return feedBackDao.findByUid(uid, request);
    }

}
