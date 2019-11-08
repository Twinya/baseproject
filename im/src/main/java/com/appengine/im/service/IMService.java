package com.appengine.im.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.common.model.RegisterInfo;
import cn.jmessage.api.common.model.UserPayload;
import cn.jmessage.api.user.UserInfoResult;
import cn.jmessage.api.user.UserListResult;
import com.appengine.auth.model.AuthExcepFactor;
import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.common.utils.PageHelp;
import com.appengine.im.dao.UserDao;
import com.appengine.im.domain.po.User;
import com.appengine.im.exception.IMExceptionFactor;
import com.appengine.im.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class IMService {
    public static final List<String> IMAGE_TYPES = Arrays.asList("png", "jpg", "jpeg");
    @Autowired
    private UserDao userDao;
    private JMessageClient jMessageClient;

    public User registerUsers(User user) {
        RegisterInfo registerInfo = RegisterInfo.newBuilder()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .build();
        RegisterInfo[] registerInfos = new RegisterInfo[1];
        registerInfos[0] = registerInfo;
        try {
            getClient().registerUsers(registerInfos);
            return saveUser(user);
        } catch (APIConnectionException e) {
            throw EngineExceptionHelper.localException(IMExceptionFactor.REGIST_FAILED);
        } catch (APIRequestException e) {
            if (e.getErrorCode() == 899001) {
                return saveUser(user);
            } else {
                throw EngineExceptionHelper.localException(IMExceptionFactor.REGIST_FAILED);
            }
        }
    }

    private User saveUser(User user) {
        User exist = userDao.findFirstByUsername(user.getUsername());
        if (exist != null) {
            return exist;
        }
        return userDao.save(user);
    }

    private JMessageClient getClient() {
        if (jMessageClient == null) {
            jMessageClient = new JMessageClient(Constants.JIGUANG_IM_KEY, Constants.JIGUANG_IM_SECRET);
        }
        return jMessageClient;
    }

    public UserInfoResult getUserInfo(String username) {
        try {
            return getClient().getUserInfo(username);
        } catch (APIConnectionException | APIRequestException e) {
            throw EngineExceptionHelper.localException(IMExceptionFactor.GET_USER_INFO_FAILED);
        }
    }

    public void setUserInfo(String username, UserPayload payload) {
        try {
            getClient().updateUserInfo(username, payload);
        } catch (APIConnectionException | APIRequestException e) {
            throw EngineExceptionHelper.localException(IMExceptionFactor.GET_USER_INFO_FAILED);
        }
    }

    public Boolean validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw EngineExceptionHelper.localException(IMExceptionFactor.UPLOAD_FILE_IS_NULL);
        }
        return validateFileName(file.getOriginalFilename());
    }

    private boolean validateFileName(String fname) {
        if (fname == null) {
            return false;
        }
        String[] suffixs = fname.split("\\.");
        if (suffixs.length < 1) {
            throw EngineExceptionHelper.localException(IMExceptionFactor.UPLOAD_FILE_TYPE_ERROR);
        }
        String suffix = suffixs[suffixs.length - 1];
        return IMAGE_TYPES.stream().anyMatch(item -> item.equals(suffix));
    }

    User get(long uid) {
        return userDao.findById(uid).orElseThrow(() -> EngineExceptionHelper.localException(IMExceptionFactor.ACCOUNT_NOT_EXISTS));
    }

    public User login(String username, String from, String password) {
        User user = userDao.findFirstByUsername(username);
        if (user == null) {
            throw EngineExceptionHelper.localException(AuthExcepFactor.E_AUTH_USERNAME_ERROR);
        }
        if (!StringUtils.equals(password, user.getPassword())) {
            throw EngineExceptionHelper.localException(AuthExcepFactor.E_AUTH_PASSWORD_ERROR);
        }
        return user;
    }

    public void addBlackList(String owner_username, String[] black_usernames) {
        for (String black_username : black_usernames) {
            try {
                getClient().addBlackList(owner_username, black_username);
            } catch (APIConnectionException | APIRequestException e) {
                e.printStackTrace();
            }
        }
    }

    public PageHelp<String> getBlackList(String username, Long page, Long pagesize) {
        try {
            UserInfoResult[] blackList = getClient().getBlackList(username);
            return new PageHelp<>(page, pagesize, blackList);
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer RemoveBlacklist(String username, List<String> black_usernames) {
        Integer result = null;
        for (String black_username : black_usernames) {
            try {
                ResponseWrapper responseWrapper = getClient().removeBlacklist(username, black_username);
                result = responseWrapper.responseCode + 2;
            } catch (APIConnectionException | APIRequestException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public UserListResult getUserList() {
        UserListResult userList = null;
        try {
            userList = getClient().getUserList(0, 10);
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
        }
        return userList;
    }

}
