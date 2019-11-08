package com.appengine.user.service;

import com.appengine.auth.model.AuthExcepFactor;
import com.appengine.auth.redis.SMSRedisDao;
import com.appengine.common.encrypt.Digests;
import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.sms.dao.SMSDao;
import com.appengine.sms.domain.SMSrecord;
import com.appengine.sms.utils.PhoneNumber;
import com.appengine.sms.utils.RandomCode;
import com.appengine.user.dao.LoginRecordDao;
import com.appengine.user.dao.UserDao;
import com.appengine.user.domain.po.LoginRecord;
import com.appengine.user.domain.po.User;
import com.appengine.user.utils.UserExcepFactor;
import lombok.NonNull;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author fuyou
 * @version 1.0 Created at: 2015-10-13 17:02
 */
@Service
public class UserService {
    public static final int HASH_INTERATIONS = 1024;

    @Resource
    private UserDao userDao;
    @Resource
    private SMSDao smsDao;
    @Resource
    private SMSRedisDao smsRedisDao;
    @Resource
    private LoginRecordDao loginRecordDao;

    public User save(User user) {
        entryptPassword(user);
        user = userDao.save(user);
        if (user.getId() <= 0) {
            throw EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_EXISTS);
        }
        return user;
    }

    public boolean checkUserExist(String username, String from) {
        checkPhoneNumber(username);
        User existsUser = userDao.findByUsernameAndRegistFrom(username, from);
        if (existsUser != null) {
            throw EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_EXISTS);
        }
        return true;
    }

    public boolean containsUser(String username, String from) {
        User existsUser = userDao.findByUsernameAndRegistFrom(username, from);
        return existsUser != null;
    }

    public void checkPhoneNumber(String username) {
        if (!PhoneNumber.getInstance().verify(username)) {
            throw EngineExceptionHelper.localException(UserExcepFactor.MOBILE_NUMBER_ERROR);
        }
    }


    public User createFirstPartUser(String realName, String ip, int jurisdiction) {
        String username = "1" + System.currentTimeMillis() / 1000;
        checkPhoneNumber(username);
        checkUserExist(username, "first");
        User user = new User(username, "first", "123456", username, "admin", ip);
        user.setRealName(realName);
        user.setJurisdiction(jurisdiction);
        entryptPassword(user);
        return userDao.save(user);
    }

    public User set(User user) {
        checkUserExist(user.getId());
        user = userDao.save(user);
        return user;
    }

    private User checkUserExist(Long id) {
        Optional<User> existsUser = userDao.findById(id);
        if (!existsUser.isPresent()) {
            throw EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_NOT_EXISTS);
        }
        return existsUser.get();
    }

    public User get(long id) {
        return checkUserExist(id);
    }

    public User getFirstPartUser(long uid) {
        User u = userDao.findFirstByIdAndJurisdiction(uid, 4);
        if (u == null) {
            throw EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_NOT_EXISTS);
        }
        return u;
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(127);
        user.setSalt(Hex.encodeHexString(salt));

        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Hex.encodeHexString(hashPassword));
    }


//    public static void main(String[] args) {
//        System.out.println(getMembers().getContent());
//    }

    private String entryptPassword(String saltStr, String password) {

        byte[] salt = new byte[0];
        try {
            salt = Hex.decodeHex(saltStr.toCharArray());
        } catch (DecoderException ignored) {
        }
        byte[] hashPassword = Digests.sha1(password.getBytes(), salt, HASH_INTERATIONS);
        return Hex.encodeHexString(hashPassword);
    }

    public User login(String mobile, String from, String password) {
        User user = userDao.findByUsernameAndRegistFrom(mobile, from);
        if (user == null) {
            throw EngineExceptionHelper.localException(AuthExcepFactor.E_AUTH_USERNAME_ERROR);
        }
        if (!StringUtils.equals(entryptPassword(user.getSalt(), password), user.getPassword())) {
            throw EngineExceptionHelper.localException(AuthExcepFactor.E_AUTH_PASSWORD_ERROR);
        }
        return user;
    }

    public User login(String loginName, String from, String code, String requestId, String channel, String ip, String phone_os) {
        User user = userDao.findByUsernameAndRegistFrom(loginName, from);
        if (user == null) {
            User tUser = new User(loginName, from, RandomCode.getCode(), loginName, channel, ip);
            return register(tUser, from, code, requestId, phone_os);
        }
        checkCode(user.getMobile(), code, requestId);
        return user;
    }

    public User register(@NonNull User user, String from, String code, String requestId, String phone_os) {
        checkPhoneNumber(user.getUsername());
        User existsUser = userDao.findByUsernameAndRegistFrom(user.getUsername(), from);
        if (existsUser != null) {
            throw EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_EXISTS);
        }
        checkCode(user.getMobile(), code, requestId);
        entryptPassword(user);
        user.setPhoneOs(phone_os);
        user = userDao.save(user);
        return user;
    }

    private void checkCode(String mobile, String code, String requestId) {
        SMSrecord srecord = smsDao.findFirstByRequestId(requestId);
        if (srecord == null) {
            throw EngineExceptionHelper.localException(AuthExcepFactor.E_AUTH_REQUEST_ERROR);
        }
        if (!StringUtils.equals(code, srecord.getCode())) {
            throw EngineExceptionHelper.localException(AuthExcepFactor.E_AUTH_CODE_ERROR);
        }
        if (!smsRedisDao.isValidCode(requestId, mobile, code)) {
            throw EngineExceptionHelper.localException(AuthExcepFactor.E_AUTH_CODE_EXPIRED);
        }
    }

    public User changePassword(@NonNull Long uid, @NonNull String password, @NonNull String newPassword) {
        User existsUser = checkUserExist(uid);
        if (!StringUtils.equals(entryptPassword(existsUser.getSalt(), password), existsUser.getPassword())) {
            throw EngineExceptionHelper.localException(UserExcepFactor.USERPASS_ERROR);
        }
        existsUser.setPassword(newPassword);
        entryptPassword(existsUser);
        return userDao.save(existsUser);
    }

    public User resetPassword(@NonNull String userName, String from, String password, String code, String requestId) {
        User existsUser = userDao.findByUsernameAndRegistFrom(userName, from);
        if (existsUser == null) {
            throw EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_NOT_EXISTS);
        }
        if (!smsRedisDao.isValidCode(requestId, userName, code)) {
            throw EngineExceptionHelper.localException(AuthExcepFactor.E_AUTH_CODE_EXPIRED);
        }
        existsUser.setPassword(password);
        entryptPassword(existsUser);
        return userDao.save(existsUser);
    }

    public Page<User> getFirstPartUser(Pageable request) {
        return userDao.findAllByJurisdiction(4, request);
    }

    public Page<User> getFirstPartUserByName(String name, Pageable request) {
        return userDao.findAllByRealNameLikeAndJurisdiction("%" + name + "%", 4, request);
    }

    public User innerRegister(User user) {
        checkPhoneNumber(user.getUsername());
        User existsUser = userDao.findByUsernameAndJurisdictionIsNot(user.getUsername(),0);
        if (existsUser != null) {
            throw EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_EXISTS);
        }
        entryptPassword(user);
        userDao.saveInnerUser(userDao.getMinUid().getId() - 1, user.getUsername(), user.getPassword(), user.getJurisdiction(), user.getRealName(), user.getCreatedTime(),user.getRegistFrom(),user.getSalt());
        return userDao.findByUsernameAndJurisdictionIsNot(user.getUsername(),0);
    }

    public User editInnerUser(Long id, String username, String password, Integer jurisdiction, String registFrom,String realName) {
        User user = userDao.findById(id).orElseThrow(()->EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_NOT_EXISTS));
        if (username != null) {
            user.setUsername(username);
        }
        if (password != null) {
            user.setPassword(password);
        }
        if (jurisdiction != null) {
            user.setJurisdiction(jurisdiction);
        }
        if (realName != null) {
            user.setRealName(realName);
        }
        if (registFrom != null) {
            user.setRegistFrom(registFrom);
        }
        if (password != null) {
            entryptPassword(user);
        }
        return userDao.save(user);
    }

    public boolean deleteInnerUser(Long id) {
        User user = userDao.findById(id).get();
        if (user == null) {
            return false;
        }
        user.setRegistFrom("No");
//        userDao.deleteById(id);
        userDao.save(user);
        return true;
    }

    public Map<String, Object> findInnerUsers(String username, String realName, Integer jurisdiction, String registFrom, PageRequest request) {
        Specification specification = (Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (username != null) {
                predicateList.add(criteriaBuilder.equal(root.get("username"), username));
            }
            if (realName != null) {
                predicateList.add(criteriaBuilder.equal(root.get("realName"), realName));
            }
            if (jurisdiction != null) {
                predicateList.add(criteriaBuilder.equal(root.get("jurisdiction"), jurisdiction));
            }
            if (registFrom != null) {
                predicateList.add(criteriaBuilder.equal(root.get("registFrom"), registFrom));
            }
            predicateList.add(criteriaBuilder.notEqual(root.get("jurisdiction"),0));
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("uid"),"0"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        Page<User> users = userDao.findAll(specification, request);
        for (int i = 0; i < users.getContent().size(); i++) {
            Long uid = users.getContent().get(i).getId();
            LoginRecord lastLoginTime = loginRecordDao.getInnerUserLastLoginTimes(uid);
            if (lastLoginTime != null) {
                users.getContent().get(i).setLastLoginTime(lastLoginTime);
            } else {
                LoginRecord loginRecord = new LoginRecord();
                loginRecord.setCreateTime(0L);
                users.getContent().get(i).setLastLoginTime(loginRecord);
            }
        }
        Map<String,Object> map = new HashMap<>();
        Long usable = userDao.countAllByRegistFromAndJurisdictionIsNotAndIdLessThanEqual("admin",0,0);
        map.put("users",users);
        map.put("usable",usable);
        return map;
    }
}
