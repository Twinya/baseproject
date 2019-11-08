package com.appengine.task.service;

import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.task.dao.PhoneAreaDao;
import com.appengine.task.domain.PhoneArea;
import com.appengine.task.exception.PhoneAreaExceptionFactor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneAreaService {

    PhoneAreaDao phoneAreaDao;

    public PhoneArea getPhoneArea(String phone) {
        if (phone == null) {
            throw EngineExceptionHelper.localException(PhoneAreaExceptionFactor.NO_PHONE_NUMBER);
        }
        if (phone.length() < 7) {
            throw EngineExceptionHelper.localException(PhoneAreaExceptionFactor.NO_PHONE_NUMBER);
        }
        if (phone.length() > 7) {
            phone = phone.substring(0, 7);
        }
        PhoneArea phoneArea = phoneAreaDao.findByPhone(phone);
        if (null == phoneArea) {
            throw EngineExceptionHelper.localException(PhoneAreaExceptionFactor.NO_PHONE_NUMBER);
        }
        return phoneArea;
    }

    @Autowired
    public void setPhoneAreaDao(PhoneAreaDao phoneAreaDao) {
        this.phoneAreaDao = phoneAreaDao;
    }
}
