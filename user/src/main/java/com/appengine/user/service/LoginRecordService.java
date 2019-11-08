package com.appengine.user.service;

import com.appengine.frame.utils.StatisticsTimeUtils;
import com.appengine.user.dao.LoginNumByDayDao;
import com.appengine.user.dao.LoginRecordDao;
import com.appengine.user.domain.po.LoginNumByDay;
import com.appengine.user.domain.po.LoginRecord;
import com.appengine.user.domain.po.TotalLoginNumByDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginRecordService {
    @Resource
    LoginRecordDao recordDao;
    @Resource
    LoginNumByDayDao dayDao;

    public LoginRecord add(LoginRecord record) {
        return recordDao.save(record);
    }

    public LoginNumByDay save(LoginNumByDay loginNumByDay) {
        // TODO
        LoginNumByDay day = dayDao.findFirstByYyyyMMdd(loginNumByDay.yyyyMMdd);
        if (day != null) {
            day.setNum(loginNumByDay.num);
            dayDao.save(day);
            return day;
        } else {
            return dayDao.save(loginNumByDay);
        }
    }

    public List<LoginNumByDay> getChannelLogin(long start, long end) {
        List<LoginNumByDay> result = new ArrayList<>();
        List<Object> r = recordDao.getChannelLoginNum(start, end);
        for (Object o : r) {
            Object[] temp = (Object[]) o;
            result.add(new LoginNumByDay(temp[0].toString(), StatisticsTimeUtils.getDateFromLong(start), Integer.parseInt(temp[1].toString())));
        }
        return result;
    }

    public Page<LoginRecord> getAllLoginUser(String channel, Pageable request) {
        return recordDao.findDistinctUidByChannel(channel, request);
    }

    public List<TotalLoginNumByDay> getAllNumByDay() {
        List<TotalLoginNumByDay> result = new ArrayList<>();
        List<Object> r = dayDao.getAllChannelLoginNum();
        for (Object o : r) {
            Object[] temp = (Object[]) o;
            result.add(new TotalLoginNumByDay(temp[0].toString(), temp[1].toString()));
        }
        return result;

    }

    public LoginRecord getByUid(Long uid) {
        return recordDao.getFirstByUid(uid);
    }

    public Iterable<LoginNumByDay> saveAllChannel2MysqlByDay() {
        if (dayDao.findFirstByYyyyMMdd(StatisticsTimeUtils.getPreviousDay()) != null) {
            return null;
        }
        long start = StatisticsTimeUtils.getZeroTime() - StatisticsTimeUtils.MS_OF_DAY;
        long end = StatisticsTimeUtils.getZeroTime();
        List<LoginNumByDay> yesterdayNum = getChannelLogin(start, end);
        return dayDao.saveAll(yesterdayNum);
    }
}
