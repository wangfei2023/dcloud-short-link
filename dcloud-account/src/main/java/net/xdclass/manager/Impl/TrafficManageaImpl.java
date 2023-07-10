/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/10 0010 19:00:12
 * @version 1.0
 */

package net.xdclass.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.TrafficManage;
import net.xdclass.mapper.TrafficMapper;
import net.xdclass.model.TrafficDO;
import net.xdclass.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
@Slf4j
public class TrafficManageaImpl implements TrafficManage {
    @Autowired
    private TrafficMapper trafficMapper;
    @Override
    public int add(TrafficDO trafficDO) {
        int rows = trafficMapper.insert(trafficDO);
        return rows;
    }

    @Override
    public IPage<TrafficDO> pageAvaliable(int page, int size, long accountNo) {
        Page<TrafficDO> pageInfo = new Page<>(page, size);
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");
        Page<TrafficDO> trafficDOPage = trafficMapper.selectPage(pageInfo, new QueryWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                //ge:大于
                .ge("expired_date", today).orderByDesc("gmt_create")
        );
        return trafficDOPage;
    }

    @Override
    public TrafficDO findByIdAndAccountNo(long trafficId, long accountNo) {
        TrafficDO trafficDO = trafficMapper.selectOne(new QueryWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                .eq("id", trafficId)
        );
        return trafficDO;
    }

    @Override
    public int addDayUsedTimes(long currentTrafficId, long accountNo, int daysUsedTimes) {
        int rows = trafficMapper.update(null, new UpdateWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                .eq("id", currentTrafficId)
                .set("day_used", daysUsedTimes)
        );
        log.info("当天使用了day_used：{}",daysUsedTimes);
        return rows;
    }
}


