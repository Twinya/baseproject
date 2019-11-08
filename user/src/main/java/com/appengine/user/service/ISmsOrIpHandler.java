package com.appengine.user.service;

import com.appengine.common.utils.PageHelp;
import com.appengine.user.domain.AuthOptionEnum;

public interface ISmsOrIpHandler {

    Boolean add(String arg, AuthOptionEnum status);

    Long del(String arg, AuthOptionEnum status);

    Long count(AuthOptionEnum key);

    PageHelp<String> findAll(AuthOptionEnum key, Long start, Long end);


}
